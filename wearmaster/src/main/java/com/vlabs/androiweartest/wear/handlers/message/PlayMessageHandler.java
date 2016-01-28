package com.vlabs.androiweartest.wear.handlers.message;

import com.google.android.gms.wearable.DataMap;
import com.google.android.gms.wearable.MessageEvent;
import com.vlabs.wearcontract.PlayStationData;
import com.vlabs.wearcontract.WearStation;

import rx.Observable;
import rx.subjects.PublishSubject;

public class PlayMessageHandler implements MessageHandler {

    final PublishSubject<WearStation> onChanged = PublishSubject.create();

    @Override
    public void handle(final MessageEvent messageEvent) {
        PlayStationData data = PlayStationData.fromDataMap(DataMap.fromByteArray(messageEvent.getData()));

        // TODO: tag data.playedFrom in analytics??
        onChanged.onNext(data.station);
    }

    public Observable<WearStation> onChanged() {
        return onChanged;
    }

}
