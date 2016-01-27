package com.vlabs.androiweartest.wear.managers;

import com.vlabs.androiweartest.connection.ConnectionManager;
import com.vlabs.wearcontract.WearMessage;
import com.vlabs.wearcontract.WearPlayerState;
import com.vlabs.wearcontract.dummy.DummyWearPlayerState;
import com.vlabs.wearcontract.messages.FeedbackMessage;

public class WearPlayerManager {

    private WearPlayerState mPlayerState = DummyWearPlayerState.Dummy1;

    private final ConnectionManager mConnectionManager;

    public WearPlayerManager(ConnectionManager connectionManager) {
        mConnectionManager = connectionManager;
    }

    public void thumbDown() {
        mPlayerState.setThumbedState(WearPlayerState.VALUE_THUMBED_DOWN);

        syncPlayerState(mPlayerState);
        sendFeedbackMessage("Thumbed down");
    }

    public void thumbUp() {
        mPlayerState.setThumbedState(WearPlayerState.VALUE_THUMBED_UP);

        syncPlayerState(mPlayerState);
        sendFeedbackMessage("Thumbed up");
    }

    public void togglePlay() {
        final boolean wasPlaying = mPlayerState.isPlaying();
        mPlayerState.setIsPlaying(!wasPlaying);

        syncPlayerState(mPlayerState);

        final String feedback = wasPlaying ? "Stop" : "Play";
        sendFeedbackMessage(feedback);
    }

    private void syncPlayerState(final WearPlayerState wearPlayerState) {
        mConnectionManager.broadcastMessage(WearMessage.STATE, wearPlayerState.getData());
    }

    private void sendFeedbackMessage(final String feedback) {
        mConnectionManager.broadcastMessage(WearMessage.FEEDBACK, new FeedbackMessage(feedback).asDataMap());
    }

}
