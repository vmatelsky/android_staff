package com.vlabs.androiweartest.oughter;

import android.content.Context;
import android.media.AudioManager;

import com.vlabs.androiweartest.MasterApplication;
import com.vlabs.androiweartest.wear.connection.ConnectionManager;
import com.vlabs.wearcontract.WearMessage;
import com.vlabs.wearcontract.WearPlayerState;
import com.vlabs.wearcontract.WearStation;
import com.vlabs.wearcontract.dummy.DummyWearPlayerState;
import com.vlabs.wearcontract.messages.FeedbackMessage;

public class OuterPlayerManager {

    private WearPlayerState mPlayerState = DummyWearPlayerState.Dummy1;

    private final Context mContext;

    public OuterPlayerManager(final Context context) {
        mContext = context;
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
//        mConnectionManager.broadcastMessage(WearMessage.STATE, wearPlayerState.getData());
    }

    private void sendFeedbackMessage(final String feedback) {
//        mConnectionManager.broadcastMessage(WearMessage.FEEDBACK, new FeedbackMessage(feedback).asDataMap());
    }

    public void play(final WearStation station) {
        mPlayerState = new WearPlayerState();

        mPlayerState.setIsPlaying(true);
        mPlayerState.setThumbedState(WearPlayerState.UNKNOWN_VOLUME);
        mPlayerState.setImagePath(station.getImagePath());
        mPlayerState.setDeviceVolume(getDeviceVolume());
        mPlayerState.setThumbsEnabled(!station.isLive());
        mPlayerState.setTitle(station.name());

        syncPlayerState(mPlayerState);
    }

    private int getDeviceVolume() {
        AudioManager audioManager = (AudioManager) mContext.getSystemService(Context.AUDIO_SERVICE);
        return audioManager.getStreamVolume(AudioManager.STREAM_MUSIC);
    }
}
