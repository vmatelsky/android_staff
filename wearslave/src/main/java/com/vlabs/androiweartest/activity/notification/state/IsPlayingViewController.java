package com.vlabs.androiweartest.activity.notification.state;

import android.view.View;
import android.widget.ImageButton;

import com.vlabs.androiweartest.R;
import com.vlabs.androiweartest.helpers.analytics.Analytics;
import com.vlabs.androiweartest.models.PlayerManager;
import com.vlabs.wearcontract.WearAnalyticsConstants;
import com.vlabs.wearcontract.WearPlayerState;
import com.vlabs.wearcontract.messages.ControlMessage;

public class IsPlayingViewController implements View.OnClickListener {

    private final PlayerManager mPlayerManager;
    private final Analytics mAnalytics;

    private final ImageButton mVolumeDownButton;
    private final ImageButton mVolUpButton;
    private final ImageButton mThumbUpButton;
    private final ImageButton mThumbDownButton;
    private final View mView;

    public IsPlayingViewController(final View view, final Analytics analytics, final PlayerManager playerManager) {
        mView = view;
        mPlayerManager = playerManager;
        mAnalytics = analytics;

        mVolUpButton = (ImageButton) view.findViewById(R.id.control_vol_up);
        mVolumeDownButton = (ImageButton) view.findViewById(R.id.control_vol_down);
        mThumbUpButton = (ImageButton) view.findViewById(R.id.control_thumb_up);
        mThumbDownButton = (ImageButton) view.findViewById(R.id.control_thumb_down);
        mVolUpButton.setOnClickListener(this);
        mVolumeDownButton.setOnClickListener(this);
        mThumbUpButton.setOnClickListener(this);
        mThumbDownButton.setOnClickListener(this);
    }

    public void show(final WearPlayerState state) {
        mView.setVisibility(View.VISIBLE);
        handleIsPlayingState(state);
    }

    public void hide() {
        mView.setVisibility(View.GONE);
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
