package com.vlabs.androiweartest.images;

import android.content.res.Resources;

import com.google.android.gms.wearable.Asset;
import com.google.android.gms.wearable.DataMap;
import com.vlabs.androiweartest.WearApplication;
import com.vlabs.androiweartest.events.data.OnAssetLoaded;
import com.vlabs.androiweartest.manager.ConnectionManager;
import com.vlabs.wearcontract.WearMessage;
import com.vlabs.wearcontract.dataevent.AssetLoadedEvent;
import com.vlabs.wearcontract.messages.LoadImageMessage;

import javax.inject.Inject;

import de.greenrobot.event.EventBus;
import rx.functions.Action1;

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
                final Asset asset = assetFromMap(map);

                if (asset == null) {
                    requestImageToBeLoaded(path);
                } else {
                    resolveBitmap(path, asset);
                }
            });
        } else {
            mConnectionManager.onConnected().subscribe(new Action1<Void>() {
                @Override
                public void call(final Void aVoid) {
                    mConnectionManager.getDataItems(imagePath, (path, map) -> {
                        final Asset asset = assetFromMap(map);

                        if (asset == null) {
                            requestImageToBeLoaded(path);
                        } else {
                            resolveBitmap(path, asset);
                        }
                    });
                }
            });
        }
    }

    public void requestImageToBeLoaded(final String imagePath) {
        mConnectionManager.broadcastMessage(WearMessage.LOAD_IMAGE, new LoadImageMessage(imagePath, mWindowHeight, mWindowWidth).asDataMap());
    }


    private Asset assetFromMap(final DataMap map) {
        if (map == null) return null;
        if (map.containsKey(AssetLoadedEvent.KEY_IMAGE_ASSET)) {
            return map.getAsset(AssetLoadedEvent.KEY_IMAGE_ASSET);
        }
        return null;
    }

    private void resolveBitmap(final String path, final Asset asset) {
        mConnectionManager.getAssetAsBitmap(path, asset);
    }

    @SuppressWarnings("unused")
    public void onEventBackgroundThread(OnAssetLoaded event) {
        mConnectionManager.getAssetAsBitmap(event.path(), event.asset());
    }
}
