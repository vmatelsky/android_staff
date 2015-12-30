package com.vlabs.wearmanagers.connection;

import android.graphics.Bitmap;
import android.net.Uri;

import com.google.android.gms.wearable.Asset;
import com.google.android.gms.wearable.DataMap;
import com.google.android.gms.wearable.PutDataMapRequest;

public interface ConnectionManager {

    interface ImageListener {
        void onImage(String path, final Bitmap bitmap);
    }

    interface DataListener {
        void onData(final String path, final DataMap map);
    }

    boolean isConnected();

    void broadcastMessage(String path, DataMap dataMap);
    void getAssetAsBitmap(String path, final Asset asset, final ImageListener onImageReady);
    void getDataItems(final String path, final DataListener onDataReady);
    void putData(PutDataMapRequest putRequest);
    void deleteData(PutDataMapRequest putRequest);

}
