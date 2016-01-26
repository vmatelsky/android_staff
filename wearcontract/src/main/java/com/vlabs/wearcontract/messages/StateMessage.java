package com.vlabs.wearcontract.messages;

import com.google.android.gms.wearable.DataMap;
import com.vlabs.wearcontract.WearPlayerState;

public class StateMessage {

    private final WearPlayerState mPlayerState;

    public StateMessage(final WearPlayerState playerState) {
        mPlayerState = playerState;
    }

    public StateMessage(final DataMap dataMap) {
        this(new WearPlayerState(dataMap));
    }

    public WearPlayerState asPlayerState() {
        return mPlayerState;
    }

    public DataMap asDataMap() {
        return mPlayerState.getData();
    }

}
