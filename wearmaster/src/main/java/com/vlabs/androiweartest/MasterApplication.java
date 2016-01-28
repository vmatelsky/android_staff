package com.vlabs.androiweartest;

import android.app.Application;

import com.vlabs.androiweartest.oughter.OuterImage;
import com.vlabs.androiweartest.oughter.OuterPlayerManager;
import com.vlabs.androiweartest.oughter.OuterStation;
import com.vlabs.androiweartest.wear.WearFacade;

import java.util.ArrayList;

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
                                 mIntegrationModule.mySearchStationsPin,
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

        facade.searchStationPort().onChanged().subscribe(searchMessage -> {
            // TODO: perform real search
            final ArrayList<OuterStation> response = new ArrayList<>();
            response.add(new OuterStation(1));
            mIntegrationModule.mySearchStationsPin.call(response);
        });

        facade.analyticsPort().onChanged().subscribe(analyticsMessage -> {
            // TODO: tag analytics
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
