package com.vlabs.androiweartest.wear.model;

import com.google.android.gms.wearable.DataMap;
import com.google.android.gms.wearable.PutDataMapRequest;
import com.vlabs.androiweartest.integration.InPort;
import com.vlabs.androiweartest.wear.connection.ConnectionManager;
import com.vlabs.wearcontract.WearDataEvent;
import com.vlabs.wearcontract.WearStation;

import java.util.ArrayList;

import rx.functions.Action1;

public class RecentlyPlayedModel<OuterStation> implements Action1<WearStation> {

    private final ConnectionManager mConnectionManager;

    public RecentlyPlayedModel(InPort.RecentlyPlayedPin<OuterStation> inPort, ConnectionManager connectionManager) {
        mConnectionManager = connectionManager;
        inPort.onInnerChanged().subscribe(this);
    }

    @Override
    public void call(final WearStation station) {
        final PutDataMapRequest putRequest = PutDataMapRequest.create(WearDataEvent.PATH_STATIONS_RECENT);
        final ArrayList<DataMap> stationMaps = new ArrayList<>();
        putRequest.setUrgent();

        stationMaps.add(station.toMap());

        putRequest.getDataMap().putDataMapArrayList(WearDataEvent.KEY_STATIONS, stationMaps);
        mConnectionManager.putData(putRequest.asPutDataRequest());
    }
}
