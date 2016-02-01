package com.vlabs.androiweartest.manager;

import android.util.Log;

import com.path.android.jobqueue.JobManager;
import com.vlabs.androiweartest.job.BroadcastMessageJob;
import com.vlabs.wearcontract.helpers.WearAnalyticsConstants;
import com.vlabs.wearcontract.PlayStationData;
import com.vlabs.wearcontract.WearMessage;
import com.vlabs.wearcontract.WearPlayerState;
import com.vlabs.wearcontract.WearStation;
import com.vlabs.wearcontract.messages.ControlMessage;
import com.vlabs.wearcontract.messages.StateMessage;

import de.greenrobot.event.EventBus;

public class PlayerManager {

    private static final String TAG = PlayerManager.class.getSimpleName();

    private final JobManager mJobManager;

    public PlayerManager(
            final JobManager jobManager,
            final EventBus eventBus) {
        mJobManager = jobManager;
        eventBus.register(this);
    }

    private WearPlayerState mCurrentState = new WearPlayerState();

    public void playStation(final WearStation station, final WearAnalyticsConstants.WearPlayedFrom playedFrom) {
        Log.d(TAG, "Sending control message to play station: " + station);
        mJobManager.addJobInBackground(new BroadcastMessageJob(WearMessage.PLAY_STATION, new PlayStationData(station, playedFrom).toMap().toBundle()));
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
        mJobManager.addJobInBackground(new BroadcastMessageJob(WearMessage.CONTROL, new ControlMessage(command).asDataMap().toBundle()));
    }

    @SuppressWarnings("unused")
    public void onEventMainThread(final StateMessage event) {
        Log.d(TAG, "onPlayerState: " + event);
        mCurrentState = event.asPlayerState();
    }
}
