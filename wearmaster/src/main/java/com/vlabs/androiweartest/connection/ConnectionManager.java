package com.vlabs.androiweartest.connection;

import android.graphics.Bitmap;

import com.google.android.gms.wearable.Asset;
import com.google.android.gms.wearable.DataMap;
import com.google.android.gms.wearable.PutDataMapRequest;
import com.vlabs.wearcontract.WearMessage;

import rx.Observable;

public interface ConnectionManager {

    interface ImageListener {
        void onImage(String path, final Bitmap bitmap);
    }

    interface DataListener {
        void onData(final String path, final DataMap map);
    }

    boolean isConnected();

    Observable<Void> onConnected();

    void broadcastMessage(WearMessage message, DataMap dataMap);
    void getAssetAsBitmap(String path, final Asset asset, final ImageListener onImageReady);
    void getDataItems(final String path, final DataListener onDataReady);
    void putData(PutDataMapRequest putRequest);
    void deleteData(PutDataMapRequest putRequest);

}
