package com.vlabs.androiweartest;

import android.util.Log;

import com.google.android.gms.common.data.FreezableUtils;
import com.google.android.gms.wearable.DataEvent;
import com.google.android.gms.wearable.DataEventBuffer;
import com.google.android.gms.wearable.DataItem;
import com.google.android.gms.wearable.DataMap;
import com.google.android.gms.wearable.DataMapItem;
import com.google.android.gms.wearable.MessageEvent;
import com.google.android.gms.wearable.Node;
import com.google.android.gms.wearable.WearableListenerService;

import java.util.List;

public class MasterListenerService extends WearableListenerService {

    public static final String TAG = MainActivity.TAG;

    @Override
    public void onMessageReceived(final MessageEvent messageEvent) {
        super.onMessageReceived(messageEvent);
        Log.d(TAG, "onMessageReceived: " + messageEvent.getPath());
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
                    Log.d(TAG, "count received: " + dataMap.getInt(MainActivity.COUNT_KEY));
                }
            } else if (event.getType() == DataEvent.TYPE_DELETED) {
                // DataItem deleted
            }
        }
    }

    @Override
    public void onConnectedNodes(final List<Node> connectedNodes) {
        super.onConnectedNodes(connectedNodes);
        Log.d(TAG, "onConnectedNodes: " + connectedNodes.toString());
    }

    @Override
    public void onPeerConnected(final Node peer) {
        super.onPeerConnected(peer);
        Log.d(TAG, "onPeerConnected: " + peer);
    }

    @Override
    public void onPeerDisconnected(final Node peer) {
        super.onPeerDisconnected(peer);
        Log.d(TAG, "onPeerDisconnected: " + peer);
    }
}
