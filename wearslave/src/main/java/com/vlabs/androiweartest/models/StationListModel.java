package com.vlabs.androiweartest.models;

import com.google.android.gms.wearable.DataEvent;
import com.google.android.gms.wearable.DataMap;
import com.vlabs.androiweartest.WearApplication;
import com.vlabs.wearcontract.Data;
import com.vlabs.wearcontract.WearStation;
import com.vlabs.wearmanagers.connection.ConnectionManager;
import com.vlabs.wearmanagers.message.MessageManager;

import java.util.ArrayList;
import java.util.List;

import rx.Observable;
import rx.Subscription;
import rx.functions.Action1;
import rx.subjects.PublishSubject;

public class StationListModel {

    private final String mPath;
    private final MessageManager mMessageManager;
    private final PublishSubject<List<WearStation>> mOnStationsChanged = PublishSubject.create();

    private Action1<DataEvent> mOnStationListChanged = new Action1<DataEvent>() {
        @Override
        public void call(final DataEvent dataEvent) {
            final DataMap dataMap = DataMap.fromByteArray(dataEvent.getDataItem().getData());
            onStationsChanged(processDataMap(dataMap));
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


    public StationListModel(final MessageManager messageManager, final String path) {
        mPath = path;
        mMessageManager = messageManager;
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

    public void getData(final Action1<List<WearStation>> receiver) {
        WearApplication.instance().connectionManager().getDataItems(mPath, new ConnectionManager.DataListener() {
            @Override
            public void onData(final String path, final DataMap map) {
                receiver.call(processDataMap(map));
            }
        });
    }
}
