package com.vlabs.androiweartest.job;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.Log;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.wearable.Asset;
import com.google.android.gms.wearable.Wearable;
import com.vlabs.androiweartest.events.data.OnImageLoaded;
import com.vlabs.androiweartest.job.base.BaseJob;

import java.io.IOException;
import java.io.InputStream;
import java.util.concurrent.TimeUnit;

public class AssetAsBitmap extends BaseJob {

    public static final String TAG = AssetAsBitmap.class.getSimpleName();

    final String mPath;
    final Asset mAsset;

    public AssetAsBitmap(final String path, final Asset asset) {
        super();
        mPath = path;
        mAsset = asset;

        if (asset == null) {
            throw new IllegalArgumentException("Asset can't be null");
        }
    }

    @Override
    public void onAdded() {

    }

    @Override
    public void onRun() throws Throwable {
        final ConnectionResult connectionResult = googleApiClient().blockingConnect(ASSET_TIMEOUT, TimeUnit.SECONDS);

        if (!connectionResult.isSuccess()) return;

        final InputStream inputStream = Wearable.DataApi.getFdForAsset(googleApiClient(), mAsset).await().getInputStream();

        if (inputStream == null) return;

        Bitmap bitmap = BitmapFactory.decodeStream(inputStream);
        try {
            inputStream.close();
        } catch (IOException e) {
            Log.w(TAG, "Exception while closing asset stream", e);
        }

        eventBus().post(new OnImageLoaded(mPath, bitmap));
    }

    @Override
    protected void onCancel() {

    }
}
