package com.vlabs.androiweartest.wear;

import android.content.Context;

import com.google.android.gms.wearable.DataEventBuffer;
import com.google.android.gms.wearable.MessageEvent;
import com.vlabs.androiweartest.wear.connection.ConnectionManager;
import com.vlabs.androiweartest.wear.di.WearScopeModule;
import com.vlabs.androiweartest.wear.handlers.IncomingHandler;
import com.vlabs.androiweartest.wear.managers.WearPlayerManager;

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

    public WearFacade(
            final Context context) {
        mObjectGraph = ObjectGraph.create(new WearScopeModule(context));
        mObjectGraph.inject(this);
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
