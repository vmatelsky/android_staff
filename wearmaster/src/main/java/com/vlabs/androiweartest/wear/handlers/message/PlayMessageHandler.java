package com.vlabs.androiweartest.wear.handlers.message;

import com.google.android.gms.wearable.DataMap;
import com.google.android.gms.wearable.MessageEvent;
import com.vlabs.androiweartest.wear.managers.WearPlayerManager;
import com.vlabs.wearcontract.PlayStationData;

public class PlayMessageHandler implements MessageHandler {

    private final WearPlayerManager mPlayerManager;

    public PlayMessageHandler(final WearPlayerManager playerManager) {
        mPlayerManager = playerManager;
    }

    @Override
    public void handle(final MessageEvent messageEvent) {
        PlayStationData data = PlayStationData.fromDataMap(DataMap.fromByteArray(messageEvent.getData()));

        mPlayerManager.play(data.station);

        // TODO: tag analytics??

    }

}
