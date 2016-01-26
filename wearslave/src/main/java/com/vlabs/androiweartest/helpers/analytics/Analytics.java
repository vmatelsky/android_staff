package com.vlabs.androiweartest.helpers.analytics;

import com.google.android.gms.wearable.DataMap;
import com.vlabs.DataMapBuilder;
import com.vlabs.androiweartest.manager.ConnectionManager;
import com.vlabs.wearcontract.WearMessage;
import com.vlabs.wearcontract.WearAnalyticsConstants;

public class Analytics {
    private final ConnectionManager mConnectionManager;

    public Analytics(ConnectionManager connectionManager) {
        mConnectionManager = connectionManager;
    }

    public void broadcastRemoteAction(WearAnalyticsConstants.WearPlayerAction action) {
        broadcast(makeRemoteActionData(action));
    }

    public void broadcastRemoteControl(WearAnalyticsConstants.WearBrowse browse) {
        broadcast(makeRemoteControlData(browse));
    }

    private void broadcast(DataMap data) {
        mConnectionManager.broadcastMessage(WearMessage.ANALYTICS, data);
    }

    private static DataMap makeRemoteActionData(WearAnalyticsConstants.WearPlayerAction action) {
        return makeWearAnalyticsBuilder(WearAnalyticsConstants.WearEvent.REMOTE_ACTION)
                .putInt(WearAnalyticsConstants.KEY_PLAYER_ACTION, action.ordinal())
                .getMap();
    }

    private static DataMap makeRemoteControlData(WearAnalyticsConstants.WearBrowse browse) {
        return makeWearAnalyticsBuilder(WearAnalyticsConstants.WearEvent.REMOTE_CONTROL)
                .putInt(WearAnalyticsConstants.KEY_BROWSE, browse.ordinal())
                .getMap();
    }

    private static DataMapBuilder makeWearAnalyticsBuilder(WearAnalyticsConstants.WearEvent event) {
        return new DataMapBuilder().putInt(WearAnalyticsConstants.KEY_EVENT, event.ordinal());
    }
}
