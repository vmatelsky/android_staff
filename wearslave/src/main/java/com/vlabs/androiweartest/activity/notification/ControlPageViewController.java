package com.vlabs.androiweartest.activity.notification;

import android.view.View;
import android.widget.ImageButton;

import com.vlabs.androiweartest.WearApplication;
import com.vlabs.androiweartest.helpers.analytics.Analytics;
import com.vlabs.androiweartest.models.PlayerManager;
import com.vlabs.androiweartest.models.PlayerStateModel;

public class ControlPageViewController {
    protected PlayerManager mPlayerManager;
    protected Analytics mAnalytics;
//    protected StationListModel mStationListModel;
//    protected PlayerStateModel mPlayerStateModel;

    private ImageButton mVolumeDownButton;
    private ImageButton mVolUpButton;
    private ImageButton mThumbUpButton;
    private ImageButton mThumbDownButton;
    private View mContainerControls;
    private View mContainerPlayStation;
//    private PlayStationPresenter mPlayStationPresenter;
    private View backgroundTint;

    public ControlPageViewController(WearApplication application, View rootView) {
        init(application, rootView);
    }

    void init(WearApplication application, View rootView) {
//        mPlayerManager = application.playerManager();
//        WearConnectionManager connectionManager = application.connectionManager();
//        mAnalytics = new Analytics(connectionManager);
//
//        mStationListModel = new StationListModel(WearApplication.instance().connectionManager(), Data.PATH_STATIONS_RECENT);
//        mPlayerStateModel = new PlayerStateModel(connectionManager, mPlayerManager);
//        initView(rootView);
    }

//    private void initView(View view) {
//        mContainerControls = view.findViewById(R.id.control_container_controls);
////        mContainerPlayStation = view.findViewById(R.id.control_container_station);
//        mVolUpButton = (ImageButton) view.findViewById(R.id.control_vol_up);
//        mVolumeDownButton = (ImageButton) view.findViewById(R.id.control_vol_down);
//        mThumbUpButton = (ImageButton) view.findViewById(R.id.control_thumb_up);
//        mThumbDownButton = (ImageButton) view.findViewById(R.id.control_thumb_down);
////        mVolUpButton.setOnClickListener(onControlButtonClicked);
////        mVolumeDownButton.setOnClickListener(onControlButtonClicked);
////        mThumbUpButton.setOnClickListener(onControlButtonClicked);
////        mThumbDownButton.setOnClickListener(onControlButtonClicked);
//        backgroundTint = view.findViewById(R.id.background_tint);
//        String title = view.getContext().getString(R.string.recently_played_title);
////        mPlayStationPresenter = new PlayStationPresenter(title, mPlayerManager, new UiChangeExecutor() {
////            @Override
////            public void execute(Runnable runnable) {
////                runnable.run();
////            }
////        }, mAnalytics);
////        TextView stationButton = (TextView) mContainerPlayStation.findViewById(R.id.station_name_button);
////        TextView titleView = (TextView) mContainerPlayStation.findViewById(R.id.title);
////        mPlayStationPresenter.bind(mStationListModel, stationButton, titleView);
////
////        mPlayerStateModel.addPlayerStateChangeListener(new Receiver<WearPlayerState>() {
////            @Override
////            public void receive(WearPlayerState wearPlayerState) {
////                updatePlayerState(wearPlayerState);
////            }
////        });
////        updatePlayerState(mPlayerStateModel.getState());
//    }


//    private void updatePlayerState(WearPlayerState wearPlayerState) {
//        if (wearPlayerState.isPlaying()) {
//            handleIsPlayingState(wearPlayerState);
//        } else {
//            handleNotPlayingState();
//        }
//
//        mPlayStationPresenter.refresh();
//    }
//
//    public void onStart() {
//        mStationListModel.startListening();
//        mPlayerStateModel.startListening();
//        updatePlayerState(mPlayerStateModel.getState());
//    }
//
//    public void onStop() {
//        mStationListModel.stopListening();
//        mPlayerStateModel.stopListening();
//    }
//
//    private final View.OnClickListener onControlButtonClicked= new View.OnClickListener() {
//        @Override
//        public void onClick(final View v) {
//            String command = null;
//            if(v == mVolUpButton) {
//                command = Message.CONTROL_ACTION_VOLUME_UP;
//                tagAction(WearAnalyticsConstants.WearPlayerAction.VOLUME_UP);
//            }
//            else if (v == mVolumeDownButton) {
//                command = Message.CONTROL_ACTION_VOLUME_DOWN;
//                tagAction(WearAnalyticsConstants.WearPlayerAction.VOLUME_DOWN);
//            }
//            else if (v == mThumbUpButton) {
//                command = Message.CONTROL_ACTION_THUMB_UP;
//                mThumbUpButton.setSelected(true);
//                tagAction(WearAnalyticsConstants.WearPlayerAction.THUMB_UP);
//            }
//            else if (v == mThumbDownButton) {
//                command = Message.CONTROL_ACTION_THUMB_DOWN;
//                mThumbDownButton.setSelected(true);
//                tagAction(WearAnalyticsConstants.WearPlayerAction.THUMB_DOWN);
//            }
//            if (command == null && BuildConfig.DEBUG) throw new RuntimeException("No command set, unhandled button?" + v);
//            WearApplication.instance().playerManager().sendControlCommand(command);
//        }
//    };
//
//    private void handleIsPlayingState(WearPlayerState state) {
//        // Update thumbs state
//        setContainerVisibility(mContainerControls, mContainerPlayStation);
//        fadeIn(backgroundTint);
//        if (state.isThumbsEnabled()) {
//            mThumbDownButton.setEnabled(true);
//            mThumbDownButton.setSelected(state.isThumbedDown());
//            mThumbUpButton.setEnabled(true);
//            mThumbUpButton.setSelected(state.isThumbedUp());
//        }
//        else {
//            mThumbDownButton.setSelected(false);
//            mThumbDownButton.setEnabled(false);
//            mThumbUpButton.setSelected(false);
//            mThumbUpButton.setEnabled(false);
//        }
//
//        if (state.getDeviceVolume() == 0) {
//            mVolumeDownButton.setImageResource(R.drawable.ic_controls_volume_mute);
//        } else {
//            mVolumeDownButton.setImageResource(R.drawable.ic_controls_volume_down);
//        }
//    }
//
//    private void handleNotPlayingState() {
//        setContainerVisibility(mContainerPlayStation, mContainerControls);
//        fadeOut(backgroundTint);
//    }
//
//    private void setContainerVisibility(View visible, View hidden) {
//        fadeIn(visible);
//        fadeOut(hidden);
//        visible.setVisibility(View.VISIBLE);
//        hidden.setVisibility(View.GONE);
//    }
//
//    private void fadeOut(View hidden) {
//        ObjectAnimator.ofFloat(hidden, "alpha", hidden.getAlpha(), 0.0f).start();
//    }
//
//    private void fadeIn(View visible) {
//        ObjectAnimator.ofFloat(visible, "alpha", visible.getAlpha(), 1.0f).start();
//    }
//
//    void tagAction(WearAnalyticsConstants.WearPlayerAction action) {
//        mAnalytics.broadcastRemoteAction(action);
//    }


}