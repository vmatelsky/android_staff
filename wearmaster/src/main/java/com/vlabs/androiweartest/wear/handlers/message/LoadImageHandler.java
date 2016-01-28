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
import com.vlabs.androiweartest.wear.connection.ConnectionManager;
import com.vlabs.wearcontract.PlayStationData;
import com.vlabs.wearcontract.WearDataEvent;
import com.vlabs.wearcontract.WearStation;
import com.vlabs.wearcontract.dummy.DummyWearStation;
import com.vlabs.wearcontract.messages.LoadImageMessage;

import java.io.ByteArrayOutputStream;

import rx.Observable;
import rx.subjects.PublishSubject;

public class LoadImageHandler implements MessageHandler {

    private final Context mContext;
    private final ConnectionManager mConnectionManager;

    public LoadImageHandler(final Context context, final ConnectionManager connectionManager) {
        mContext = context;
        mConnectionManager = connectionManager;
    }

    final PublishSubject<LoadImageMessage> onLoadImage = PublishSubject.create();

    @Override
    public void handle(final MessageEvent messageEvent) {
        final DataMap map = DataMap.fromByteArray(messageEvent.getData());
        LoadImageMessage loadImageMessage = new LoadImageMessage(map);
        onLoadImage.onNext(loadImageMessage);
    }

    public Observable<LoadImageMessage> onLoadImage() {
        return onLoadImage;
    }
}
