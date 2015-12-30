package com.vlabs.androiweartest.managers;

import android.content.res.Resources;
import android.graphics.Bitmap;

import com.google.android.gms.wearable.Asset;
import com.google.android.gms.wearable.DataMap;
import com.vlabs.androiweartest.WearApplication;
import com.vlabs.wearcontract.Data;
import com.vlabs.wearcontract.Message;
import com.vlabs.wearmanagers.Receiver;
import com.vlabs.wearmanagers.connection.ConnectionManager;

import java.util.HashMap;


public class ImageLoader {

    private final HashMap<String, Asset> mAssetCache = new HashMap<>();

    private final int mWindowHeight;
    private final int mWindowWidth;

    public ImageLoader() {

        Resources res = WearApplication.instance().getResources();
        mWindowHeight = res.getDisplayMetrics().heightPixels;
        mWindowWidth = res.getDisplayMetrics().widthPixels;
    }

    public void imageByPath(final String imagePath, final Receiver<Bitmap> receiver) {
        if (mAssetCache.containsKey(imagePath)) {
            final Asset asset = mAssetCache.get(imagePath);
            resolveBitmap(imagePath, asset, receiver);
            return;
        }

        final ConnectionManager.DataListener listener = new ConnectionManager.DataListener() {
            @Override
            public void onData(final String path, final DataMap map) {
                final Asset asset = assetFromMap(map);

                if (asset == null) {
                    requestImageToBeLoaded(path);
                }
                else {
                    resolveBitmap(path, asset, receiver);
                    mAssetCache.put(path, asset);
                }
            }
        };

        WearApplication.instance().connectionManager().getDataItems(imagePath, listener);
    }

    private void requestImageToBeLoaded(final String path) {
        final DataMap dataMap = new DataMap();
        dataMap.putString(Message.KEY_LOAD_IMAGE_KEY, path);
        dataMap.putInt(Message.KEY_LOAD_HEIGHT, mWindowHeight);
        dataMap.putInt(Message.KEY_LOAD_WIDTH, mWindowWidth);
        WearApplication.instance().connectionManager().broadcastMessage(Message.PATH_LOAD_IMAGE, dataMap);
    }

    private Asset assetFromMap(final DataMap map) {
        if (map == null) return null;
        if (map.containsKey(Data.KEY_IMAGE_ASSET)) {
            return map.getAsset(Data.KEY_IMAGE_ASSET);
        }
        return null;
    }

    private void resolveBitmap(final String path, final Asset asset, final Receiver<Bitmap> receiver) {
        WearApplication.instance().connectionManager().getAssetAsBitmap(path, asset, new ConnectionManager.ImageListener() {
            @Override
            public void onImage(final String path, final Bitmap bitmap) {
                receiver.receive(bitmap);
            }
        });
    }
}
