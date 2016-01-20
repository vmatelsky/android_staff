package com.vlabs.androiweartest.events.message;

import com.google.android.gms.wearable.DataMap;
import com.google.android.gms.wearable.MessageEvent;
import com.vlabs.wearcontract.WearPlayerState;

public class OnPlayerState {

    public static OnPlayerState fromMessageEvent(final MessageEvent event) {
        final DataMap map = DataMap.fromByteArray(event.getData());
        return new OnPlayerState(new WearPlayerState(map));
    }

    private final WearPlayerState mPlayerState;

    public OnPlayerState(final WearPlayerState playerState) {
        mPlayerState = playerState;
    }

    public WearPlayerState playerState() {
        return mPlayerState;
    }

}
