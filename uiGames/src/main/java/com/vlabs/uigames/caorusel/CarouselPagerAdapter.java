package com.vlabs.uigames.caorusel;

import android.app.Activity;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

public class CarouselPagerAdapter extends FragmentPagerAdapter {

    private final CarouselContentProvider mContentProvider;
    private final int mLoops;
    private final Activity mActivity;

    // You can choose a bigger number for LOOPS, but you know, nobody will fling
    // more than 10000 times just in order to test your "infinite" ViewPager
    public final static int LOOPS = 10000;

    public CarouselPagerAdapter(Activity activity, FragmentManager fm, CarouselContentProvider content) {
        this(activity, fm, content, LOOPS);
    }

    public CarouselPagerAdapter(Activity activity, FragmentManager fm, CarouselContentProvider provider, int loops) {
        super(fm);
        mActivity = activity;
        mContentProvider = provider;
        mLoops = loops;
    }

    @Override
    public Fragment getItem(int position) {
        position = position % mContentProvider.count();
        return mContentProvider.fragmentForPosition(mActivity, position);
    }

    @Override
    public int getCount() {
        return mContentProvider.count() * mLoops;
    }

    public int firstPage() {
        return mContentProvider.count() * mLoops / 2;
    }
}
