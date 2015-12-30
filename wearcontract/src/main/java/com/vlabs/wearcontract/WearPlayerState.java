package com.vlabs.wearcontract;

import com.google.android.gms.wearable.DataMap;

public class WearPlayerState {
    public static final String KEY_PLAYING = "playing";
    public static final String KEY_IMAGE = "image";
    public static final String KEY_STATION_IMAGE = "station-image";
    public static final String KEY_TITLE = "title";
    public static final String KEY_SUBTITLE = "subtitle";
    public static final String KEY_TRACKID = "trackId";
    public static final String KEY_THUMBED = "thumbed";
    public static final String KEY_THUMBS_ENABLED = "thumbs-enabled";
    public static final String KEY_DEVICE_VOLUME = "device-volume";
    public static final int VALUE_THUMBED_DOWN = -1;
    public static final int VALUE_THUMBED_UP = 1;
    public static final int UNKNOWN_VOLUME = -1;

    private final DataMap mDataMap;

    public WearPlayerState(final DataMap dataMap) {
        mDataMap = dataMap;
    }

    public WearPlayerState() {
        mDataMap = new DataMap();
    }

    public void setIsPlaying(final boolean isPlaying) {
        mDataMap.putBoolean(KEY_PLAYING, isPlaying);
    }

    public boolean isPlaying() {
        return mDataMap.getBoolean(KEY_PLAYING);
    }

    public void setImagePath(final String imagePath) {
        mDataMap.putString(KEY_IMAGE, imagePath);
    }

    public String getImagePath() {
        return mDataMap.getString(KEY_IMAGE);
    }

    public void setStationImagePath(final String stationImage) {
        mDataMap.putString(KEY_STATION_IMAGE, stationImage);
    }

    public void setTitle(final String title) {
        mDataMap.putString(KEY_TITLE, title);
    }

    public String getTitle(){
        return mDataMap.getString(KEY_TITLE);
    }

    public void setSubtitle(final String subtitle) {
        mDataMap.putString(KEY_SUBTITLE, subtitle);
    }

    public String getSubtitle() {
        return mDataMap.getString(KEY_SUBTITLE);
    }

    public void setThumbedState(final int thumbedValue) {
        mDataMap.putInt(KEY_THUMBED, thumbedValue);
    }

    public boolean isThumbedUp() {
        return mDataMap.getInt(KEY_THUMBED, 0) == VALUE_THUMBED_UP;
    }

    public boolean isThumbedDown() {
        return mDataMap.getInt(KEY_THUMBED, 0) == VALUE_THUMBED_DOWN;
    }

    public void setThumbsEnabled(final boolean thumbsEnabled) {
        mDataMap.putBoolean(KEY_THUMBS_ENABLED, thumbsEnabled);
    }

    public boolean isThumbsEnabled() {
        return mDataMap.getBoolean(KEY_THUMBS_ENABLED);
    }

    public void setDeviceVolume(int volume) {
        mDataMap.putInt(KEY_DEVICE_VOLUME, volume);
    }

    public int getDeviceVolume() {
        return mDataMap.getInt(KEY_DEVICE_VOLUME, UNKNOWN_VOLUME);
    }

    public boolean isEmpty() {
        return mDataMap.isEmpty();
    }

    public DataMap getData() {
        return mDataMap;
    }

    @Override
    public String toString() {
        return mDataMap.toString();
    }
}
