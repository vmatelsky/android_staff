package com.vlabs.androiweartest.wear;

import android.content.Context;

import com.google.android.gms.common.data.FreezableUtils;
import com.google.android.gms.wearable.DataEvent;
import com.google.android.gms.wearable.DataEventBuffer;
import com.google.android.gms.wearable.MessageEvent;
import com.vlabs.androiweartest.integration.InPort;
import com.vlabs.androiweartest.integration.OutPort;
import com.vlabs.androiweartest.oughter.OuterImage;
import com.vlabs.androiweartest.oughter.OuterPlayerState;
import com.vlabs.androiweartest.oughter.OuterStation;
import com.vlabs.androiweartest.wear.connection.ConnectionManager;
import com.vlabs.androiweartest.wear.di.WearScopeModule;
import com.vlabs.androiweartest.wear.handlers.data.DataHandler;
import com.vlabs.androiweartest.wear.handlers.message.AnalyticsMessageHandler;
import com.vlabs.androiweartest.wear.handlers.message.LoadImageHandler;
import com.vlabs.androiweartest.wear.handlers.message.MessageHandler;
import com.vlabs.androiweartest.wear.handlers.message.PlayMessageHandler;
import com.vlabs.androiweartest.wear.handlers.message.SearchMessageHandler;
import com.vlabs.androiweartest.wear.model.FeedbackModel;
import com.vlabs.androiweartest.wear.model.ImageLoadedModel;
import com.vlabs.androiweartest.wear.model.PlayerStateChangedModel;
import com.vlabs.androiweartest.wear.model.RecentlyPlayedModel;
import com.vlabs.androiweartest.wear.model.StationsModel;
import com.vlabs.wearcontract.WearMessage;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.inject.Inject;

import dagger.ObjectGraph;

public class WearFacade {

    private final Map<String, MessageHandler> mMessageHandlers = new HashMap<>();
    private final Map<String, DataHandler> mDataHandlers = new HashMap<>();

    @Inject
    ConnectionManager mConnectionManager;

    private final ObjectGraph mObjectGraph;

    private final OutPort.StationPlayedPort mStationPlayedPort;
    private final OutPort.LoadImagePort mLoadImagePort;
    private OutPort.SearchStationPort mSearchStationPort;

    private final StationsModel.ForYouModel<List<OuterStation>> mForYouModel;
    private final StationsModel.MyStationsModel<List<OuterStation>> mMyStationsModel;
    private final StationsModel.SearchStationsModel<List<OuterStation>> mSearchStationsModel;
    private final RecentlyPlayedModel<OuterStation> mRecentlyPlayedModel;
    private final ImageLoadedModel mImageLoadedModel;
    private final FeedbackModel mFeedbackModel;
    private final PlayerStateChangedModel mPlayerStateChangedModel;

    public WearFacade(
            final InPort.ForYouPin<List<OuterStation>> forYouPin,
            final InPort.MyStationsPin<List<OuterStation>> myStationsPin,
            final InPort.SearchStationsPin<List<OuterStation>> searchStationsPin,
            final InPort.RecentlyPlayedPin<OuterStation> recentlyPlayedPin,
            final InPort.ImageLoadedPin<OuterImage> imageLoadedPin,
            final InPort.FeedbackPin feedbackPin,
            final InPort.PlayerStateChangedPin<OuterPlayerState> playerStateChangedPin,
            final Context context) {
        mObjectGraph = ObjectGraph.create(new WearScopeModule(context));
        mObjectGraph.inject(this);

        mForYouModel = new StationsModel.ForYouModel<>(forYouPin, mConnectionManager);
        mMyStationsModel = new StationsModel.MyStationsModel<>(myStationsPin, mConnectionManager);
        mSearchStationsModel = new StationsModel.SearchStationsModel<>(searchStationsPin, mConnectionManager);
        mRecentlyPlayedModel = new RecentlyPlayedModel<>(recentlyPlayedPin, mConnectionManager);
        mImageLoadedModel = new ImageLoadedModel<>(imageLoadedPin, mConnectionManager);
        mFeedbackModel = new FeedbackModel(feedbackPin, mConnectionManager);
        mPlayerStateChangedModel = new PlayerStateChangedModel(playerStateChangedPin, mConnectionManager);

        final PlayMessageHandler playMessageHandler = new PlayMessageHandler();
        mStationPlayedPort = new OutPort.StationPlayedPort(playMessageHandler.onChanged());

        final LoadImageHandler loadImageHandler = new LoadImageHandler();
        mLoadImagePort = new OutPort.LoadImagePort(loadImageHandler.onLoadImage());

        final SearchMessageHandler searchMessageHandler = new SearchMessageHandler();
        mSearchStationPort = new OutPort.SearchStationPort(searchMessageHandler.onSearchTermChanged());

        mMessageHandlers.put(WearMessage.SEARCH.path(), searchMessageHandler);
        mMessageHandlers.put(WearMessage.LOAD_IMAGE.path(), loadImageHandler);
        mMessageHandlers.put(WearMessage.ANALYTICS.path(), new AnalyticsMessageHandler());
        mMessageHandlers.put(WearMessage.PLAY_STATION.path(), playMessageHandler);
    }

    public void handleMessageEvent(final MessageEvent messageEvent) {
        final MessageHandler handler = mMessageHandlers.get(messageEvent.getPath());
        if (handler == null) {
//            throw new RuntimeException("no handler for message: " + messageEvent.getPath());
        } else {
            handler.handle(messageEvent);
        }
    }

    public void handleDataEvents(final DataEventBuffer dataEvents) {
        final List<DataEvent> events = FreezableUtils.freezeIterable(dataEvents);

        for (DataEvent event : events) {
            final String path = event.getDataItem().getUri().getPath();

            final DataHandler handler = mDataHandlers.get(path);
            if (handler == null) {
//                throw new RuntimeException("no handler for message: " + path);
            } else {
                handler.handle(event);
            }
        }
    }

    public OutPort.StationPlayedPort stationPlayedPort() {
        return mStationPlayedPort;
    }

    public OutPort.LoadImagePort loadImagePort() {
        return mLoadImagePort;
    }

    public OutPort.SearchStationPort searchStationPort() {
        return mSearchStationPort;
    }

    public ConnectionManager connectionManager() {
        return mConnectionManager;
    }

}
