package com.vlabs.androiweartest;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.Log;

import com.google.android.gms.common.data.FreezableUtils;
import com.google.android.gms.wearable.Asset;
import com.google.android.gms.wearable.DataEvent;
import com.google.android.gms.wearable.DataEventBuffer;
import com.google.android.gms.wearable.DataMap;
import com.google.android.gms.wearable.MessageEvent;
import com.google.android.gms.wearable.Node;
import com.google.android.gms.wearable.PutDataMapRequest;
import com.google.android.gms.wearable.WearableListenerService;
import com.vlabs.wearcontract.Data;
import com.vlabs.wearcontract.Message;

import java.io.ByteArrayOutputStream;
import java.util.List;

import rx.functions.Action1;

public class MasterListenerService extends WearableListenerService {

    public static final String TAG = MainActivity.TAG;

    @Override
    public void onCreate() {
        super.onCreate();
        MasterApplication.instance().messageManager().onMessageByPath(Message.PATH_LOAD_IMAGE).subscribe(new Action1<MessageEvent>() {
            @Override
            public void call(final MessageEvent messageEvent) {
                final DataMap map = DataMap.fromByteArray(messageEvent.getData());
                uploadImageToWear(map);
            }
        });
    }

    private void uploadImageToWear(final DataMap map) {
        final String path = map.getString(Message.KEY_LOAD_IMAGE_KEY);
        final int width = map.getInt(Message.KEY_LOAD_WIDTH);
        final int height = map.getInt(Message.KEY_LOAD_HEIGHT);
        sendImage(path);
    }

    private void sendImage(final String path) {
        final Bitmap bitmap = BitmapFactory.decodeResource(getResources(), R.drawable.test_image_to_send);
        final Asset asset = createAssetFromBitmap(bitmap);
        final PutDataMapRequest dataMap = PutDataMapRequest.create(path);

        dataMap.getDataMap().putAsset(Data.KEY_IMAGE_ASSET, asset);
        dataMap.setUrgent();

        MasterApplication.instance().connectionManager().putData(dataMap);
    }

    private static Asset createAssetFromBitmap(Bitmap bitmap) {
        final ByteArrayOutputStream byteStream = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.PNG, 100, byteStream);
        return Asset.createFromBytes(byteStream.toByteArray());
    }

    @Override
    public void onMessageReceived(final MessageEvent messageEvent) {
        super.onMessageReceived(messageEvent);
        Log.d(TAG, "onMessageReceived: " + messageEvent.getPath());
        MasterApplication.instance().messageManager().handleMessage(messageEvent);
    }

    @Override
    public void onDataChanged(final DataEventBuffer dataEvents) {
        super.onDataChanged(dataEvents);

        final List<DataEvent> events = FreezableUtils.freezeIterable(dataEvents);
        for (DataEvent event : events) {
            MasterApplication.instance().messageManager().handleData(event);
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
