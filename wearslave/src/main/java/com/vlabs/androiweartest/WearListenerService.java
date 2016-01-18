package com.vlabs.androiweartest;

import com.google.android.gms.common.data.FreezableUtils;
import com.google.android.gms.wearable.DataEvent;
import com.google.android.gms.wearable.DataEventBuffer;
import com.google.android.gms.wearable.MessageEvent;
import com.google.android.gms.wearable.WearableListenerService;
import com.vlabs.wearmanagers.message.MessageManager;

import java.util.List;

import javax.inject.Inject;

public class WearListenerService extends WearableListenerService {

    @Inject
    MessageManager mMessageManager;

    @Override
    public void onMessageReceived(MessageEvent messageEvent) {
        WearApplication.instance().appComponent().inject(this);
        mMessageManager.handleMessage(messageEvent);
    }

    @Override
    public void onDataChanged(final DataEventBuffer dataEvents) {
        super.onDataChanged(dataEvents);

        final List<DataEvent> events = FreezableUtils.freezeIterable(dataEvents);
        for (DataEvent event : events) {
            mMessageManager.handleData(event);
        }
    }

}
