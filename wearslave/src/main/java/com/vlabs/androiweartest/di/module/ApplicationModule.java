package com.vlabs.androiweartest.di.module;

import android.content.Context;

import com.vlabs.androiweartest.WearApplication;
import com.vlabs.androiweartest.helpers.analytics.Analytics;
import com.vlabs.androiweartest.images.ImageLoader;
import com.vlabs.androiweartest.images.ImageManager;
import com.vlabs.androiweartest.models.PlayerManager;
import com.vlabs.wearmanagers.connection.ConnectionManager;
import com.vlabs.wearmanagers.connection.ConnectionManagerImpl;
import com.vlabs.wearmanagers.message.MessageManager;
import com.vlabs.wearmanagers.message.MessageManagerImpl;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;
import de.greenrobot.event.EventBus;

@Module
public class ApplicationModule {
    private final WearApplication mApp;

    public ApplicationModule(final WearApplication app) {
        mApp = app;
    }

    @Provides
    @Singleton
    public Context appContext() {
        return mApp;
    }

    @Provides
    @Singleton
    public EventBus eventBus() {
        return new EventBus();
    }

    @Provides
    @Singleton
    public MessageManager messageManager() {
        return new MessageManagerImpl();
    }

    @Provides
    @Singleton
    public ConnectionManager connectionManager() {
        return new ConnectionManagerImpl(mApp);
    }

    @Provides
    @Singleton
    public Analytics analytics() {
        return new Analytics(connectionManager());
    }

    @Provides
    @Singleton
    public PlayerManager playerManager() {
        return new PlayerManager(connectionManager());
    }

    @Provides
    @Singleton
    public ImageManager imageManager() {
        return new ImageManager(new ImageLoader(mApp.appComponent()));
    }

}
