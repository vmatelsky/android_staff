package com.vlabs.androiweartest;

import android.app.Application;

import com.vlabs.androiweartest.di.component.AppComponent;
import com.vlabs.androiweartest.di.component.DaggerAppComponent;
import com.vlabs.androiweartest.di.module.ApplicationModule;

public class WearApplication extends Application {

    private AppComponent mAppComponent;

    private static WearApplication sInstance;

    public static WearApplication instance() {
        return sInstance;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        sInstance = this;

        mAppComponent = DaggerAppComponent.builder()
                .applicationModule(new ApplicationModule(this))
                .build();
    }

    public AppComponent appComponent() {
        return mAppComponent;
    }

}
