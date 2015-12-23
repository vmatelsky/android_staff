package com.vlabs.androiweartest;

import android.app.Activity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.PendingResult;
import com.google.android.gms.common.api.ResultCallback;
import com.google.android.gms.common.data.FreezableUtils;
import com.google.android.gms.wearable.DataApi;
import com.google.android.gms.wearable.DataEvent;
import com.google.android.gms.wearable.DataEventBuffer;
import com.google.android.gms.wearable.DataItem;
import com.google.android.gms.wearable.DataMap;
import com.google.android.gms.wearable.DataMapItem;
import com.google.android.gms.wearable.PutDataMapRequest;
import com.google.android.gms.wearable.PutDataRequest;
import com.google.android.gms.wearable.Wearable;

import java.util.List;

public class WearMainActivity extends Activity implements
        DataApi.DataListener,
        GoogleApiClient.ConnectionCallbacks,
        GoogleApiClient.OnConnectionFailedListener{

    public static final String TAG = WearListenerService.TAG;
    private GoogleApiClient mGoogleApiClient;
    private int count;
    public static final String COUNT_KEY = "com.example.key.count";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_display);
        findViewById(R.id.send_message).setOnClickListener(v -> increaseCounter());

        mGoogleApiClient = new GoogleApiClient.Builder(this)
                .addConnectionCallbacks(this)
                .addOnConnectionFailedListener(this)
                .addApi(Wearable.API)
                .build();
    }

    @Override
    protected void onStart() {
        super.onStart();
        mGoogleApiClient.connect();
    }

    @Override
    protected void onStop() {
        if (null != mGoogleApiClient && mGoogleApiClient.isConnected()) {
            Wearable.DataApi.removeListener(mGoogleApiClient, this);
            mGoogleApiClient.disconnect();
        }

        super.onStop();
    }

    @Override
    public void onConnected(final Bundle connectionHint) {
        Log.d(TAG, "onConnected: " + connectionHint);
        Wearable.DataApi.addListener(mGoogleApiClient, this);
    }

    @Override
    public void onConnectionSuspended(final int cause) {
        Log.d(TAG, "onConnectionSuspended: " + cause);
    }

    @Override
    public void onDataChanged(final DataEventBuffer dataEvents) {
        final List<DataEvent> events = FreezableUtils.freezeIterable(dataEvents);
        for (DataEvent event : events) {

            if (event.getType() == DataEvent.TYPE_CHANGED) {
                DataItem item = event.getDataItem();
                if (item.getUri().getPath().compareTo("/count") == 0) {
                    DataMap dataMap = DataMapItem.fromDataItem(item).getDataMap();
                    final int count = dataMap.getInt(WearMainActivity.COUNT_KEY);
                    Log.d(TAG, "count received: " + count);
                    updateCount(count);
                }
            } else if (event.getType() == DataEvent.TYPE_DELETED) {
                // DataItem deleted
            }
        }

    }

    private void updateCount(final int count) {
        this.count = count;
    }

    @Override
    public void onConnectionFailed(final ConnectionResult result) {
        Log.d(TAG, "onConnectionFailed: " + result);
    }

    private void increaseCounter() {
        PutDataMapRequest putDataMapReq = PutDataMapRequest.create("/count");
        putDataMapReq.getDataMap().putInt(COUNT_KEY, ++count);
        PutDataRequest putDataReq = putDataMapReq.asPutDataRequest();
        putDataMapReq.setUrgent();
        PendingResult<DataApi.DataItemResult> pendingResult = Wearable.DataApi.putDataItem(mGoogleApiClient, putDataReq);
        pendingResult.setResultCallback(result -> {
            if(!result.getStatus().isSuccess()) {
                Log.d(TAG, "Fail to send message: " + result.getDataItem().getUri());
            }
        });
    }
}
