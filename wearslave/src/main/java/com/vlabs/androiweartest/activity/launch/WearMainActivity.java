package com.vlabs.androiweartest.activity.launch;

import android.app.Activity;
import android.os.Bundle;
import android.support.wearable.view.DotsPageIndicator;
import android.support.wearable.view.GridViewPager;

import com.clearchannel.iheartradio.controller.view.ImageByDataPathView;
import com.vlabs.androiweartest.R;

public class WearMainActivity extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_display);

        GridViewPager pager = (GridViewPager) findViewById(R.id.gridPager);
        WearGridPagerAdapter adapter = new WearGridPagerAdapter(getFragmentManager());
        pager.setAdapter(adapter);

        DotsPageIndicator dotsIndicator = (DotsPageIndicator)findViewById(R.id.page_indicator);
        dotsIndicator.setPager(pager);

        final ImageByDataPathView background = (ImageByDataPathView) findViewById(R.id.background);
        background.setImagePath("/image1234");
    }
}
