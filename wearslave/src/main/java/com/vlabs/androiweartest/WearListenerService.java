package com.vlabs.androiweartest;

import android.util.Log;

import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.data.FreezableUtils;
import com.google.android.gms.wearable.DataEvent;
import com.google.android.gms.wearable.DataEventBuffer;
import com.google.android.gms.wearable.DataItem;
import com.google.android.gms.wearable.DataMap;
import com.google.android.gms.wearable.DataMapItem;
import com.google.android.gms.wearable.MessageEvent;
import com.google.android.gms.wearable.Wearable;
import com.google.android.gms.wearable.WearableListenerService;

import java.nio.charset.Charset;
import java.util.List;

public class WearListenerService extends WearableListenerService {

    public static final String TAG = "wearslave-bla-bla";

    private GoogleApiClient mGoogleApiClient;

    @Override
    public void onCreate() {
        super.onCreate();
        mGoogleApiClient = new GoogleApiClient.Builder(this)
                .addApi(Wearable.API)
                .build();
        mGoogleApiClient.connect();
    }

    @Override
    public void onDestroy() {
        mGoogleApiClient.disconnect();
        super.onDestroy();
    }

    @Override
    public void onMessageReceived(MessageEvent messageEvent) {
        final String params = new String(messageEvent.getData(), Charset.forName("UTF-8"));
        final String msg = "path = " + messageEvent.getPath() + " message = " + params;
        Log.d(TAG, msg);
        reply(messageEvent.getSourceNodeId(), "Received " + params);
    }

    @Override
    public void onDataChanged(final DataEventBuffer dataEvents) {
        super.onDataChanged(dataEvents);

        final List<DataEvent> events = FreezableUtils.freezeIterable(dataEvents);
        for (DataEvent event : events) {

            if (event.getType() == DataEvent.TYPE_CHANGED) {
                DataItem item = event.getDataItem();
                if (item.getUri().getPath().compareTo("/count") == 0) {
                    DataMap dataMap = DataMapItem.fromDataItem(item).getDataMap();
                    Log.d(TAG, "count received: " + dataMap.getInt(WearMainActivity.COUNT_KEY));
                }
            } else if (event.getType() == DataEvent.TYPE_DELETED) {
                // DataItem deleted
            }
        }
    }

    private void reply(final String nodeId, final String message) {
        Wearable.MessageApi.sendMessage(mGoogleApiClient, nodeId, message, null).setResultCallback(result -> {
            if(!result.getStatus().isSuccess()) {
                Log.d(TAG, "Fail to send message: " + result.getRequestId());
            }
        });
    }

}
