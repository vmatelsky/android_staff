package com.vlabs.androiweartest.manager;

import com.google.android.gms.wearable.Asset;
import com.google.android.gms.wearable.DataMap;
import com.google.android.gms.wearable.PutDataMapRequest;
import com.vlabs.wearcontract.WearDataEvent;
import com.vlabs.wearcontract.WearMessage;

import rx.Observable;

public interface ConnectionManager {

    interface DataListener {
        void onData(final String path, final DataMap map);
    }

    boolean isConnected();

    Observable<Void> onConnected();

    void broadcastMessage(WearMessage message, DataMap dataMap);
    void getAssetAsBitmap(String path, final Asset asset);
    void getDataItems(final String path, final DataListener onDataReady);
    void putData(PutDataMapRequest putRequest);
    void deleteData(PutDataMapRequest putRequest);

}
