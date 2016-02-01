package com.vlabs.androiweartest.activity.notification;

import android.os.Bundle;
import android.transition.Fade;
import android.transition.Scene;
import android.transition.TransitionManager;
import android.widget.FrameLayout;

import com.clearchannel.iheartradio.controller.view.ImageByDataPathView;
import com.google.android.gms.wearable.DataMap;
import com.vlabs.androiweartest.R;
import com.vlabs.androiweartest.activity.BaseActivity;
import com.vlabs.androiweartest.activity.notification.scenes.IsPausedSceneController;
import com.vlabs.androiweartest.activity.notification.scenes.IsPlayingSceneController;
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

    public static boolean isNotificationActivityActive = false;

    @Inject
    PlayerManager playerManager;

    @Inject
    Analytics mAnalytics;

    @Inject
    ConnectionManager mConnectionManager;

    @Inject
    EventBus eventBus;

    private FrameLayout mContentRoot;

    private ImageByDataPathView mBackground;

    @Inject
    ChangeBackgroundBehavior mBackgroundBehavior;

    @Override
    public void onCreate(Bundle savedInstance) {
        super.onCreate(savedInstance);
        setContentView(R.layout.controls_activity);

        getComponent().inject(this);

        mBackground = (ImageByDataPathView) findViewById(R.id.background);
        mContentRoot = (FrameLayout) findViewById(R.id.content_root);
    }

    @Override
    protected void onStart() {
        super.onStart();
        isNotificationActivityActive = true;

        eventBus.register(this);
        mBackgroundBehavior.activateFor(mBackground);
        processPlayerState(playerManager.currentState());

    }

    @Override
    protected void onStop() {
        super.onStop();
        isNotificationActivityActive = false;

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
        final Scene isPlayingScene = Scene.getSceneForLayout(mContentRoot, R.layout.is_playing_scene, this);
        final IsPlayingSceneController controller = new IsPlayingSceneController(mAnalytics, playerManager);

        isPlayingScene.setEnterAction(() -> controller.onEnter(isPlayingScene, state));
        TransitionManager.go(isPlayingScene, new Fade());
    }

    private void switchToIsPausedController() {
        mConnectionManager.getDataItems(WearDataEvent.PATH_STATIONS_RECENT, (path, map) -> {
            if (playerManager.isPlaying()) return;

            if (map == null) return;

            final ArrayList<DataMap> stationMapLists = map.getDataMapArrayList(WearDataEvent.KEY_STATIONS);

            final Scene isPausedScene = Scene.getSceneForLayout(mContentRoot, R.layout.is_paused_scene, this);
            final IsPausedSceneController controller = new IsPausedSceneController(mAnalytics, playerManager);

            final WearStation wearStation;

            if (stationMapLists.isEmpty()) {
                wearStation = null;
            } else {
                wearStation = WearStation.fromDataMap(stationMapLists.get(0));
            }

            isPausedScene.setEnterAction(() -> controller.onEnter(isPausedScene, wearStation));
            TransitionManager.go(isPausedScene, new Fade());
        });
    }

}
