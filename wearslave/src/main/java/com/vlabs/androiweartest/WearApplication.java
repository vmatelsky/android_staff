package com.vlabs.androiweartest;

import android.app.Application;

import com.vlabs.androiweartest.helpers.analytics.Analytics;
import com.vlabs.wearmanagers.connection.ConnectionManager;
import com.vlabs.wearmanagers.connection.ConnectionManagerImpl;
import com.vlabs.wearmanagers.message.MessageManager;
import com.vlabs.wearmanagers.message.MessageManagerImpl;

public class WearApplication extends Application {

    private static WearApplication sInstance;

    private MessageManager mMessageManager;

    private ConnectionManager mConnectionManager;

    private Analytics mAnalytics;

    public static WearApplication instance() {
        return sInstance;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        sInstance = this;

        mMessageManager = new MessageManagerImpl();
        mConnectionManager = new ConnectionManagerImpl(this);
        mAnalytics = new Analytics(mConnectionManager);
    }

    public MessageManager messageManager() {
        return mMessageManager;
    }

    public ConnectionManager connectionManager() {
        return mConnectionManager;
    }

    public Analytics analytics() {
        return mAnalytics;
    }
}
