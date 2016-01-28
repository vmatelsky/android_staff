package com.vlabs.androiweartest.wear.model;

import com.google.android.gms.wearable.DataMap;
import com.google.android.gms.wearable.PutDataMapRequest;
import com.vlabs.androiweartest.integration.InPort;
import com.vlabs.androiweartest.wear.connection.ConnectionManager;
import com.vlabs.wearcontract.WearDataEvent;
import com.vlabs.wearcontract.WearStation;

import java.util.ArrayList;
import java.util.List;

import rx.functions.Action1;

public abstract class StationsModel<OuterType> implements Action1<List<WearStation>> {

    public static class ForYouModel<OuterType> extends StationsModel<OuterType> {

        public ForYouModel(final InPort.ForYouPin<OuterType> forYouPin, final ConnectionManager connectionManager) {
            super(forYouPin, connectionManager, WearDataEvent.PATH_STATIONS_FOR_YOU);
        }
    }

    public static class MyStationsModel<OuterStation> extends StationsModel<OuterStation> {

        public MyStationsModel(final InPort.MyStationsPin<OuterStation> myStationsPin, final ConnectionManager connectionManager) {
            super(myStationsPin, connectionManager, WearDataEvent.PATH_STATIONS_MY_STATIONS);
        }
    }

    public static class SearchStationsModel<OuterStation> extends StationsModel<OuterStation> {

        public SearchStationsModel(final InPort.SearchStationsPin<OuterStation> searchStationsPin, final ConnectionManager connectionManager) {
            super(searchStationsPin, connectionManager, WearDataEvent.PATH_STATIONS_SEARCH);
        }
    }

    private ConnectionManager mConnectionManager;
    private final String mPath;

    public StationsModel(
            InPort<OuterType, List<WearStation>> forYouPin,
            final ConnectionManager connectionManager,
            final String path) {
        forYouPin.onInnerChanged().subscribe(this);
        mConnectionManager = connectionManager;
        mPath = path;
    }

    @Override
    public void call(final List<WearStation> stations) {
        final PutDataMapRequest putRequest = PutDataMapRequest.create(mPath);

        if (stations == null) {
            mConnectionManager.deleteData(putRequest);
        } else {
            ArrayList<DataMap> stationMaps = new ArrayList<>();
            for (WearStation s : stations) {
                stationMaps.add(s.toMap());
            }
            putRequest.getDataMap().putDataMapArrayList(WearDataEvent.KEY_STATIONS, stationMaps);
            putRequest.setUrgent();
            mConnectionManager.putData(putRequest.asPutDataRequest());
        }
    }
}
