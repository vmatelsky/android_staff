package com.vlabs.androiweartest.activity.notification;

import android.animation.ObjectAnimator;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.view.View;

import com.clearchannel.iheartradio.controller.view.ImageByDataPathView;
import com.google.android.gms.wearable.DataMap;
import com.vlabs.androiweartest.R;
import com.vlabs.androiweartest.activity.BaseActivity;
import com.vlabs.androiweartest.activity.notification.state.IsPausedViewController;
import com.vlabs.androiweartest.activity.notification.state.IsPlayingViewController;
import com.vlabs.androiweartest.behavior.ChangeBackgroundBehavior;
import com.vlabs.androiweartest.helpers.analytics.Analytics;
import com.vlabs.androiweartest.manager.ConnectionManager;
import com.vlabs.androiweartest.manager.PlayerManager;
import com.vlabs.wearcontract.WearDataEvent;
import com.vlabs.wearcontract.WearPlayerState;
import com.vlabs.wearcontract.WearStation;
import com.vlabs.wearcontract.messages.StateMessage;

import java.util.ArrayList;

import javax.inject.Inject;

import de.greenrobot.event.EventBus;


public class NotificationActivity extends BaseActivity {

    @Inject
    PlayerManager playerManager;

    @Inject
    Analytics mAnalytics;

    @Inject
    ConnectionManager mConnectionManager;

    @Inject
    EventBus eventBus;

    private ImageByDataPathView mBackground;
    private View backgroundTint;
    private IsPlayingViewController mIsPlayingController;
    private IsPausedViewController mIsPausedController;

    @Inject
    ChangeBackgroundBehavior mBackgroundBehavior;

    @Override
    public void onCreate(Bundle savedInstance) {
        super.onCreate(savedInstance);
        setContentView(R.layout.controls_activity);

        getComponent().inject(this);

        mBackground = (ImageByDataPathView) findViewById(R.id.background);
        backgroundTint = findViewById(R.id.background_tint);

        mIsPlayingController = new IsPlayingViewController(
                findViewById(R.id.is_playing_content),
                mAnalytics,
                playerManager
        );

        mIsPausedController = new IsPausedViewController(
                findViewById(R.id.is_paused_content),
                mAnalytics,
                playerManager
        );
    }

    @Override
    protected void onStart() {
        super.onStart();
        eventBus.register(this);
        mBackgroundBehavior.activateFor(mBackground);
        processPlayerState(playerManager.currentState());

    }

    @Override
    protected void onStop() {
        super.onStop();
        eventBus.unregister(this);
        mBackgroundBehavior.deactivateFor(mBackground);
    }

    @SuppressWarnings("unused")
    public void onEventMainThread(StateMessage event) {
        if (isFinishing()) return;

        processPlayerState(event.asPlayerState());
    }

    private void processPlayerState(final WearPlayerState playerState) {
        if (playerState.isPlaying()) {
            switchToIsPlayingController(playerState);
        } else {
            switchToIsPausedController();
        }
    }

    private void switchToIsPlayingController(final WearPlayerState state) {
        fadeIn(backgroundTint);

        mIsPlayingController.show(state);
        mIsPausedController.hide();
    }

    private void switchToIsPausedController() {
        fadeOut(backgroundTint);

        mIsPlayingController.hide();

        mConnectionManager.getDataItems(WearDataEvent.PATH_STATIONS_RECENT, (path, map) -> {
            if (playerManager.isPlaying()) return;

            if (map == null) return;

            final ArrayList<DataMap> stationMapLists = map.getDataMapArrayList(WearDataEvent.KEY_STATIONS);

            if (stationMapLists.isEmpty()) {
                setDefaultBackgroundColor();
                mIsPausedController.show(null);
            } else {
                final WearStation wearStation = WearStation.fromDataMap(stationMapLists.get(0));
                mIsPausedController.show(wearStation);
            }
        });
    }

    private void fadeOut(View hidden) {
        ObjectAnimator.ofFloat(hidden, View.ALPHA, hidden.getAlpha(), 0.0f).start();
    }

    private void fadeIn(View visible) {
        ObjectAnimator.ofFloat(visible, View.ALPHA, visible.getAlpha(), 1.0f).start();
    }

    private void setDefaultBackgroundColor() {
        final int color = ContextCompat.getColor(this, R.color.notification_background);
        final ColorDrawable colorDrawable = new ColorDrawable(color);
        mBackground.setImageDrawable(colorDrawable);
    }

}
