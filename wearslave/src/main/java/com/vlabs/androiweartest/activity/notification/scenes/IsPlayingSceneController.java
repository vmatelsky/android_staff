package com.vlabs.androiweartest.activity.notification.scenes;

import android.transition.Scene;
import android.view.View;
import android.widget.ImageButton;

import com.vlabs.androiweartest.R;
import com.vlabs.androiweartest.helpers.analytics.Analytics;
import com.vlabs.androiweartest.manager.PlayerManager;
import com.vlabs.wearcontract.WearAnalyticsConstants;
import com.vlabs.wearcontract.WearPlayerState;
import com.vlabs.wearcontract.messages.ControlMessage;

public class IsPlayingSceneController implements View.OnClickListener {

    private final PlayerManager mPlayerManager;
    private final Analytics mAnalytics;

    private ImageButton mVolumeDownButton;
    private ImageButton mVolUpButton;
    private ImageButton mThumbUpButton;
    private ImageButton mThumbDownButton;

    public IsPlayingSceneController(final Analytics analytics, final PlayerManager playerManager) {
        mPlayerManager = playerManager;
        mAnalytics = analytics;
    }

    public void onEnter(final Scene scene, final WearPlayerState state) {
        final View view = scene.getSceneRoot();

        mVolUpButton = (ImageButton) view.findViewById(R.id.control_vol_up);
        mVolumeDownButton = (ImageButton) view.findViewById(R.id.control_vol_down);
        mThumbUpButton = (ImageButton) view.findViewById(R.id.control_thumb_up);
        mThumbDownButton = (ImageButton) view.findViewById(R.id.control_thumb_down);
        mVolUpButton.setOnClickListener(this);
        mVolumeDownButton.setOnClickListener(this);
        mThumbUpButton.setOnClickListener(this);
        mThumbDownButton.setOnClickListener(this);

        handleIsPlayingState(state);
    }

    @Override
    public void onClick(final View v) {
        ControlMessage.ControlAction command = null;
        WearAnalyticsConstants.WearPlayerAction action = null;

        if (v == mVolUpButton) {
            command = ControlMessage.ControlAction.VOLUME_UP;
            action = WearAnalyticsConstants.WearPlayerAction.VOLUME_UP;
        } else if (v == mVolumeDownButton) {
            command = ControlMessage.ControlAction.VOLUME_DOWN;
            action = WearAnalyticsConstants.WearPlayerAction.VOLUME_DOWN;
        } else if (v == mThumbUpButton) {
            command = ControlMessage.ControlAction.THUMB_UP;
            action = WearAnalyticsConstants.WearPlayerAction.THUMB_UP;
            mThumbUpButton.setSelected(true);
        } else if (v == mThumbDownButton) {
            command = ControlMessage.ControlAction.THUMB_DOWN;
            action = WearAnalyticsConstants.WearPlayerAction.THUMB_DOWN;
            mThumbDownButton.setSelected(true);
        }

        tagAction(action);
        mPlayerManager.sendControlCommand(command);
    }

    void tagAction(WearAnalyticsConstants.WearPlayerAction action) {
        mAnalytics.broadcastRemoteAction(action);
    }

    public void handleIsPlayingState(WearPlayerState state) {
        if (state.isThumbsEnabled()) {
            mThumbDownButton.setEnabled(true);
            mThumbDownButton.setSelected(state.isThumbedDown());
            mThumbUpButton.setEnabled(true);
            mThumbUpButton.setSelected(state.isThumbedUp());
        } else {
            mThumbDownButton.setSelected(false);
            mThumbDownButton.setEnabled(false);
            mThumbUpButton.setSelected(false);
            mThumbUpButton.setEnabled(false);
        }

        if (state.getDeviceVolume() == 0) {
            mVolumeDownButton.setImageResource(R.drawable.ic_controls_volume_mute);
        } else {
            mVolumeDownButton.setImageResource(R.drawable.ic_controls_volume_down);
        }
    }
}
