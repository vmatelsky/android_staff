package com.vlabs.androiweartest.activity.notification.state;

import android.support.annotation.Nullable;
import android.view.View;
import android.widget.TextView;

import com.vlabs.androiweartest.R;
import com.vlabs.androiweartest.helpers.PlayedFromUtils;
import com.vlabs.androiweartest.helpers.analytics.Analytics;
import com.vlabs.androiweartest.models.PlayerManager;
import com.vlabs.wearcontract.WearDataEvent;
import com.vlabs.wearcontract.WearAnalyticsConstants;
import com.vlabs.wearcontract.WearStation;

public class IsPausedViewController implements View.OnClickListener {

    private final View mView;
    private final TextView mTitleView;
    private final Analytics mAnalytics;
    private final PlayerManager mPlayerManager;

    private WearStation mStation;

    public IsPausedViewController(final View view, final Analytics analytics, final PlayerManager playerManager) {
        mView = view;
        final TextView stationButton = (TextView) view.findViewById(R.id.station_name_button);
        mTitleView = (TextView) view.findViewById(R.id.title);
        stationButton.setOnClickListener(this);

        mAnalytics = analytics;
        mPlayerManager = playerManager;
    }

    public void show(@Nullable final WearStation station) {
        mView.setVisibility(View.VISIBLE);

        mStation = station;
        if (station == null) {
            mTitleView.setText("");
        } else {
            mTitleView.setText(station.name());
        }
    }

    public void hide() {
        mView.setVisibility(View.GONE);
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
