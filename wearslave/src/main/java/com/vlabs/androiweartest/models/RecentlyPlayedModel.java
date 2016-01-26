package com.vlabs.androiweartest.models;


import com.vlabs.androiweartest.manager.ConnectionManager;

import de.greenrobot.event.EventBus;

public class RecentlyPlayedModel {

    private final EventBus mEventBus;
    private final ConnectionManager mConnectionManager;

    public RecentlyPlayedModel(final EventBus eventBus, final ConnectionManager connectionManager) {
        mEventBus = eventBus;
        mConnectionManager = connectionManager;
    }

    public void activate() {

    }

    public void deactivate() {

    }

}
