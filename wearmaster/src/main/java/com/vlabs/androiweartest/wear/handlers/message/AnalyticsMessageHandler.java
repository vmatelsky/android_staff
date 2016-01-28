package com.vlabs.androiweartest.wear.handlers.message;

import com.google.android.gms.wearable.DataMap;
import com.google.android.gms.wearable.MessageEvent;
import com.vlabs.wearcontract.messages.AnalyticsMessage;

import rx.Observable;
import rx.subjects.PublishSubject;

public class AnalyticsMessageHandler implements MessageHandler {

    final PublishSubject<AnalyticsMessage> mOnReceived = PublishSubject.create();

    @Override
    public void handle(final MessageEvent messageEvent) {
        final DataMap map = DataMap.fromByteArray(messageEvent.getData());
        final AnalyticsMessage message = new AnalyticsMessage(map);
        mOnReceived.onNext(message);
    }

    public Observable<AnalyticsMessage> onReceived() {
        return mOnReceived;
    }
}
