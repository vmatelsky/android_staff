package com.vlabs.androiweartest;

import android.app.Application;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;

import com.google.android.gms.wearable.Asset;
import com.google.android.gms.wearable.PutDataMapRequest;
import com.google.android.gms.wearable.PutDataRequest;
import com.vlabs.androiweartest.oughter.OuterImage;
import com.vlabs.androiweartest.wear.WearFacade;
import com.vlabs.wearcontract.WearDataEvent;
import com.vlabs.wearcontract.dummy.DummyWearStation;
import com.vlabs.wearcontract.messages.LoadImageMessage;

import java.io.ByteArrayOutputStream;

import rx.functions.Action1;

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
                                 mIntegrationModule.imageLoadedPin,
                                 this);

        initFacade(mFacade);

    }

    private void initFacade(final WearFacade facade) {
        facade.loadImagePort().onChanged().subscribe(loadImageMessage -> {
            mIntegrationModule.imageLoadedPin.call(new OuterImage(loadImageMessage));
        });
    }

    public WearFacade wearFacade() {
        return mFacade;
    }

    public MasterIntegrationModule integrationModule() {
        return mIntegrationModule;
    }

}
