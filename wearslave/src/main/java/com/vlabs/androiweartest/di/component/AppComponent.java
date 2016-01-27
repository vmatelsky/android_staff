package com.vlabs.androiweartest.di.component;

import android.content.Context;

import com.clearchannel.iheartradio.controller.view.ImageByDataPathView;
import com.vlabs.androiweartest.WearListenerService;
import com.vlabs.androiweartest.behavior.ChangeBackgroundBehavior;
import com.vlabs.androiweartest.di.module.ApplicationModule;
import com.vlabs.androiweartest.helpers.analytics.Analytics;
import com.vlabs.androiweartest.images.ImageManager;
import com.vlabs.androiweartest.manager.ConnectionManager;
import com.vlabs.androiweartest.models.PlayerManager;

import javax.inject.Singleton;

import dagger.Component;
import de.greenrobot.event.EventBus;

@Singleton
@Component(modules = {ApplicationModule.class})
public interface AppComponent {

    Context appContext();

    EventBus eventBus();

    ConnectionManager connectionManager();

    Analytics analytics();

    PlayerManager playerManager();

    ImageManager imageManager();

    ChangeBackgroundBehavior backgroundBehavior();

    void inject(WearListenerService wearListenerService);

    void inject(ImageByDataPathView imageByDataPathView);
}
