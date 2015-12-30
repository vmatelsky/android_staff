package com.vlabs.wearcontract;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.android.gms.wearable.DataMap;

public class WearStation implements Parcelable {

    public interface Adapter {
        String getUniqueId();
        String getName();
        String getListenLink();
        String getImagePath();
        boolean getIsFavorite();
    }

    public static final String LIVE_UNIQUE_ID_PREFIX = "LR";
    public static final String CUSTOM_UNIQUE_ID_PREFIX = "CR";
    public static final String TALK_UNIQUE_ID_PREFIX = "TR";

    private static final String FIELD_ID = "unique-id";
    private static final String FIELD_NAME = "mName";
    private static final String FIELD_IMAGE = "image-uri";
    private static final String FIELD_LISTEN_LINK = "listen-link";
    private static final String FIELD_IS_FAVORITE = "is-favorite";

    private final String mName;
    private final String mUniqueId;
    private final String mListenLink;
    private final String mImagePath;
    private final boolean mIsFavorite;

    public WearStation(final DataMap map) {
        mUniqueId = map.getString(FIELD_ID);
        mName = map.getString(FIELD_NAME);
        mListenLink = map.getString(FIELD_LISTEN_LINK);
        mImagePath = map.getString(FIELD_IMAGE);
        mIsFavorite = map.getBoolean(FIELD_IS_FAVORITE);
    }

    public WearStation(final Adapter station) {
        mUniqueId = station.getUniqueId();
        mName = station.getName();
        mListenLink = station.getListenLink();
        mImagePath = station.getImagePath();
        mIsFavorite = station.getIsFavorite();
    }

    public DataMap toMap() {
        DataMap dataMap = new DataMap();
        dataMap.putString(FIELD_NAME, mName);
        dataMap.putString(FIELD_ID, mUniqueId);
        dataMap.putString(FIELD_LISTEN_LINK, mListenLink);
        dataMap.putString(FIELD_IMAGE, mImagePath);
        dataMap.putBoolean(FIELD_IS_FAVORITE, mIsFavorite);
        return dataMap;
    }

    public static WearStation fromDataMap(final DataMap map) {
        if (map == null) return null;
        return new WearStation(map);
    }

    @Override
    public String toString() {
        return "WearStation{" +
                "mName='" + mName + '\'' +
                ", mUniqueId='" + mUniqueId + '\'' +
                ", mListenLink='" + mListenLink + '\'' +
                ", mImagePath='" + mImagePath + '\'' +
                ", mIsFavorite='" + mIsFavorite + '\'' +
                '}';
    }

    public String name() {
        return mName;
    }

    public String getId() {
        return mUniqueId.substring(LIVE_UNIQUE_ID_PREFIX.length());
    }

    public String getImagePath() {
        return mImagePath;
    }

    public boolean isFavorite() {
        return mIsFavorite;
    }

    public boolean isLive() {
        return getListenLink().contains("/live");
    }

    public boolean isCustom() {
        return getListenLink().contains("/custom");
    }

    public String getListenLink() {
        return mListenLink;
    }

    public static Parcelable.Creator<WearStation> CREATOR = new Parcelable.Creator<WearStation>() {
        @Override
        public WearStation createFromParcel(Parcel source) {
            return new WearStation(source);
        }

        @Override
        public WearStation[] newArray(int size) {
            return new WearStation[size];
        }
    };

    @Override
    public int describeContents() {
        return 0;
    }

    public WearStation(Parcel source) {
        this.mName = source.readString();
        this.mUniqueId = source.readString();
        this.mListenLink = source.readString();
        this.mImagePath = source.readString();
        this.mIsFavorite = source.readByte() == 1;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(mName);
        dest.writeString(mUniqueId);
        dest.writeString(mListenLink);
        dest.writeString(mImagePath);
        dest.writeByte((byte) (mIsFavorite ? 1 : 0));
    }
}
