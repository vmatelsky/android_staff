package com.vlabs.androiweartest.wear.handlers;

import android.content.Context;

import com.google.android.gms.common.data.FreezableUtils;
import com.google.android.gms.wearable.DataEvent;
import com.google.android.gms.wearable.DataEventBuffer;
import com.google.android.gms.wearable.MessageEvent;
import com.vlabs.androiweartest.wear.connection.ConnectionManager;
import com.vlabs.androiweartest.wear.handlers.data.DataHandler;
import com.vlabs.androiweartest.wear.handlers.message.AnalyticsMessageHandler;
import com.vlabs.androiweartest.wear.handlers.message.LoadImageHandler;
import com.vlabs.androiweartest.wear.handlers.message.MessageHandler;
import com.vlabs.androiweartest.wear.handlers.message.PlayMessageHandler;
import com.vlabs.androiweartest.wear.handlers.message.SearchMessageHandler;
import com.vlabs.wearcontract.WearMessage;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class IncomingHandler {

    private final Map<String, MessageHandler> mMessageHandlers = new HashMap<>();
    private final Map<String, DataHandler> mDataHandlers = new HashMap<>();

    public IncomingHandler(final Context context, ConnectionManager connectionManager) {
        mMessageHandlers.put(WearMessage.SEARCH.path(), new SearchMessageHandler());
        mMessageHandlers.put(WearMessage.LOAD_IMAGE.path(), new LoadImageHandler());
        mMessageHandlers.put(WearMessage.ANALYTICS.path(), new AnalyticsMessageHandler());
        mMessageHandlers.put(WearMessage.PLAY_STATION.path(), new PlayMessageHandler());

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
}
