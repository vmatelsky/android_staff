package com.vlabs.androiweartest;

import android.app.Application;

import com.vlabs.wearmanagers.connection.ConnectionManager;
import com.vlabs.wearmanagers.connection.ConnectionManagerImpl;
import com.vlabs.wearmanagers.message.MessageManager;
import com.vlabs.wearmanagers.message.MessageManagerImpl;

public class MasterApplication extends Application {

    private static MasterApplication sInstance;

    private MessageManager mMessageManager;

    private ConnectionManager mConnectionManager;

    public static MasterApplication instance() {
        return sInstance;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        sInstance = this;

        mMessageManager = new MessageManagerImpl();
        mConnectionManager = new ConnectionManagerImpl(this);
    }

    public MessageManager messageManager() {
        return mMessageManager;
    }

    public ConnectionManager connectionManager() {
        return mConnectionManager;
    }

}
