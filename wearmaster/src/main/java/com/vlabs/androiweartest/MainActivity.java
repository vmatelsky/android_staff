package com.vlabs.androiweartest;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.widget.Toast;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.PendingResult;
import com.google.android.gms.common.data.FreezableUtils;
import com.google.android.gms.wearable.DataApi;
import com.google.android.gms.wearable.DataEvent;
import com.google.android.gms.wearable.DataEventBuffer;
import com.google.android.gms.wearable.DataItem;
import com.google.android.gms.wearable.DataMap;
import com.google.android.gms.wearable.DataMapItem;
import com.google.android.gms.wearable.Node;
import com.google.android.gms.wearable.PutDataMapRequest;
import com.google.android.gms.wearable.PutDataRequest;
import com.google.android.gms.wearable.Wearable;

import java.util.List;

public class MainActivity extends AppCompatActivity implements
        DataApi.DataListener,
        GoogleApiClient.ConnectionCallbacks,
        GoogleApiClient.OnConnectionFailedListener {

    public static final String TAG = "main-bla-bla";
    public static final String COUNT_KEY = "com.example.key.count";
    public static final String PATH_COUNT = "/count";
    private GoogleApiClient mGoogleApiClient;
    private int count = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        findViewById(R.id.send_broadcast).setOnClickListener(v -> increaseCounter());

        findViewById(R.id.send_direct).setOnClickListener(v -> sendDirect());

        mGoogleApiClient = new GoogleApiClient.Builder(this)
                .addConnectionCallbacks(this)
                .addOnConnectionFailedListener(this)
                .addApi(Wearable.API)
                .build();
    }

    private void sendDirect() {
        Wearable.NodeApi.getConnectedNodes(mGoogleApiClient).setResultCallback(result -> {
            if (result.getStatus().isSuccess()) {
                List<Node> nodes = result.getNodes();
                if (nodes.size() > 0) {
                    String nodeId = nodes.get(0).getId();
                    Wearable.MessageApi.sendMessage(mGoogleApiClient, nodeId, PATH_COUNT, Integer.toString(count).getBytes()).setResultCallback(result1 -> {
                        if(!result1.getStatus().isSuccess()) {
                            Log.d(TAG, "Fail to send message: " + result1.getRequestId());
                        }
                    });
                }
            } else {
                Toast.makeText(MainActivity.this, "node id is null", Toast.LENGTH_LONG).show();
            }
        });
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
                if (item.getUri().getPath().compareTo(PATH_COUNT) == 0) {
                    DataMap dataMap = DataMapItem.fromDataItem(item).getDataMap();
                    final int count = dataMap.getInt(COUNT_KEY);
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
        PutDataMapRequest putDataMapReq = PutDataMapRequest.create(PATH_COUNT);
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
