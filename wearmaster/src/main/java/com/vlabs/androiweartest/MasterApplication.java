package com.vlabs.androiweartest;

import android.app.Application;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.Log;

import com.google.android.gms.wearable.Asset;
import com.google.android.gms.wearable.DataMap;
import com.google.android.gms.wearable.MessageEvent;
import com.google.android.gms.wearable.PutDataMapRequest;
import com.vlabs.wearcontract.Data;
import com.vlabs.wearcontract.Message;
import com.vlabs.wearcontract.dummy.DummyWearStation;
import com.vlabs.wearmanagers.connection.ConnectionManager;
import com.vlabs.wearmanagers.connection.ConnectionManagerImpl;
import com.vlabs.wearmanagers.message.MessageManager;
import com.vlabs.wearmanagers.message.MessageManagerImpl;

import java.io.ByteArrayOutputStream;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;

import rx.functions.Action1;

public class MasterApplication extends Application {

    public static final String TAG = MainActivity.TAG;

    private static MasterApplication sInstance;

    private MessageManager mMessageManager;

    private ConnectionManager mConnectionManager;

    public static MasterApplication instance() {
        return sInstance;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        sInstance = this;

        mMessageManager = new MessageManagerImpl();
        mConnectionManager = new ConnectionManagerImpl(this);

        MasterApplication.instance().messageManager().onMessageByPath(Message.PATH_LOAD_IMAGE).subscribe(new Action1<MessageEvent>() {
            @Override
            public void call(final MessageEvent messageEvent) {
                final DataMap map = DataMap.fromByteArray(messageEvent.getData());
                uploadImageToWear(map);
            }
        });

        MasterApplication.instance().messageManager().onMessageByPath(Message.PATH_SEARCH).subscribe(new Action1<MessageEvent>() {
            @Override
            public void call(final MessageEvent messageEvent) {
                final DataMap map = DataMap.fromByteArray(messageEvent.getData());
                final String searchTerm = map.getString(Message.KEY_SEARCH_TERM);
                Log.d(TAG, "search by term: " + searchTerm);
                // TODO: perform real search
                deliverBestResult();
            }
        });
    }

    public MessageManager messageManager() {
        return mMessageManager;
    }

    public ConnectionManager connectionManager() {
        return mConnectionManager;
    }



    private void deliverBestResult() {
        final PutDataMapRequest putRequest = PutDataMapRequest.create(Data.PATH_STATIONS_SEARCH);
        ArrayList<DataMap> mapArray = new ArrayList<>();
        mapArray.add(DummyWearStation.Dummy1.toMap());
        putRequest.getDataMap().putDataMapArrayList(Data.KEY_STATIONS, mapArray);
        putRequest.setUrgent();
        MasterApplication.instance().connectionManager().deleteData(putRequest);
        MasterApplication.instance().connectionManager().putData(putRequest);
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

}
