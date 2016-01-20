package com.vlabs.androiweartest.activity.showMore;

import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.content.ContextCompat;

import com.clearchannel.iheartradio.controller.view.ImageByDataPathView;
import com.google.android.gms.wearable.DataMap;
import com.vlabs.androiweartest.R;
import com.vlabs.androiweartest.activity.BaseActivity;
import com.vlabs.androiweartest.events.message.OnPlayerState;
import com.vlabs.androiweartest.models.PlayerManager;
import com.vlabs.wearcontract.Data;
import com.vlabs.wearcontract.WearPlayerState;
import com.vlabs.wearcontract.WearStation;
import com.vlabs.wearmanagers.connection.ConnectionManager;

import java.util.ArrayList;

import javax.inject.Inject;

import de.greenrobot.event.EventBus;

public class ShowMoreActivity extends BaseActivity {

    @Inject
    EventBus mEventBus;

    @Inject
    ConnectionManager mConnectionManager;

    @Inject
    PlayerManager mPlayerManager;

    private ImageByDataPathView mBackground;


    @Override
    protected void onCreate(@Nullable final Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        getComponent().inject(this);

        setContentView(R.layout.show_more_activity);

        mBackground = (ImageByDataPathView) findViewById(R.id.background);
    }

    @Override
    protected void onStart() {
        super.onStart();
        mEventBus.register(this);
        processPlayerState(mPlayerManager.currentState());
    }

    @Override
    protected void onStop() {
        super.onStop();
        mEventBus.unregister(this);
    }

    @SuppressWarnings("unused")
    public void onEventMainThread(OnPlayerState event) {
        if (isFinishing()) return;

        processPlayerState(event.playerState());
    }

    private void processPlayerState(final WearPlayerState playerState) {
        if (playerState.isPlaying()) {
            setBackgroundImagePath(playerState.getImagePath());
        } else {
            switchToIsPausedController();
        }
    }

    private void switchToIsPausedController() {
        mConnectionManager.getDataItems(Data.PATH_STATIONS_RECENT, (path, map) -> {
            if (mPlayerManager.isPlaying()) return;

            if (map == null) return;

            final ArrayList<DataMap> stationMapLists = map.getDataMapArrayList(Data.KEY_STATIONS);

            if (stationMapLists.isEmpty()) {
                setDefaultBackgroundColor();
            } else {
                final WearStation wearStation = WearStation.fromDataMap(stationMapLists.get(0));
                setBackgroundImagePath(wearStation.getImagePath());
            }
        });
    }

    private void setBackgroundImagePath(final String imagePath) {
        mBackground.setImagePath(imagePath);
    }

    private void setDefaultBackgroundColor() {
        final int color = ContextCompat.getColor(this, R.color.notification_background);
        final ColorDrawable colorDrawable = new ColorDrawable(color);
        mBackground.setImageDrawable(colorDrawable);
    }
}
