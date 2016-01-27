package com.vlabs.androiweartest.wear.managers;

import com.google.android.gms.wearable.DataMap;
import com.google.android.gms.wearable.PutDataMapRequest;
import com.vlabs.androiweartest.MasterApplication;
import com.vlabs.wearcontract.WearDataEvent;
import com.vlabs.wearcontract.WearStation;
import com.vlabs.wearcontract.dummy.DummyWearStation;

import java.util.ArrayList;

import rx.Observable;
import rx.functions.Action1;

public class RecentlyPlayedManager implements Action1<WearStation> {

    WearStation mRecentlyPlayedStation = DummyWearStation.Dummy1;

    public RecentlyPlayedManager(Observable<WearStation> onRecentStationChanged) {
        onRecentStationChanged.subscribe(this);
    }

    @Override
    public void call(final WearStation station) {
        mRecentlyPlayedStation = station;

        final PutDataMapRequest putRequest = PutDataMapRequest.create(WearDataEvent.PATH_STATIONS_RECENT);
        final ArrayList<DataMap> stationMaps = new ArrayList<>();
        putRequest.setUrgent();

        stationMaps.add(station.toMap());

        putRequest.getDataMap().putDataMapArrayList(WearDataEvent.KEY_STATIONS, stationMaps);
        MasterApplication.instance().wearFacade().connectionManager().putData(putRequest.asPutDataRequest());
    }
}
