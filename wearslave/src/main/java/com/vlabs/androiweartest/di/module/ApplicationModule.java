package com.vlabs.androiweartest.di.module;

import android.content.Context;

import com.vlabs.androiweartest.WearApplication;
import com.vlabs.androiweartest.behavior.ChangeBackgroundBehavior;
import com.vlabs.androiweartest.helpers.analytics.Analytics;
import com.vlabs.androiweartest.images.ImageLoader;
import com.vlabs.androiweartest.images.ImageManager;
import com.vlabs.androiweartest.manager.ConnectionManager;
import com.vlabs.androiweartest.manager.ConnectionManagerImpl;
import com.vlabs.androiweartest.models.PlayerManager;
import com.vlabs.wearmanagers.message.MessageManager;
import com.vlabs.wearmanagers.message.MessageManagerImpl;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;
import de.greenrobot.event.EventBus;

@Module
public class ApplicationModule {
    private final WearApplication mApp;
    private final EventBus mEventBus = new EventBus();
    private final ImageManager mImageManager;
    private final PlayerManager mPlayerManager;
    private final ChangeBackgroundBehavior mBackgroundBehavior;

    public ApplicationModule(final WearApplication app) {
        mApp = app;
        final ImageLoader imageLoader = new ImageLoader(connectionManager(), mEventBus);
        mImageManager = new ImageManager(imageLoader, mEventBus);
        mPlayerManager = new PlayerManager(connectionManager(), mEventBus);
        mBackgroundBehavior = new ChangeBackgroundBehavior(app, mEventBus, connectionManager());
    }

    @Provides
    @Singleton
    public Context appContext() {
        return mApp;
    }

    @Provides
    @Singleton
    public EventBus eventBus() {
        return mEventBus;
    }

    @Provides
    @Singleton
    public MessageManager messageManager() {
        return new MessageManagerImpl();
    }

    @Provides
    @Singleton
    public ConnectionManager connectionManager() {
        return new ConnectionManagerImpl(mApp, eventBus());
    }

    @Provides
    @Singleton
    public Analytics analytics() {
        return new Analytics(connectionManager());
    }

    @Provides
    @Singleton
    public PlayerManager playerManager() {
        return mPlayerManager;
    }

    @Provides
    @Singleton
    public ImageManager imageManager() {
        return mImageManager;
    }

    @Provides
    @Singleton
    public ChangeBackgroundBehavior backgroundBehavior() {
        return mBackgroundBehavior;
    }

}
