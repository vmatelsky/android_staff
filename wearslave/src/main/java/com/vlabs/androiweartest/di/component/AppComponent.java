package com.vlabs.androiweartest.di.component;

import android.content.Context;

import com.clearchannel.iheartradio.controller.view.ImageByDataPathView;
import com.path.android.jobqueue.JobManager;
import com.vlabs.androiweartest.WearListenerService;
import com.vlabs.androiweartest.behavior.ChangeBackgroundBehavior;
import com.vlabs.androiweartest.di.module.ApplicationModule;
import com.vlabs.androiweartest.helpers.analytics.Analytics;
import com.vlabs.androiweartest.images.ImageManager;
import com.vlabs.androiweartest.job.base.BaseJob;
import com.vlabs.androiweartest.job.connectivity.ConnectivityModule;
import com.vlabs.androiweartest.manager.PlayerManager;

import javax.inject.Singleton;

import dagger.Component;
import de.greenrobot.event.EventBus;

@Singleton
@Component(modules = {ApplicationModule.class})
public interface AppComponent {

    Context appContext();

    EventBus eventBus();

    Analytics analytics();

    PlayerManager playerManager();

    ImageManager imageManager();

    ChangeBackgroundBehavior backgroundBehavior();

    JobManager jobManager();

    ConnectivityModule connectivityModule();

    void inject(WearListenerService wearListenerService);

    void inject(ImageByDataPathView imageByDataPathView);

    void inject(BaseJob baseJob);
}
