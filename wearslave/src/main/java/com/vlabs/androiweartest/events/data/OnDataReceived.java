package com.vlabs.androiweartest.events.data;

import com.google.android.gms.wearable.DataMap;

public class OnDataReceived {

    private final String mPath;
    private final DataMap mDataMap;

    public OnDataReceived(final String path, final DataMap dataMap) {
        mPath = path;
        mDataMap = dataMap;
    }

    public String path() {
        return mPath;
    }

    public DataMap dataMap() {
        return mDataMap;
    }
}