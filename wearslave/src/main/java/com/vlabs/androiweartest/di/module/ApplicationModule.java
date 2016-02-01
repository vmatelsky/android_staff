package com.vlabs.androiweartest.di.module;

import android.content.Context;

import com.path.android.jobqueue.JobManager;
import com.path.android.jobqueue.config.Configuration;
import com.vlabs.androiweartest.WearApplication;
import com.vlabs.androiweartest.behavior.ChangeBackgroundBehavior;
import com.vlabs.androiweartest.helpers.analytics.Analytics;
import com.vlabs.androiweartest.images.ImageLoader;
import com.vlabs.androiweartest.images.ImageManager;
import com.vlabs.androiweartest.job.base.BaseJob;
import com.vlabs.androiweartest.job.connectivity.ConnectivityModule;
import com.vlabs.androiweartest.manager.PlayerManager;

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
    private ConnectivityModule mConnectivityModule;
    private JobManager mJobManager;

    public ApplicationModule(final WearApplication app) {
        mApp = app;
        mConnectivityModule = new ConnectivityModule(mApp);
        mJobManager = new JobManager(mApp, jobManagerConfiguration());

        final ImageLoader imageLoader = new ImageLoader(mJobManager, mEventBus);
        mImageManager = new ImageManager(imageLoader, mEventBus);
        mPlayerManager = new PlayerManager(mJobManager, mEventBus);
        mBackgroundBehavior = new ChangeBackgroundBehavior(app, mEventBus, mJobManager);
    }

    private Configuration jobManagerConfiguration() {
        return new Configuration.Builder(mApp)
                    .maxConsumerCount(3)
                    .minConsumerCount(1)
                    .networkUtil(mConnectivityModule)
                    .injector(job -> {
                        if (job instanceof BaseJob) {
                            ((BaseJob) job).inject(mApp.appComponent());
                        }
                    })
                    .build();
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
    public Analytics analytics() {
        return new Analytics(mJobManager);
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

    @Provides
    @Singleton
    public ConnectivityModule connectivityModule() {
        return mConnectivityModule;
    }

    @Provides
    @Singleton
    public JobManager jobManager() {
        return mJobManager;
    }

}
