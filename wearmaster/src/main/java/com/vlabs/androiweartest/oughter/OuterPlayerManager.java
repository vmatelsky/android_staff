package com.vlabs.androiweartest.oughter;

import android.content.Context;
import android.media.AudioManager;

import com.vlabs.androiweartest.MasterApplication;
import com.vlabs.wearcontract.WearPlayerState;
import com.vlabs.wearcontract.WearStation;
import com.vlabs.wearcontract.dummy.DummyWearPlayerState;
import com.vlabs.wearcontract.model.Feedback;

public class OuterPlayerManager {

    private OuterPlayerState mPlayerState = new OuterPlayerState(DummyWearPlayerState.Dummy1.getData());

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

    private void syncPlayerState(final OuterPlayerState outerPlayerState) {
        MasterApplication.instance().integrationModule().playerStateChangedPin.call(outerPlayerState);
    }

    private void sendFeedbackMessage(final String feedback) {
        MasterApplication.instance().integrationModule().feedbackPin.call(new Feedback(feedback));
    }

    public void play(final WearStation station) {
        mPlayerState = new OuterPlayerState();

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
