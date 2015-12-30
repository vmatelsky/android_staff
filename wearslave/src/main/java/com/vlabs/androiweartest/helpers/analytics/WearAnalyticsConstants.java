package com.vlabs.androiweartest.helpers.analytics;

import com.google.android.gms.wearable.DataMap;

public class WearAnalyticsConstants {
    private static final int NONE = -1;

    public static enum WearPlayedFrom {
        PLAY,
        FOR_YOU,
        MY_STATIONS_FAVORITE,
        MY_STATIONS_RECENT;

        public static final String KEY = "wear-played-from";

        public static WearPlayedFrom fromDataMap(DataMap map) {
            int ordinal = map.getInt(KEY);

            if (ordinal == NONE) {
                return null;
            } else {
                return WearPlayedFrom.values()[ordinal];
            }
        }

//        public void putValues(DataMapBuilder dataMapBuilder) {
//            dataMapBuilder.putInt(KEY, ordinal());
//        }
    }
}
