package com.vlabs.androiweartest.images;

import android.content.res.Resources;

import com.vlabs.androiweartest.WearApplication;
import com.vlabs.androiweartest.events.data.OnAssetLoaded;
import com.vlabs.androiweartest.manager.ConnectionManager;
import com.vlabs.wearcontract.WearMessage;
import com.vlabs.wearcontract.messages.LoadImageMessage;

import javax.inject.Inject;

import de.greenrobot.event.EventBus;

public class ImageLoader {

    @Inject
    ConnectionManager mConnectionManager;

    @Inject
    EventBus mEventBus;

    private final int mWindowHeight;
    private final int mWindowWidth;

    public ImageLoader(final ConnectionManager connectionManager, final EventBus eventBus) {
        mConnectionManager = connectionManager;
        mEventBus = eventBus;
        mEventBus.register(this);

        final Resources res = WearApplication.instance().getResources();
        mWindowHeight = res.getDisplayMetrics().heightPixels;
        mWindowWidth = res.getDisplayMetrics().widthPixels;
    }

    public void imageByPath(final String imagePath) {
        if (mConnectionManager.isConnected()) {
            mConnectionManager.getDataItems(imagePath, (path, map) -> {
                requestImageToBeLoaded(path);
            });
        } else {
            mConnectionManager.onConnected().subscribe(aVoid -> {
                mConnectionManager.getDataItems(imagePath, (path, map) -> {
                    requestImageToBeLoaded(path);
                });
            });
        }
    }

    public void requestImageToBeLoaded(final String imagePath) {
        mConnectionManager.broadcastMessage(WearMessage.LOAD_IMAGE, new LoadImageMessage(imagePath, mWindowHeight, mWindowWidth).asDataMap());
    }

    @SuppressWarnings("unused")
    public void onEventBackgroundThread(OnAssetLoaded event) {
        mConnectionManager.getAssetAsBitmap(event.path(), event.asset());
    }
}
