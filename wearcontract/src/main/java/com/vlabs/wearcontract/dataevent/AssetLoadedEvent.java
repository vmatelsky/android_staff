package com.vlabs.wearcontract.dataevent;

import com.google.android.gms.wearable.Asset;
import com.google.android.gms.wearable.DataEvent;
import com.google.android.gms.wearable.DataMap;
import com.google.android.gms.wearable.PutDataMapRequest;

public class AssetLoadedEvent {

    public static final String KEY_IMAGE_ASSET = "image-asset";
    public static final String KEY_IMAGE_PATH = "image-path";

    private final EventType mEventType;
    private final String mImageKey;
    private final Asset mAsset;

    public AssetLoadedEvent(final DataEvent dataEvent) {

        if (dataEvent.getType() == DataEvent.TYPE_CHANGED) {
            mEventType = EventType.CHANGED;
            final DataMap dataMap = DataMap.fromByteArray(dataEvent.getDataItem().getData());
            mImageKey = dataEvent.getDataItem().getUri().getPath();
            mAsset = assetFrom(dataMap);
        } else {
            mEventType = EventType.DELETED;
            mImageKey = "";
            mAsset = null;
        }
    }

    public AssetLoadedEvent(final String imageKey, final Asset asset) {
        mEventType = EventType.UNKNOWN;
        mImageKey = imageKey;
        mAsset = asset;
    }

    private Asset assetFrom(final DataMap dataMap) {
        return dataMap.getAsset(KEY_IMAGE_ASSET);
    }

    public EventType eventType() {
        return mEventType;
    }

    public String imageKey() {
        return mImageKey;
    }

    public Asset asset() {
        return mAsset;
    }

    public PutDataMapRequest asDataMapRequest() {
        final PutDataMapRequest dataMapRequest = PutDataMapRequest.create(mImageKey);
        dataMapRequest.getDataMap().putAsset(KEY_IMAGE_ASSET, mAsset);
        dataMapRequest.setUrgent();
        return dataMapRequest;
    }
}
