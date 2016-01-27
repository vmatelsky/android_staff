package com.vlabs.androiweartest.events.data;

import com.google.android.gms.wearable.Asset;
import com.google.android.gms.wearable.DataEvent;
import com.google.android.gms.wearable.DataMapItem;
import com.vlabs.wearcontract.dataevent.AssetLoadedEvent;

public class OnAssetLoaded {

    public static OnAssetLoaded fromDataEvent(final DataEvent event) {
        if (event.getType() == DataEvent.TYPE_CHANGED) {
            final DataMapItem dataMapItem = DataMapItem.fromDataItem(event.getDataItem());
            final Asset asset = dataMapItem.getDataMap().getAsset(AssetLoadedEvent.KEY_IMAGE_ASSET);
            final String assetPath = dataMapItem.getDataMap().getString(AssetLoadedEvent.KEY_IMAGE_PATH);
            return new OnAssetLoaded(assetPath, asset);
        }

        return null;
    }

    private final String mPath;
    private final Asset mAsset;

    public OnAssetLoaded(final String path, final Asset asset) {
        mPath = path;
        mAsset = asset;
    }

    public String path() {
        return mPath;
    }

    public Asset asset() {
        return mAsset;
    }
}
