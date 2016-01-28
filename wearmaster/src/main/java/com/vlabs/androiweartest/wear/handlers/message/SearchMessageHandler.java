package com.vlabs.androiweartest.wear.handlers.message;

import com.google.android.gms.wearable.DataMap;
import com.google.android.gms.wearable.MessageEvent;
import com.vlabs.wearcontract.messages.SearchMessage;

import rx.Observable;
import rx.subjects.PublishSubject;

public class SearchMessageHandler implements MessageHandler {

    final PublishSubject<SearchMessage> onSearchTermChanged = PublishSubject.create();

    @Override
    public void handle(final MessageEvent messageEvent) {
        final DataMap map = DataMap.fromByteArray(messageEvent.getData());
        SearchMessage message = new SearchMessage(map);

        onSearchTermChanged.onNext(message);
    }

    public Observable<SearchMessage> onSearchTermChanged() {
        return onSearchTermChanged;
    }
}
