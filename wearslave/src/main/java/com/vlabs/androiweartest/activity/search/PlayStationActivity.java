package com.vlabs.androiweartest.activity.search;

import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.text.TextUtils;
import android.view.View;
import android.widget.TextView;

import com.clearchannel.iheartradio.controller.view.ImageByDataPathView;
import com.vlabs.androiweartest.R;
import com.vlabs.androiweartest.activity.BaseActivity;
import com.vlabs.androiweartest.behavior.ChangeBackgroundBehavior;
import com.vlabs.androiweartest.events.data.OnStations;
import com.vlabs.androiweartest.helpers.PlayedFromUtils;
import com.vlabs.androiweartest.helpers.analytics.Analytics;
import com.vlabs.androiweartest.manager.ConnectionManager;
import com.vlabs.androiweartest.manager.PlayerManager;
import com.vlabs.wearcontract.WearAnalyticsConstants;
import com.vlabs.wearcontract.WearExtras;
import com.vlabs.wearcontract.WearStation;

import java.util.List;

import javax.inject.Inject;

import de.greenrobot.event.EventBus;

public class PlayStationActivity extends BaseActivity  {

    @Inject
    PlayerManager mPlayerManager;

    @Inject
    ConnectionManager mConnectionManager;

    @Inject
    Analytics mAnalytics;

    @Inject
    EventBus mEventBus;

    @Inject
    ChangeBackgroundBehavior mBehavior;

    private WearStation mStation;
    private TextView mStationButton;
    private ImageByDataPathView mBackground;

    private final View.OnClickListener onPlayStationListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            if (mStation != null) {
                clearPlayButton();
                mPlayerManager.playStation(mStation, PlayedFromUtils.getWearPlayedFrom(mStation, getPath()));
                tagPlay();
                finishAffinity();
            }
        }
    };

    @Override
    protected void onCreate(final Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_play_station);
        verifyIntent();

        getComponent().inject(this);

        mStationButton = (TextView) findViewById(R.id.station_name_button);
        mStationButton.setOnClickListener(onPlayStationListener);

        mBackground = (ImageByDataPathView) findViewById(R.id.station_background);

        final String titleText = getIntent().getStringExtra(WearExtras.EXTRA_TITLE);
        final TextView title = (TextView) findViewById(R.id.title);
        title.setText(titleText);
    }

    private String getPath() {
        return getIntent().getStringExtra(WearExtras.EXTRA_STATION_LIST_PATH);
    }

    private void updateStationUi() {
        final String stationButtonText;
        if (mStation == null) {
            stationButtonText = "";
            clearPlayButton();
        } else {
            stationButtonText = mStation.name();
            updateStationButton();
        }

        mStationButton.setText(stationButtonText);
    }

    private void clearPlayButton() {
        mStationButton.setCompoundDrawablesWithIntrinsicBounds(null, null, null, null);
    }

    private void updateStationButton() {
        mStationButton.setCompoundDrawablesWithIntrinsicBounds(null, null, drawableForStation(), null);
    }

    private Drawable drawableForStation() {
        if (mStation == null) {
            return null;
        }
        return ContextCompat.getDrawable(this, R.drawable.ic_blackcard_play);
    }

    @Override
    protected void onStart() {
        super.onStart();
        mBehavior.activateFor(mBackground);
        mEventBus.register(this);
    }

    @Override
    protected void onStop() {
        super.onStop();
        mBehavior.deactivateFor(mBackground);
        mEventBus.unregister(this);
    }

    private void verifyIntent() {
        if (TextUtils.isEmpty(getIntent().getStringExtra(WearExtras.EXTRA_TITLE))) {
            throw new IllegalStateException("No title passed as extra, please pass String as EXTRA_TITLE");
        }
    }

    void tagPlay() {
        mAnalytics.broadcastRemoteAction(WearAnalyticsConstants.WearPlayerAction.PLAY);
    }

    @SuppressWarnings("unused")
    public void onEventMainThread(OnStations event) {
        if (isFinishing()) return;

        if (event.path().equals(getPath())) {
            final List<WearStation> wearStations = event.stations();
            if (wearStations == null || wearStations.isEmpty()) {
                mStation = null;
            } else {
                mStation = wearStations.get(0);
            }
            updateStationUi();
        }
    }
}
