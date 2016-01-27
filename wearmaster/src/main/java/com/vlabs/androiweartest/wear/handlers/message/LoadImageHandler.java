package com.vlabs.androiweartest.wear.handlers.message;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;

import com.google.android.gms.wearable.Asset;
import com.google.android.gms.wearable.DataMap;
import com.google.android.gms.wearable.MessageEvent;
import com.google.android.gms.wearable.PutDataMapRequest;
import com.google.android.gms.wearable.PutDataRequest;
import com.vlabs.androiweartest.R;
import com.vlabs.androiweartest.connection.ConnectionManager;
import com.vlabs.wearcontract.WearDataEvent;
import com.vlabs.wearcontract.dummy.DummyWearStation;
import com.vlabs.wearcontract.messages.LoadImageMessage;

import java.io.ByteArrayOutputStream;

public class LoadImageHandler implements MessageHandler {

    private final Context mContext;
    private final ConnectionManager mConnectionManager;

    public LoadImageHandler(final Context context, final ConnectionManager connectionManager) {
        mContext = context;
        mConnectionManager = connectionManager;
    }

    @Override
    public void handle(final MessageEvent messageEvent) {
        final DataMap map = DataMap.fromByteArray(messageEvent.getData());
        LoadImageMessage loadImageMessage = new LoadImageMessage(map);
        sendImage(loadImageMessage);
    }

    private int imageByPath(final String path) {
        switch (path) {
            case DummyWearStation.DUMMY_IMAGE_PATH_1:
                return R.drawable.dummy_station_1;
            case DummyWearStation.DUMMY_IMAGE_PATH_2:
                return R.drawable.dummy_station_2;
            case DummyWearStation.DUMMY_IMAGE_PATH_3:
                return R.drawable.dummy_station_3;
            case DummyWearStation.DUMMY_IMAGE_PATH_4:
                return R.drawable.dummy_station_4;
            case DummyWearStation.DUMMY_IMAGE_PATH_5:
                return R.drawable.dummy_station_5;
            default:
                return R.drawable.test_image_to_send;
        }
    }

    private void sendImage(final LoadImageMessage message) {
        final Bitmap bitmap = BitmapFactory.decodeResource(mContext.getResources(), imageByPath(message.imagePath()));
        final Bitmap scaledBitmap = Bitmap.createScaledBitmap(bitmap, message.width(), message.height(), false);
        final Asset asset = createAssetFromBitmap(scaledBitmap);

        final PutDataMapRequest dataMap = PutDataMapRequest.create(WearDataEvent.PATH_IMAGE_LOADED);
        dataMap.getDataMap().putAsset(WearDataEvent.KEY_IMAGE_ASSET, asset);
        dataMap.getDataMap().putString(WearDataEvent.KEY_IMAGE_PATH, message.imagePath());
        final PutDataRequest request = dataMap.asPutDataRequest();
        mConnectionManager.putData(request);
    }

    private static Asset createAssetFromBitmap(Bitmap bitmap) {
        final ByteArrayOutputStream byteStream = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.PNG, 100, byteStream);
        return Asset.createFromBytes(byteStream.toByteArray());
    }
}
