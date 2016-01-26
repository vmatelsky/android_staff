package com.vlabs.androiweartest.events.data;

import android.graphics.Bitmap;

public class OnImageLoaded {

    private final String mPath;
    private final Bitmap mImage;

    public OnImageLoaded(final String path, final Bitmap image) {
        mPath = path;
        mImage = image;
    }

    public String path() {
        return mPath;
    }

    public Bitmap image() {
        return mImage;
    }
}
