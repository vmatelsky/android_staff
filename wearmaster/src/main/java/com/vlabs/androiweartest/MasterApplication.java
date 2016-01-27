package com.vlabs.androiweartest;

import android.app.Application;

import com.vlabs.androiweartest.wear.WearFacade;

public class MasterApplication extends Application {

    private static MasterApplication sInstance;

    private WearFacade mFacade;

    public static MasterApplication instance() {
        return sInstance;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        sInstance = this;

        mFacade = new WearFacade(this);
    }

    public WearFacade wearFacade() {
        return mFacade;
    }



}
