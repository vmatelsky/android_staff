package com.vlabs.androiweartest;

import android.util.Log;

import com.google.android.gms.wearable.DataEventBuffer;
import com.google.android.gms.wearable.MessageEvent;
import com.google.android.gms.wearable.Node;
import com.google.android.gms.wearable.WearableListenerService;
import com.vlabs.androiweartest.wear.WearFacade;

import java.util.List;

public class MasterListenerService extends WearableListenerService {

    public static final String TAG = MainActivity.TAG;

    private WearFacade mFacade;

    @Override
    public void onCreate() {
        super.onCreate();
        mFacade = MasterApplication.instance().wearFacade();
    }

    @Override
    public void onMessageReceived(final MessageEvent messageEvent) {
        super.onMessageReceived(messageEvent);
        mFacade.handleMessageEvent(messageEvent);
    }

    @Override
    public void onDataChanged(final DataEventBuffer dataEvents) {
        super.onDataChanged(dataEvents);
        mFacade.handleDataEvents(dataEvents);
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
