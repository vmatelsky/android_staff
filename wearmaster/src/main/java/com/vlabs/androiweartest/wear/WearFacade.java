package com.vlabs.androiweartest.wear;

import android.content.Context;

import com.google.android.gms.wearable.DataEventBuffer;
import com.google.android.gms.wearable.MessageEvent;
import com.vlabs.androiweartest.connection.ConnectionManager;
import com.vlabs.androiweartest.connection.ConnectionManagerImpl;
import com.vlabs.androiweartest.wear.handlers.IncomingHandler;
import com.vlabs.androiweartest.wear.managers.WearPlayerManager;

public class WearFacade {

    private final IncomingHandler mIncomingHandler;

    private final WearPlayerManager mWearPlayerManager;

    private final ConnectionManager mConnectionManager;

    public WearFacade(final Context context) {
        mConnectionManager = new ConnectionManagerImpl(context);
        mIncomingHandler = new IncomingHandler(context, mConnectionManager);
        mWearPlayerManager = new WearPlayerManager(mConnectionManager);
    }

    public void handleMessageEvent(final MessageEvent messageEvent) {
        mIncomingHandler.handleMessageEvent(messageEvent);
    }

    public void handleDataEvents(final DataEventBuffer dataEvents) {
        mIncomingHandler.handleDataEvents(dataEvents);
    }

    public WearPlayerManager playerManager() {
        return mWearPlayerManager;
    }

    public ConnectionManager connectionManager() {
        return mConnectionManager;
    }

}
