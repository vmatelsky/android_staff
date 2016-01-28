package com.vlabs.androiweartest.oughter.model;

import com.vlabs.androiweartest.MasterApplication;
import com.vlabs.androiweartest.oughter.OuterStation;
import com.vlabs.wearcontract.dummy.DummyWearStation;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class MyStationsWearAdapter {

    private final List<OuterStation> mLastObtainedStations;

    public MyStationsWearAdapter() {
        mLastObtainedStations = new ArrayList<>();
        for(int i = 0; i < DummyWearStation.Dummies.size(); i++) {
            mLastObtainedStations.add(new OuterStation(i));
        }
    }

    public void refresh() {
        Collections.shuffle(mLastObtainedStations);
        MasterApplication.instance().integrationModule().myStationsPin.call(mLastObtainedStations);
    }

}
