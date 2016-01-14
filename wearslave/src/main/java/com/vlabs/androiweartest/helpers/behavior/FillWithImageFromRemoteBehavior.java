package com.vlabs.androiweartest.helpers.behavior;

import android.graphics.Bitmap;
import android.widget.ImageView;

import com.google.android.gms.wearable.Asset;
import com.google.android.gms.wearable.DataEvent;
import com.google.android.gms.wearable.DataMap;
import com.vlabs.androiweartest.WearApplication;
import com.vlabs.wearcontract.Data;
import com.vlabs.wearmanagers.connection.ConnectionManager;

import rx.Subscription;
import rx.functions.Action1;

public class FillWithImageFromRemoteBehavior implements
        Action1<DataEvent>,
        ConnectionManager.ImageListener,
        ConnectionManager.DataListener {

    private final ImageView mImageView;
    private final String mPath;

    private Subscription mCurrentSubscription;

    public FillWithImageFromRemoteBehavior(final ImageView imageView, final String path) {
        mImageView = imageView;
        mPath = path;
    }

    public void startListening() {
        mCurrentSubscription = WearApplication.instance().messageManager().onDataByPath(mPath).subscribe(this);
        WearApplication.instance().connectionManager().getDataItems(mPath, this);
    }

    public void stopListening() {
        if (mCurrentSubscription != null) {
            mCurrentSubscription.unsubscribe();
            mCurrentSubscription = null;
        }
    }

    private void handleDataMap(final DataMap dataMap) {
        if (dataMap == null) return;

        final Asset asset = dataMap.getAsset(Data.KEY_IMAGE_ASSET);
        WearApplication.instance().connectionManager().getAssetAsBitmap(mPath, asset, this);
    }

    @Override
    public void onImage(final String path, final Bitmap bitmap) {
        mImageView.setImageBitmap(bitmap);
    }

    @Override
    public void onData(final String path, final DataMap map) {
        handleDataMap(map);
    }

    @Override
    public void call(final DataEvent dataEvent) {
        handleDataMap(DataMap.fromByteArray(dataEvent.getDataItem().getData()));
    }
}
