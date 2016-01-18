package com.vlabs.androiweartest.events.data;

import android.support.annotation.Nullable;

import com.google.android.gms.wearable.DataEvent;
import com.google.android.gms.wearable.DataMap;
import com.vlabs.androiweartest.helpers.WearStationsParser;
import com.vlabs.wearcontract.WearStation;

import java.util.List;

public class OnStations {

    public static OnStations fromDataEvent(final DataEvent dataEvent, final String path) {
        return new OnStations(
                path,
                EventType.fromDataEventType(dataEvent.getType()),
                WearStationsParser.fromDataEvent(dataEvent));
    }

    public static OnStations fromDataMap(final DataMap map, final String path) {
        return new OnStations(
                path,
                EventType.GET,
                WearStationsParser.fromDataMap(map));
    }

    private final String mPath;
    private final EventType mEventType;
    private final List<WearStation> mStations;

    public OnStations(final String path, EventType eventType, final List<WearStation> stations) {
        mPath = path;
        mEventType = eventType;
        mStations = stations;
    }

    public String path() {
        return mPath;
    }

    public EventType eventType() {
        return mEventType;
    }

    @Nullable
    public List<WearStation> stations() {
        return mStations;
    }
}
