package com.vlabs.androiweartest.di.component;

import android.content.Context;

import com.clearchannel.iheartradio.controller.view.ImageByDataPathView;
import com.vlabs.androiweartest.WearListenerService;
import com.vlabs.androiweartest.di.module.ApplicationModule;
import com.vlabs.androiweartest.helpers.analytics.Analytics;
import com.vlabs.androiweartest.images.ImageLoader;
import com.vlabs.androiweartest.images.ImageManager;
import com.vlabs.androiweartest.models.PlayerManager;
import com.vlabs.androiweartest.models.StationListModel;
import com.vlabs.wearmanagers.connection.ConnectionManager;
import com.vlabs.wearmanagers.message.MessageManager;

import javax.inject.Singleton;

import dagger.Component;

@Singleton
@Component(modules = {ApplicationModule.class})
public interface AppComponent {

    Context appContext();

    MessageManager messageManager();

    ConnectionManager connectionManager();

    Analytics analytics();

    PlayerManager playerManager();

    ImageManager imageManager();

    void inject(WearListenerService wearListenerService);

    void inject(ImageLoader imageLoader);

    void inject(ImageByDataPathView imageByDataPathView);
}
