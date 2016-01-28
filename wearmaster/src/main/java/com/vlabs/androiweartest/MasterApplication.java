package com.vlabs.androiweartest;

import android.app.Application;

import com.vlabs.androiweartest.wear.WearFacade;

public class MasterApplication extends Application {

    private static MasterApplication sInstance;

    private WearFacade mFacade;
    private MasterIntegrationModule mIntegrationModule;

    public static MasterApplication instance() {
        return sInstance;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        sInstance = this;


        mIntegrationModule = new MasterIntegrationModule();

        mFacade = new WearFacade(
                                 mIntegrationModule.forYouPin,
                                 mIntegrationModule.myStationsPin,
                                 mIntegrationModule.recentlyPlayedPin,
                                 this);
    }

    public WearFacade wearFacade() {
        return mFacade;
    }

    public MasterIntegrationModule integrationModule() {
        return mIntegrationModule;
    }

}
