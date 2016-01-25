package com.vlabs.androiweartest.models;

import com.vlabs.wearmanagers.connection.ConnectionManager;
import com.vlabs.wearmanagers.message.MessageManager;

public class ImageByDataPathModel {

    private final ConnectionManager mConnectionManager;

    private final MessageManager mMessageManager;

    private String mPath;

    public ImageByDataPathModel(
            final ConnectionManager connectionManager,
            final MessageManager messageManager) {
        mConnectionManager = connectionManager;
        mMessageManager = messageManager;
    }

    public void activate() {

    }

    public void deactivate() {

    }

    public void updateImagePath(final String newPath) {

    }
}
