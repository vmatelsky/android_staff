package com.vlabs.wearcontract;

public class WearAnalyticsConstants {
    public static final String KEY_EVENT = "event";
    public static final String KEY_PLAYER_ACTION = "player_action";
    public static final String KEY_BROWSE = "browse";

    public enum WearEvent {
        REMOTE_ACTION,
        REMOTE_CONTROL,
    }

    public enum WearPlayerAction {
        PLAY,
        THUMB_UP,
        THUMB_DOWN,
        VOLUME_UP,
        VOLUME_DOWN,
    }

    public enum WearBrowse {
        FOR_YOU,
        VOICE_SEARCH,
        MY_STATIONS,
        VOICE_SEARCH_SYSTEM,
    }
}
