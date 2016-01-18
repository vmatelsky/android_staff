package com.vlabs.androiweartest.helpers;

import android.text.TextUtils;

import com.vlabs.androiweartest.helpers.analytics.WearAnalyticsConstants;
import com.vlabs.wearcontract.Data;
import com.vlabs.wearcontract.WearStation;

public class PlayedFromUtils {

    public static WearAnalyticsConstants.WearPlayedFrom getWearPlayedFrom(final WearStation wearStation, final String path) {
        if (TextUtils.isEmpty(path)) return null;

        if (Data.PATH_STATIONS_FOR_YOU.equals(path)) {
            return WearAnalyticsConstants.WearPlayedFrom.FOR_YOU;
        } else if (Data.PATH_STATIONS_MY_STATIONS.equals(path)) {
            if (wearStation.isFavorite()) {
                return WearAnalyticsConstants.WearPlayedFrom.MY_STATIONS_FAVORITE;
            } else {
                return WearAnalyticsConstants.WearPlayedFrom.MY_STATIONS_RECENT;
            }
        } else {
            return null;
        }
    }

}
