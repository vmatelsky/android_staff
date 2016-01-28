package com.vlabs.wearcontract;

import com.google.android.gms.wearable.DataMap;
import com.vlabs.DataMapBuilder;
import com.vlabs.wearcontract.helpers.WearAnalyticsConstants;

public class PlayStationData {
    private static final String FIELD_STATION = "wear-station";

    public final WearStation station;
    public final WearAnalyticsConstants.WearPlayedFrom playedFrom;

    public PlayStationData(WearStation station, WearAnalyticsConstants.WearPlayedFrom playedFrom) {
        this.station = station;
        this.playedFrom = playedFrom;
    }

    private PlayStationData(DataMap map) {
        this.station = WearStation.fromDataMap(map.getDataMap(FIELD_STATION));
        this.playedFrom = WearAnalyticsConstants.WearPlayedFrom.fromDataMap(map);
    }

    public DataMap toMap() {
        DataMapBuilder dataMapBuilder = new DataMapBuilder();

        if (station != null) {
            dataMapBuilder.putDataMap(FIELD_STATION, station.toMap());
        }

        if (playedFrom != null) {
            playedFrom.putValues(dataMapBuilder);
        }

        return dataMapBuilder.getMap();
    }


    public static PlayStationData fromDataMap(DataMap map) {
        if (map == null) return null;
        return new PlayStationData(map);
    }
}
