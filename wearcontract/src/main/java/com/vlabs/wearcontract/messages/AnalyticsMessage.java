package com.vlabs.wearcontract.messages;

import com.google.android.gms.wearable.DataMap;
import com.vlabs.wearcontract.WearAnalyticsConstants;

public class AnalyticsMessage {

    public static final int NONE = -1;

    private final int mEventOrdinal;
    private final int mActionOrdinal;
    private final int mControlOrdinal;

    public AnalyticsMessage(final DataMap map) {
        mEventOrdinal = map.getInt(WearAnalyticsConstants.KEY_EVENT, NONE);
        mActionOrdinal = map.getInt(WearAnalyticsConstants.KEY_PLAYER_ACTION, NONE);
        mControlOrdinal = map.getInt(WearAnalyticsConstants.KEY_BROWSE, NONE);
    }

    public int eventOrdinal() {
        return mEventOrdinal;
    }

    public int actionOrdinal() {
        return mActionOrdinal;
    }

    public int controlOrdinal() {
        return mControlOrdinal;
    }
}
