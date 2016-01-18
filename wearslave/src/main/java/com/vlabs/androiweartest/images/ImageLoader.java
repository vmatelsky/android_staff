package com.vlabs.androiweartest.images;

import android.content.res.Resources;
import android.graphics.Bitmap;

import com.google.android.gms.wearable.Asset;
import com.google.android.gms.wearable.DataMap;
import com.vlabs.androiweartest.WearApplication;
import com.vlabs.androiweartest.di.component.AppComponent;
import com.vlabs.wearcontract.Data;
import com.vlabs.wearcontract.Message;
import com.vlabs.wearmanagers.Receiver;
import com.vlabs.wearmanagers.connection.ConnectionManager;

import javax.inject.Inject;

public class ImageLoader {

    @Inject
    ConnectionManager mConnectionManager;

    private final int mWindowHeight;
    private final int mWindowWidth;

    public ImageLoader(final AppComponent appComponent) {
        appComponent.inject(this);

        final Resources res = WearApplication.instance().getResources();
        mWindowHeight = res.getDisplayMetrics().heightPixels;
        mWindowWidth = res.getDisplayMetrics().widthPixels;
    }

    public void imageByPath(final String imagePath, final Receiver<Bitmap> receiver) {
        mConnectionManager.getDataItems(imagePath, (path, map) -> {
            final Asset asset = assetFromMap(map);

            if (asset == null) {
                requestImageToBeLoaded(path);
            } else {
                resolveBitmap(path, asset, receiver);
            }
        });
    }

    private void requestImageToBeLoaded(final String path) {
        final DataMap dataMap = new DataMap();
        dataMap.putString(Message.KEY_LOAD_IMAGE_KEY, path);
        dataMap.putInt(Message.KEY_LOAD_HEIGHT, mWindowHeight);
        dataMap.putInt(Message.KEY_LOAD_WIDTH, mWindowWidth);
        mConnectionManager.broadcastMessage(Message.PATH_LOAD_IMAGE, dataMap);
    }

    private Asset assetFromMap(final DataMap map) {
        if (map == null) return null;
        if (map.containsKey(Data.KEY_IMAGE_ASSET)) {
            return map.getAsset(Data.KEY_IMAGE_ASSET);
        }
        return null;
    }

    private void resolveBitmap(final String path, final Asset asset, final Receiver<Bitmap> receiver) {
        mConnectionManager.getAssetAsBitmap(path, asset, (path1, bitmap) -> receiver.receive(bitmap));
    }
}
