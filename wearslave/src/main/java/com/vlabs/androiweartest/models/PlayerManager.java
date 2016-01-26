package com.vlabs.androiweartest.models;

import android.util.Log;

import com.vlabs.androiweartest.helpers.analytics.WearAnalyticsConstants;
import com.vlabs.androiweartest.manager.ConnectionManager;
import com.vlabs.wearcontract.WearMessage;
import com.vlabs.wearcontract.WearPlayerState;
import com.vlabs.wearcontract.WearStation;
import com.vlabs.wearcontract.messages.ControlMessage;
import com.vlabs.wearcontract.messages.StateMessage;

import de.greenrobot.event.EventBus;

public class PlayerManager {

    private static final String TAG = PlayerManager.class.getSimpleName();

    private final ConnectionManager mConnectionManager;

    public PlayerManager(
            final ConnectionManager connectionManager,
            final EventBus eventBus) {
        mConnectionManager = connectionManager;
        eventBus.register(this);
    }

    private WearPlayerState mCurrentState = new WearPlayerState();

    public void playStation(final WearStation station, final WearAnalyticsConstants.WearPlayedFrom playedFrom) {
        Log.d(TAG, "Sending control message to play station: " + station);
        mConnectionManager.broadcastMessage(WearMessage.PLAY_STATION, new PlayStationData(station, playedFrom).toMap());
    }

    public void play() {
        sendControlCommand(ControlMessage.ControlAction.PLAY);
    }

    public void stop() {
        sendControlCommand(ControlMessage.ControlAction.STOP);
    }

    public boolean isPlaying() {
        return mCurrentState.isPlaying();
    }

    public WearPlayerState currentState() {
        return mCurrentState;
    }

    public void sendControlCommand(final ControlMessage.ControlAction command) {
        Log.d(TAG, "Sending control message: " + command);
        mConnectionManager.broadcastMessage(WearMessage.CONTROL, new ControlMessage(command).asDataMap());
    }

    @SuppressWarnings("unused")
    public void onEventMainThread(final StateMessage event) {
        Log.d(TAG, "onPlayerState: " + event);
        mCurrentState = event.asPlayerState();
    }
}
