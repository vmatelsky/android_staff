package com.vlabs.androiweartest.helpers;

import android.support.annotation.Nullable;
import android.util.Log;

import com.google.android.gms.wearable.DataEvent;
import com.google.android.gms.wearable.DataMap;
import com.vlabs.wearcontract.Data;
import com.vlabs.wearcontract.WearStation;

import java.util.ArrayList;
import java.util.List;

public class WearStationsParser {

    private static final String TAG = WearStationsParser.class.getSimpleName();

    @Nullable
    public static List<WearStation> fromDataEvent(final DataEvent dataEvent) {
        if (dataEvent.getType() == DataEvent.TYPE_CHANGED) {
            return fromDataMap(DataMap.fromByteArray(dataEvent.getDataItem().getData()));
        } else if (dataEvent.getType() == DataEvent.TYPE_DELETED) {
            return null;
        }

        Log.d(TAG, "Unknown event type: " + dataEvent.getType());
        return null;
    }

    public static List<WearStation> fromDataMap(final DataMap dataMap) {
        if (dataMap == null) return new ArrayList<>();

        final ArrayList<DataMap> dataMapArrayList = dataMap.getDataMapArrayList(Data.KEY_STATIONS);

        if (dataMapArrayList == null) return new ArrayList<>();

        List<WearStation> stations = new ArrayList<>(dataMapArrayList.size());

        for (DataMap stationMap : dataMapArrayList) {
            stations.add(WearStation.fromDataMap(stationMap));
        }

        return stations;
    }

}
