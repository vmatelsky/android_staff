package com.vlabs.wearcontract.messages;

import com.google.android.gms.wearable.DataMap;
import com.vlabs.DataMapBuilder;

public class LoadImageMessage {

    public static final String KEY_LOAD_IMAGE_KEY = "key";
    public static final String KEY_LOAD_WIDTH = "width";
    public static final String KEY_LOAD_HEIGHT = "height";

    private final String mPath;
    private final int mWindowHeight;
    private final int mWindowWidth;

    public LoadImageMessage(final DataMap dataMap) {
        mPath = dataMap.getString(KEY_LOAD_IMAGE_KEY);
        mWindowHeight = dataMap.getInt(KEY_LOAD_HEIGHT);
        mWindowWidth = dataMap.getInt(KEY_LOAD_WIDTH);
    }

    public LoadImageMessage(final String path, final int windowHeight, final int windowWidth) {
        mPath = path;
        mWindowHeight = windowHeight;
        mWindowWidth = windowWidth;
    }

    public String imagePath() {
        return mPath;
    }

    public int width() {
        return mWindowWidth;
    }

    public int height() {
        return mWindowHeight;
    }

    public DataMap asDataMap() {
        return new DataMapBuilder()
                .putString(KEY_LOAD_IMAGE_KEY, mPath)
                .putInt(KEY_LOAD_HEIGHT, mWindowHeight)
                .putInt(KEY_LOAD_WIDTH, mWindowWidth)
                .getMap();
    }
}
