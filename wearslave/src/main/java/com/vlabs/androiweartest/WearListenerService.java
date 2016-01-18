package com.vlabs.androiweartest;

import android.util.Log;

import com.google.android.gms.common.data.FreezableUtils;
import com.google.android.gms.wearable.DataEvent;
import com.google.android.gms.wearable.DataEventBuffer;
import com.google.android.gms.wearable.MessageEvent;
import com.google.android.gms.wearable.WearableListenerService;
import com.vlabs.androiweartest.events.data.OnStations;
import com.vlabs.androiweartest.events.message.OnFeedback;
import com.vlabs.androiweartest.events.message.OnPlayerAction;
import com.vlabs.androiweartest.events.message.OnState;
import com.vlabs.wearcontract.Data;
import com.vlabs.wearcontract.Message;
import com.vlabs.wearmanagers.message.MessageManager;

import java.util.List;

import javax.inject.Inject;

import de.greenrobot.event.EventBus;

public class WearListenerService extends WearableListenerService {

    @Inject
    MessageManager mMessageManager;

    @Inject
    EventBus mEventBus;

    @Override
    public void onCreate() {
        super.onCreate();
        WearApplication.instance().appComponent().inject(this);
    }

    @Override
    public void onMessageReceived(MessageEvent messageEvent) {
        final String path = messageEvent.getPath();

        final Object event;
        switch (path) {
            case Message.PATH_STATE:
                event = OnState.fromMessageEvent(messageEvent);
                break;
            case Message.PATH_CONTROL:
                event = OnPlayerAction.fromMessageEvent(messageEvent);
                break;
            case Message.PATH_FEEDBACK:
                event = OnFeedback.fromMessageEvent(messageEvent);
                break;
            default:
                Log.d(WearListenerService.class.getSimpleName(), "unknown message: " + path);
                event = null;
        }

        postIfNotNull(event);
    }

    @Override
    public void onDataChanged(final DataEventBuffer dataEvents) {
        super.onDataChanged(dataEvents);

        final List<DataEvent> events = FreezableUtils.freezeIterable(dataEvents);

        for (DataEvent event : events) {
            final String path = event.getDataItem().getUri().getPath();
            final Object eventBusEvent;

            switch (path) {
                case Data.PATH_STATIONS_MY_STATIONS:
                case Data.PATH_STATIONS_FOR_YOU:
                case Data.PATH_STATIONS_SEARCH:
                    eventBusEvent = OnStations.fromDataEvent(event, path);
                    break;
                default:
                    eventBusEvent = null;
            }

            postIfNotNull(eventBusEvent);
        }
    }

    private void postIfNotNull(final Object event) {
        if (event != null) {
            mEventBus.post(event);
        }
    }

}
