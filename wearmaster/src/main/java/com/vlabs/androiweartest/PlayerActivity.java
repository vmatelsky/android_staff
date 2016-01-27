package com.vlabs.androiweartest;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import com.vlabs.wearcontract.WearMessage;
import com.vlabs.wearcontract.WearPlayerState;
import com.vlabs.wearcontract.dummy.DummyWearPlayerState;
import com.vlabs.wearcontract.messages.FeedbackMessage;

public class PlayerActivity extends AppCompatActivity {

    private WearPlayerState mPlayerState = DummyWearPlayerState.Dummy1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_player);

        findViewById(R.id.toggle_play).setOnClickListener(v -> togglePlay());

        findViewById(R.id.thumb_up).setOnClickListener(v -> thumbUp());

        findViewById(R.id.thumb_down).setOnClickListener(v -> thumbDown());
    }

    private void thumbDown() {
        mPlayerState.setThumbedState(WearPlayerState.VALUE_THUMBED_DOWN);

        syncPlayerState(mPlayerState);
        sendFeedbackMessage("Thumbed down");
    }

    private void thumbUp() {
        mPlayerState.setThumbedState(WearPlayerState.VALUE_THUMBED_UP);

        syncPlayerState(mPlayerState);
        sendFeedbackMessage("Thumbed up");
    }

    private void togglePlay() {
        final boolean wasPlaying = mPlayerState.isPlaying();
        mPlayerState.setIsPlaying(!wasPlaying);

        syncPlayerState(mPlayerState);

        final String feedback = wasPlaying ? "Stop" : "Play";
        sendFeedbackMessage(feedback);
    }

    private void syncPlayerState(final WearPlayerState wearPlayerState) {
        MasterApplication.instance().connectionManager().broadcastMessage(WearMessage.STATE, wearPlayerState.getData());
    }

    private void sendFeedbackMessage(final String feedback) {
        MasterApplication.instance().connectionManager().broadcastMessage(WearMessage.FEEDBACK, new FeedbackMessage(feedback).asDataMap());
    }

}
