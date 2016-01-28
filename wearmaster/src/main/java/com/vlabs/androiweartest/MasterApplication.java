package com.vlabs.androiweartest;

import android.app.Application;

import com.vlabs.androiweartest.oughter.OuterImage;
import com.vlabs.androiweartest.wear.WearFacade;
import com.vlabs.androiweartest.oughter.OuterPlayerManager;

public class MasterApplication extends Application {

    private static MasterApplication sInstance;

    private WearFacade mFacade;
    private MasterIntegrationModule mIntegrationModule;

    private OuterPlayerManager mOuterPlayerManager;

    public static MasterApplication instance() {
        return sInstance;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        sInstance = this;

        mIntegrationModule = new MasterIntegrationModule();

        mOuterPlayerManager = new OuterPlayerManager(this);

        mFacade = new WearFacade(
                                 mIntegrationModule.forYouPin,
                                 mIntegrationModule.myStationsPin,
                                 mIntegrationModule.recentlyPlayedPin,
                                 mIntegrationModule.imageLoadedPin,
                                 mIntegrationModule.feedbackPin,
                                 mIntegrationModule.playerStateChangedPin,
                                 this);

        initFacade(mFacade);

    }

    private void initFacade(final WearFacade facade) {
        facade.loadImagePort().onChanged().subscribe(loadImageMessage -> {
            mIntegrationModule.imageLoadedPin.call(new OuterImage(loadImageMessage));
        });

        facade.stationPlayedPort().onChanged().subscribe(station -> {
            mOuterPlayerManager.play(station);
        });
    }

    public WearFacade wearFacade() {
        return mFacade;
    }

    public MasterIntegrationModule integrationModule() {
        return mIntegrationModule;
    }

    public OuterPlayerManager playerManager() {
        return mOuterPlayerManager;
    }

}
