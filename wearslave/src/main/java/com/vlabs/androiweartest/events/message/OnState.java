package com.vlabs.androiweartest.events.message;

import com.google.android.gms.wearable.DataMap;
import com.google.android.gms.wearable.MessageEvent;
import com.vlabs.wearcontract.WearPlayerState;

public class OnState {

    public static OnState fromMessageEvent(final MessageEvent event) {
        final DataMap map = DataMap.fromByteArray(event.getData());
        return new OnState(new WearPlayerState(map));
    }

    private final WearPlayerState mPlayerState;

    public OnState(final WearPlayerState playerState) {
        mPlayerState = playerState;
    }

    public WearPlayerState playerState() {
        return mPlayerState;
    }

}
