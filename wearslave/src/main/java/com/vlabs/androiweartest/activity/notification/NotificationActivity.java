package com.vlabs.androiweartest.activity.notification;

import android.os.Bundle;
import android.support.v4.app.FragmentActivity;

import com.vlabs.androiweartest.R;
import com.vlabs.androiweartest.WearApplication;

public class NotificationActivity extends FragmentActivity {
//    PlayerStateBackgroundPresenter mPlayerStateBackgroundPresenter;
    ControlPageViewController mControlPageView;

    @Override
    public void onCreate(Bundle savedInstance) {
        super.onCreate(savedInstance);
        setContentView(R.layout.controls_activity);

        mControlPageView = new ControlPageViewController(WearApplication.instance(), findViewById(R.id.root));

//        StationListModel stationListModel = new StationListModel(WearApplication.instance().connectionManager(), Data.PATH_STATIONS_RECENT);
//        PlayerStateModel playerStateModel = new PlayerStateModel(WearApplication.instance().connectionManager(), WearApplication.instance().playerManager());
//        mPlayerStateBackgroundPresenter = new PlayerStateBackgroundPresenter(
//                stationListModel,
//                playerStateModel,
//                new UiChangeExecutor() {
//                    @Override
//                    public void execute(Runnable runnable) {
//                        if (runnable == null || isFinishing()) return;
//                        runnable.run();
//                    }
//                });
//
//        mPlayerStateBackgroundPresenter.bind((DataPathAwareImageView) findViewById(R.id.background));
//        final int pageTotal = getIntent().getIntExtra(WearExtras.EXTRA_PAGE_TOTAL, -1);
//        final int pageIndex = getIntent().getIntExtra(WearExtras.EXTRA_PAGE_INDEX, -1);
//        final ViewGroup indicatorContainer = (ViewGroup)findViewById(R.id.page_indicator_container);
//        new PageIndicator(indicatorContainer, pageTotal, pageIndex);
    }


    @Override
    public void onResume() {
        super.onResume();
//        mPlayerStateBackgroundPresenter.onResume();
//        mControlPageView.onStart();
    }

    @Override
    public void onPause() {
        super.onPause();
//        mPlayerStateBackgroundPresenter.onPause();
//        mControlPageView.onStop();
    }

}
