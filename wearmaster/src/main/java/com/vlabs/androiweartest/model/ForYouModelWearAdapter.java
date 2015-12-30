package com.vlabs.androiweartest.model;

import com.google.android.gms.wearable.DataMap;
import com.google.android.gms.wearable.PutDataMapRequest;
import com.vlabs.androiweartest.MasterApplication;
import com.vlabs.wearcontract.Data;
import com.vlabs.wearcontract.WearStation;
import com.vlabs.wearcontract.dummy.DummyWearStation;
import com.vlabs.wearmanagers.connection.ConnectionManager;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;


public class ForYouModelWearAdapter {

    private final static String sPath = Data.PATH_STATIONS_FOR_YOU;

    private final List<WearStation> mLastObtainedStations;
    private ConnectionManager mConnectionManager;

    public ForYouModelWearAdapter() {
        mConnectionManager = MasterApplication.instance().connectionManager();
        mLastObtainedStations = DummyWearStation.Dummies;
    }

    public void refresh() {
        Collections.shuffle(mLastObtainedStations);
        onUpdated(sPath, mLastObtainedStations);
    }

    private void onUpdated(final String path, final List<WearStation> stations) {
        final PutDataMapRequest putRequest = PutDataMapRequest.create(path);
        if (stations == null) {
            mConnectionManager.deleteData(putRequest);
        } else {
            ArrayList<DataMap> stationMaps = new ArrayList<>();
            for (WearStation s : stations) {
                stationMaps.add(s.toMap());
            }
            putRequest.getDataMap().putDataMapArrayList(Data.KEY_STATIONS, stationMaps);
            putRequest.setUrgent();
            mConnectionManager.putData(putRequest);
        }
    }

}
