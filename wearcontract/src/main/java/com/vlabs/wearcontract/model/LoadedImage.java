package com.vlabs.wearcontract.model;

import com.google.android.gms.wearable.Asset;

public class LoadedImage {

    private final Asset mAsset;
    private final String mImagePath;

    public LoadedImage(final Asset asset, final String imagePath) {
        mAsset = asset;
        mImagePath = imagePath;
    }

    public Asset asset() {
        return mAsset;
    }

    public String imagePath() {
        return mImagePath;
    }
}
