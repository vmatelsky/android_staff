package com.vlabs.androiweartest.wear;

import android.content.Context;

import com.google.android.gms.wearable.DataEventBuffer;
import com.google.android.gms.wearable.MessageEvent;
import com.vlabs.androiweartest.integration.InPort;
import com.vlabs.androiweartest.integration.OutPort;
import com.vlabs.androiweartest.oughter.OuterImage;
import com.vlabs.androiweartest.oughter.OuterStation;
import com.vlabs.androiweartest.wear.connection.ConnectionManager;
import com.vlabs.androiweartest.wear.di.WearScopeModule;
import com.vlabs.androiweartest.wear.handlers.IncomingHandler;
import com.vlabs.androiweartest.wear.handlers.message.LoadImageHandler;
import com.vlabs.androiweartest.wear.handlers.message.PlayMessageHandler;
import com.vlabs.androiweartest.wear.managers.WearPlayerManager;
import com.vlabs.androiweartest.wear.model.ImageLoadedModel;
import com.vlabs.androiweartest.wear.model.RecentlyPlayedModel;
import com.vlabs.androiweartest.wear.model.StationsModel;

import java.util.List;

import javax.inject.Inject;

import dagger.ObjectGraph;

public class WearFacade {

    @Inject
    IncomingHandler mIncomingHandler;

    @Inject
    WearPlayerManager mWearPlayerManager;

    @Inject
    ConnectionManager mConnectionManager;

    private final ObjectGraph mObjectGraph;

    private final OutPort.StationPlayedPort mStationPlayedPort;
    private final OutPort.LoadImagePort mLoadImagePort;

    private final StationsModel.ForYouModel<List<OuterStation>> mForYouModel;
    private final StationsModel.MyStationsModel<List<OuterStation>> mMyStationsModel;
    private final RecentlyPlayedModel<OuterStation> mRecentlyPlayedModel;
    private final ImageLoadedModel mImageLoadedModel;

    public WearFacade(
            InPort.ForYouPin<List<OuterStation>> forYouPin,
            InPort.MyStationsPin<List<OuterStation>> myStationsPin,
            InPort.RecentlyPlayedPin<OuterStation> recentlyPlayedPin,
            InPort.ImageLoadedPin<OuterImage> imageLoadedPin,
            final Context context) {
        mObjectGraph = ObjectGraph.create(new WearScopeModule(context));
        mObjectGraph.inject(this);

        mForYouModel = new StationsModel.ForYouModel<>(forYouPin, mConnectionManager);
        mMyStationsModel = new StationsModel.MyStationsModel<>(myStationsPin, mConnectionManager);
        mRecentlyPlayedModel = new RecentlyPlayedModel<>(recentlyPlayedPin, mConnectionManager);
        mImageLoadedModel = new ImageLoadedModel<>(imageLoadedPin, mConnectionManager);

        final PlayMessageHandler playMessageHandler = new PlayMessageHandler();
        mStationPlayedPort = new OutPort.StationPlayedPort(playMessageHandler.onChanged());

        final LoadImageHandler loadImageHandler = new LoadImageHandler(context, mConnectionManager);
        mLoadImagePort = new OutPort.LoadImagePort(loadImageHandler.onLoadImage());
    }

    public void handleMessageEvent(final MessageEvent messageEvent) {
        mIncomingHandler.handleMessageEvent(messageEvent);
    }

    public void handleDataEvents(final DataEventBuffer dataEvents) {
        mIncomingHandler.handleDataEvents(dataEvents);
    }

    public OutPort.StationPlayedPort stationPlayedPort() {
        return mStationPlayedPort;
    }

    public OutPort.LoadImagePort loadImagePort() {
        return mLoadImagePort;
    }

    public WearPlayerManager playerManager() {
        return mWearPlayerManager;
    }

    public ConnectionManager connectionManager() {
        return mConnectionManager;
    }

}
