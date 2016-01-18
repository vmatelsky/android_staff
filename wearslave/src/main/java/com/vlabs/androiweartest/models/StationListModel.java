package com.vlabs.androiweartest.models;

import android.text.TextUtils;

import com.google.android.gms.wearable.DataEvent;
import com.google.android.gms.wearable.DataMap;
import com.vlabs.androiweartest.helpers.analytics.WearAnalyticsConstants;
import com.vlabs.wearcontract.Data;
import com.vlabs.wearcontract.WearStation;
import com.vlabs.wearmanagers.connection.ConnectionManager;
import com.vlabs.wearmanagers.message.MessageManager;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

import rx.Observable;
import rx.Subscription;
import rx.functions.Action1;
import rx.subjects.PublishSubject;

public class StationListModel {

    private final String mPath;
    private final MessageManager mMessageManager;
    private final ConnectionManager mConnectionManager;

    private final PublishSubject<List<WearStation>> mOnStationsChanged = PublishSubject.create();

    private Action1<DataEvent> mOnStationListChanged = new Action1<DataEvent>() {
        @Override
        public void call(final DataEvent dataEvent) {
            if (dataEvent.getType() == DataEvent.TYPE_CHANGED) {
                final DataMap dataMap = DataMap.fromByteArray(dataEvent.getDataItem().getData());
                onStationsChanged(processDataMap(dataMap));
            } else if (dataEvent.getType() == DataEvent.TYPE_DELETED) {
            }
        }
    };

    private Subscription mCurrentSubscription;

    private List<WearStation> processDataMap(final DataMap dataMap) {
        if (dataMap == null) return new ArrayList<>();

        final ArrayList<DataMap> dataMapArrayList = dataMap.getDataMapArrayList(Data.KEY_STATIONS);

        if (dataMapArrayList == null) return new ArrayList<>();

        List<WearStation> stations = new ArrayList<>(dataMapArrayList.size());

        for (DataMap stationMap : dataMapArrayList) {
            stations.add(WearStation.fromDataMap(stationMap));
        }

        return stations;
    }

    public StationListModel(
            final MessageManager messageManager,
            final ConnectionManager connectionManager,
            final String path) {
        mPath = path;
        mMessageManager = messageManager;
        mConnectionManager = connectionManager;
    }

    public void startListening() {
        mCurrentSubscription = mMessageManager.onDataByPath(mPath).subscribe(mOnStationListChanged);
    }

    public void stopListening() {
        if (mCurrentSubscription != null) {
            mCurrentSubscription.unsubscribe();
            mCurrentSubscription = null;
        }
    }

    public Observable<List<WearStation>> onStationsChanged() {
        return mOnStationsChanged;
    }

    private void onStationsChanged(List<WearStation> stations) {
        mOnStationsChanged.onNext(stations);
    }

    public WearAnalyticsConstants.WearPlayedFrom getWearPlayedFrom(final WearStation wearStation) {
        if (TextUtils.isEmpty(mPath)) return null;

        if (Data.PATH_STATIONS_FOR_YOU.equals(mPath)) {
            return WearAnalyticsConstants.WearPlayedFrom.FOR_YOU;
        } else if (Data.PATH_STATIONS_MY_STATIONS.equals(mPath)) {
            if (wearStation.isFavorite()) {
                return WearAnalyticsConstants.WearPlayedFrom.MY_STATIONS_FAVORITE;
            } else {
                return WearAnalyticsConstants.WearPlayedFrom.MY_STATIONS_RECENT;
            }
        } else {
            return null;
        }
    }

    public void refresh() {
        mConnectionManager.getDataItems(mPath, new ConnectionManager.DataListener() {
            @Override
            public void onData(final String path, final DataMap map) {
                onStationsChanged(processDataMap(map));
            }
        });
    }
}
