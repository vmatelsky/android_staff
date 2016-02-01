package com.vlabs.androiweartest.activity.notification.scenes;

import android.support.annotation.Nullable;
import android.transition.Scene;
import android.view.View;
import android.widget.TextView;

import com.vlabs.androiweartest.R;
import com.vlabs.androiweartest.helpers.PlayedFromUtils;
import com.vlabs.androiweartest.helpers.analytics.Analytics;
import com.vlabs.androiweartest.manager.PlayerManager;
import com.vlabs.wearcontract.WearAnalyticsConstants;
import com.vlabs.wearcontract.WearDataEvent;
import com.vlabs.wearcontract.WearStation;

public class IsPausedSceneController implements View.OnClickListener {

    private final PlayerManager mPlayerManager;
    private final Analytics mAnalytics;

    private TextView mTitleView;
    private WearStation mStation;

    public IsPausedSceneController(final Analytics analytics, final PlayerManager playerManager) {
        mPlayerManager = playerManager;
        mAnalytics = analytics;
    }

    public void onEnter(final Scene scene, @Nullable final WearStation station) {
        final View view = scene.getSceneRoot();
        final TextView stationButton = (TextView) view.findViewById(R.id.station_name_button);

        mStation = station;
        mTitleView = (TextView) view.findViewById(R.id.title);
        stationButton.setOnClickListener(this);


        if (station == null) {
            mTitleView.setText("");
        } else {
            mTitleView.setText(station.name());
        }
    }

    @Override
    public void onClick(final View v) {
        if (mStation != null) {
            mPlayerManager.playStation(mStation, PlayedFromUtils.getWearPlayedFrom(mStation, WearDataEvent.PATH_STATIONS_RECENT));
            tagPlay();
        }
    }

    void tagPlay() {
        mAnalytics.broadcastRemoteAction(WearAnalyticsConstants.WearPlayerAction.PLAY);
    }
}
