package com.vlabs.androiweartest.activity;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.wearable.activity.WearableActivity;

import com.vlabs.androiweartest.WearApplication;
import com.vlabs.androiweartest.di.component.ActivityComponent;
import com.vlabs.androiweartest.di.component.DaggerActivityComponent;

public class BaseActivity extends WearableActivity {

    private ActivityComponent mComponent;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mComponent = DaggerActivityComponent.builder()
                .appComponent(app().appComponent()).build();
    }

    protected WearApplication app() {
        return (WearApplication) getApplicationContext();
    }

    public ActivityComponent getComponent() {
        return mComponent;
    }

}
