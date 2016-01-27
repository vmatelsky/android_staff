package com.vlabs.androiweartest.connection;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GooglePlayServicesUtil;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.PendingResult;
import com.google.android.gms.common.api.ResultCallback;
import com.google.android.gms.wearable.Asset;
import com.google.android.gms.wearable.DataMap;
import com.google.android.gms.wearable.Node;
import com.google.android.gms.wearable.NodeApi;
import com.google.android.gms.wearable.PutDataMapRequest;
import com.google.android.gms.wearable.PutDataRequest;
import com.google.android.gms.wearable.Wearable;
import com.vlabs.wearcontract.WearMessage;

import java.io.IOException;
import java.io.InputStream;
import java.util.concurrent.TimeUnit;

import rx.Observable;
import rx.subjects.PublishSubject;

public class ConnectionManagerImpl implements
        ConnectionManager,
        GoogleApiClient.ConnectionCallbacks,
        GoogleApiClient.OnConnectionFailedListener {

    public static final String TAG = ConnectionManagerImpl.class.getSimpleName();
    private static final long ASSET_TIMEOUT = 10;

    private GoogleApiClient mGoogleApiClient;
    private PublishSubject<Void> mOnConnected = PublishSubject.create();

    public ConnectionManagerImpl(final Context context) {
        mGoogleApiClient = new GoogleApiClient.Builder(context)
                .addApi(Wearable.API)
                .addConnectionCallbacks(this)
                .addOnConnectionFailedListener(this)
                .build();

        if (ensurePlayAvailable(context)) {
            mGoogleApiClient.connect();
        }
    }

    private boolean ensurePlayAvailable(Context context) {
        try {
            final int servicesAvailable = GooglePlayServicesUtil.isGooglePlayServicesAvailable(context);
            if (servicesAvailable != ConnectionResult.SUCCESS) {
                Log.e(TAG, "Google Services not available due to: " + GooglePlayServicesUtil.getErrorString(servicesAvailable) + " can't start wear!");
                return false;
            }
            return true;
        } catch (final IllegalStateException ise) {
            return false;
        }
    }

    @Override
    public void onConnected(final Bundle connectionHint) {
        Log.d(TAG, "onConnected: " + connectionHint);
        mOnConnected.onNext(null);
    }

    @Override
    public void onConnectionSuspended(final int cause) {
        Log.d(TAG, "onConnectionSuspended: " + cause);
    }

    @Override
    public void onConnectionFailed(final ConnectionResult connectionResult) {
        final String errorMessage = connectionResult == null ? "unknown error" : connectionResult.getErrorMessage();
        Log.e(TAG, "Can't start wear because " + errorMessage);
    }

    @Override
    public boolean isConnected() {
        return mGoogleApiClient.isConnected();
    }

    @Override
    public Observable<Void> onConnected() {
        return mOnConnected;
    }

    @Override
    public void broadcastMessage(final WearMessage message, final DataMap map) {
        connectedNodes().setResultCallback(new ResultCallback<NodeApi.GetConnectedNodesResult>() {
            @Override
            public void onResult(final NodeApi.GetConnectedNodesResult result) {
                for(Node node : result.getNodes()) {
                    Wearable.MessageApi.sendMessage(mGoogleApiClient, node.getId(), message.path(), map.toByteArray());
                }
            }
        });
    }

    @Override
    public void getAssetAsBitmap(final String path, final Asset asset, final ImageListener onImageReady) {
        if (asset == null) {
            throw new IllegalArgumentException("Asset can't be null");
        }

        if (!isConnected()) {
            onImageReady.onImage(path, null);
            Log.d(TAG, "Not connected, returning null image.");
            return;
        }

        new AsyncTask<Asset, Void, Bitmap>() {
            @Override
            protected Bitmap doInBackground(Asset... params) {
                ConnectionResult connectionResult = mGoogleApiClient.blockingConnect(ASSET_TIMEOUT, TimeUnit.SECONDS);

                if (!connectionResult.isSuccess()) return null;
                InputStream inputStream = Wearable.DataApi.getFdForAsset(mGoogleApiClient, asset).await().getInputStream();
                if (inputStream == null) {
                    return null;
                }

                Bitmap bitmap = BitmapFactory.decodeStream(inputStream);
                try {
                    inputStream.close();
                } catch (IOException e) {
                    Log.w(TAG, "Exception while closing asset stream", e);
                }
                return bitmap;
            }

            @Override
            protected void onPostExecute(Bitmap bitmap) {
                super.onPostExecute(bitmap);
                onImageReady.onImage(path, bitmap);
            }
        }.execute(asset);
    }

    public void getDataItems(final String path, final DataListener onDataReady) {
        if (isConnected()) {
            WearUtils.getDataItems(mGoogleApiClient, path, new DataListener() {
                @Override
                public void onData(String path, DataMap dataMap) {
                    onDataReady.onData(path, dataMap);
                }
            });
        }
        else {
            onDataReady.onData(path, null);
        }
    }

    @Override
    public void putData(final PutDataRequest putRequest) {
        Wearable.DataApi.putDataItem(mGoogleApiClient, putRequest);
    }

    @Override
    public void deleteData(final PutDataMapRequest putRequest) {
        Wearable.DataApi.deleteDataItems(mGoogleApiClient, putRequest.getUri());
    }

    public PendingResult<NodeApi.GetConnectedNodesResult> connectedNodes() {
        return Wearable.NodeApi.getConnectedNodes(mGoogleApiClient);
    }
}
