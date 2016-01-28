package com.vlabs.androiweartest.wear.handlers.message;

import com.google.android.gms.wearable.DataMap;
import com.google.android.gms.wearable.MessageEvent;
import com.vlabs.wearcontract.messages.LoadImageMessage;

import rx.Observable;
import rx.subjects.PublishSubject;

public class LoadImageHandler implements MessageHandler {

    final PublishSubject<LoadImageMessage> onLoadImage = PublishSubject.create();

    @Override
    public void handle(final MessageEvent messageEvent) {
        final DataMap map = DataMap.fromByteArray(messageEvent.getData());
        final LoadImageMessage loadImageMessage = new LoadImageMessage(map);
        onLoadImage.onNext(loadImageMessage);
    }

    public Observable<LoadImageMessage> onLoadImage() {
        return onLoadImage;
    }
}
