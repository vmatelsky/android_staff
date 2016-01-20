package com.vlabs.androiweartest.models;

import android.util.Log;

import com.google.android.gms.wearable.DataMap;
import com.vlabs.DataMapBuilder;
import com.vlabs.androiweartest.helpers.analytics.WearAnalyticsConstants;
import com.vlabs.wearcontract.Message;
import com.vlabs.wearcontract.WearPlayerState;
import com.vlabs.wearcontract.WearStation;
import com.vlabs.wearmanagers.connection.ConnectionManager;

public class PlayerManager {

    private static final String TAG = PlayerManager.class.getSimpleName();
    private final ConnectionManager mConnectionManager;

    public PlayerManager(final ConnectionManager connectionManager) {
        mConnectionManager = connectionManager;
    }

    private WearPlayerState mCurrentState = new WearPlayerState();

    public void onPlayerState(final DataMap map) {
        final WearPlayerState newState = new WearPlayerState(map);
        Log.d(TAG, "onPlayerState: " + newState);
        mCurrentState = newState;
    }

    public void playStation(final WearStation station, final WearAnalyticsConstants.WearPlayedFrom playedFrom) {
        Log.d(TAG, "Sending control message to play station: " + station);
        mConnectionManager.broadcastMessage(Message.PATH_PLAY_STATION, new PlayStationData(station, playedFrom).toMap());
    }

    public void play() {
        sendControlCommand(Message.CONTROL_ACTION_PLAY);
    }

    public void stop() {
        sendControlCommand(Message.CONTROL_ACTION_STOP);
    }

    public boolean isPlaying() {
        return mCurrentState.isPlaying();
    }

    public WearPlayerState currentState() {
        return mCurrentState;
    }

    public void sendControlCommand(final String command) {
        Log.d(TAG, "Sending control message: " + command);
        mConnectionManager.broadcastMessage(Message.PATH_CONTROL, new DataMapBuilder()
                .putString(Message.KEY_ACTION, command)
                .getMap());
    }
}
