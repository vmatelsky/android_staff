package com.vlabs.androiweartest;

import android.app.Application;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.Log;

import com.google.android.gms.wearable.Asset;
import com.google.android.gms.wearable.DataMap;
import com.google.android.gms.wearable.MessageEvent;
import com.google.android.gms.wearable.PutDataMapRequest;
import com.google.android.gms.wearable.PutDataRequest;
import com.vlabs.wearcontract.WearDataEvent;
import com.vlabs.wearcontract.WearMessage;
import com.vlabs.wearcontract.dataevent.AssetLoadedEvent;
import com.vlabs.wearcontract.dummy.DummyWearStation;
import com.vlabs.androiweartest.connection.ConnectionManager;
import com.vlabs.androiweartest.connection.ConnectionManagerImpl;
import com.vlabs.wearcontract.messages.LoadImageMessage;
import com.vlabs.wearcontract.messages.SearchMessage;
import com.vlabs.wearmanagers.message.MessageManager;
import com.vlabs.wearmanagers.message.MessageManagerImpl;

import java.io.ByteArrayOutputStream;
import java.util.ArrayList;

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

        MasterApplication.instance().messageManager().onMessageByPath(WearMessage.LOAD_IMAGE.path()).subscribe(new Action1<MessageEvent>() {
            @Override
            public void call(final MessageEvent messageEvent) {
                final DataMap map = DataMap.fromByteArray(messageEvent.getData());
                LoadImageMessage loadImageMessage = new LoadImageMessage(map);
                sendImage(loadImageMessage.imagePath());
            }
        });

        MasterApplication.instance().messageManager().onMessageByPath(WearMessage.SEARCH.path()).subscribe(new Action1<MessageEvent>() {
            @Override
            public void call(final MessageEvent messageEvent) {
                final DataMap map = DataMap.fromByteArray(messageEvent.getData());
                SearchMessage message = new SearchMessage(map);
                final String searchTerm = message.term();
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
        final PutDataMapRequest putRequest = PutDataMapRequest.create(WearDataEvent.PATH_STATIONS_SEARCH);
        ArrayList<DataMap> mapArray = new ArrayList<>();
        mapArray.add(DummyWearStation.Dummy1.toMap());
        putRequest.getDataMap().putDataMapArrayList(WearDataEvent.KEY_STATIONS, mapArray);
        putRequest.setUrgent();
        MasterApplication.instance().connectionManager().deleteData(putRequest);
        MasterApplication.instance().connectionManager().putData(putRequest.asPutDataRequest());
    }

    private void sendImage(final String path) {
        final Bitmap bitmap = BitmapFactory.decodeResource(getResources(), R.drawable.test_image_to_send);
        final Asset asset = createAssetFromBitmap(bitmap);

        final PutDataMapRequest dataMap = PutDataMapRequest.create(WearDataEvent.PATH_IMAGE_LOADED);
        dataMap.getDataMap().putAsset(AssetLoadedEvent.KEY_IMAGE_ASSET, asset);
        dataMap.getDataMap().putString(AssetLoadedEvent.KEY_IMAGE_PATH, path);
        final PutDataRequest request = dataMap.asPutDataRequest();
        MasterApplication.instance().connectionManager().putData(request);
    }

    private static Asset createAssetFromBitmap(Bitmap bitmap) {
        final ByteArrayOutputStream byteStream = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.PNG, 100, byteStream);
        return Asset.createFromBytes(byteStream.toByteArray());
    }

}
