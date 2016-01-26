package com.vlabs.androiweartest.activity.launch;

import android.os.Bundle;
import android.support.wearable.view.DotsPageIndicator;
import android.support.wearable.view.GridViewPager;

import com.clearchannel.iheartradio.controller.view.ImageByDataPathView;
import com.vlabs.androiweartest.R;
import com.vlabs.androiweartest.activity.BaseActivity;
import com.vlabs.androiweartest.behavior.ChangeBackgroundBehavior;
import com.vlabs.androiweartest.manager.ConnectionManager;

import javax.inject.Inject;

import de.greenrobot.event.EventBus;

public class WearMainActivity extends BaseActivity {

    @Inject
    EventBus eventBus;

    @Inject
    ConnectionManager connectionManager;

    private ChangeBackgroundBehavior mBackgroundBehavior;
    private ImageByDataPathView mBackgroundImage;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_display);

        getComponent().inject(this);

        GridViewPager pager = (GridViewPager) findViewById(R.id.gridPager);
        WearGridPagerAdapter adapter = new WearGridPagerAdapter(getFragmentManager());
        pager.setAdapter(adapter);

        DotsPageIndicator dotsIndicator = (DotsPageIndicator)findViewById(R.id.page_indicator);
        dotsIndicator.setPager(pager);

        final ImageByDataPathView background = (ImageByDataPathView) findViewById(R.id.background);
        background.setImagePath("/image1234");

//        mBackgroundImage = (ImageByDataPathView) findViewById(R.id.background);
//        mBackgroundBehavior = new ChangeBackgroundBehavior(eventBus, connectionManager);
    }

    @Override
    protected void onStart() {
        super.onStart();
//        mBackgroundBehavior.activate(mBackgroundImage);
    }

    @Override
    protected void onStop() {
        super.onStop();
  //      mBackgroundBehavior.deactivate();
    }
}
