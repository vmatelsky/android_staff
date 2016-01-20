package com.vlabs;

import com.google.android.gms.wearable.DataMap;

public class DataMapBuilder {
    private final DataMap mMap = new DataMap();

    public DataMapBuilder putBoolean(final java.lang.String key, final boolean value) {
        mMap.putBoolean(key, value);
        return this;
    }

    public DataMapBuilder putByte(final java.lang.String key, final byte value) {
        mMap.putByte(key, value);
        return this;
    }

    public DataMapBuilder putInt(final java.lang.String key, final int value) {
        mMap.putInt(key, value);
        return this;
    }

    public DataMapBuilder putLong(final java.lang.String key, final long value) {
        mMap.putLong(key, value);
        return this;
    }

    public DataMapBuilder putFloat(final java.lang.String key, final float value) {
        mMap.putFloat(key, value);
        return this;
    }

    public DataMapBuilder putDouble(final java.lang.String key, final double value) {
        mMap.putDouble(key, value);
        return this;
    }

    public DataMapBuilder putString(final java.lang.String key, final java.lang.String value) {
        mMap.putString(key, value);
        return this;
    }

    public DataMapBuilder putAsset(final java.lang.String key, final com.google.android.gms.wearable.Asset value) {
        mMap.putAsset(key, value);
        return this;
    }

    public DataMapBuilder putDataMap(final String key, final DataMap value) {
        mMap.putDataMap(key, value);
        return this;
    }

    public DataMapBuilder putDataMapArrayList(final java.lang.String key, final java.util.ArrayList<com.google.android.gms.wearable.DataMap> value) {
        mMap.putDataMapArrayList(key, value);
        return this;
    }

    public DataMapBuilder putIntegerArrayList(final java.lang.String key, final java.util.ArrayList<java.lang.Integer> value) {
        mMap.putIntegerArrayList(key, value);
        return this;
    }

    public DataMapBuilder putStringArrayList(final java.lang.String key, final java.util.ArrayList<java.lang.String> value) {
        mMap.putStringArrayList(key, value);
        return this;
    }

    public DataMapBuilder putByteArray(final java.lang.String key, final byte[] value) {
        mMap.putByteArray(key, value);
        return this;
    }

    public DataMapBuilder putLongArray(final java.lang.String key, final long[] value) {
        mMap.putLongArray(key, value);
        return this;
    }

    public DataMapBuilder putFloatArray(final java.lang.String key, final float[] value) {
        mMap.putFloatArray(key, value);
        return this;
    }

    public DataMapBuilder putStringArray(final java.lang.String key, final java.lang.String[] value) {
        mMap.putStringArray(key, value);
        return this;
    }

    public DataMap getMap() {
        return mMap;
    }
}
