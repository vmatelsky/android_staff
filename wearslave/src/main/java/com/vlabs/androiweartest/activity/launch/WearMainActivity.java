package com.vlabs.androiweartest.activity.launch;

import android.app.Activity;
import android.os.Bundle;
import android.support.wearable.view.DotsPageIndicator;
import android.support.wearable.view.GridViewPager;
import android.widget.ImageView;

import com.vlabs.androiweartest.R;
import com.vlabs.androiweartest.helpers.behavior.FillWithImageFromRemoteBehavior;


public class WearMainActivity extends Activity {

    private FillWithImageFromRemoteBehavior mBehavior;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_display);

        GridViewPager pager = (GridViewPager) findViewById(R.id.gridPager);
        WearGridPagerAdapter adapter = new WearGridPagerAdapter(getFragmentManager());
        pager.setAdapter(adapter);

        DotsPageIndicator dotsIndicator = (DotsPageIndicator)findViewById(R.id.page_indicator);
        dotsIndicator.setPager(pager);


        final ImageView background = (ImageView) findViewById(R.id.background);
        mBehavior = new FillWithImageFromRemoteBehavior(background, "/image1234");
    }

    @Override
    protected void onStart() {
        super.onStart();
        mBehavior.startListening();
    }

    @Override
    protected void onStop() {
        super.onStop();
        mBehavior.stopListening();
    }
}
