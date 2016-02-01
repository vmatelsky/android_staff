package com.vlabs.androiweartest;

import com.google.android.gms.common.data.FreezableUtils;
import com.google.android.gms.wearable.DataEvent;
import com.google.android.gms.wearable.DataEventBuffer;
import com.google.android.gms.wearable.DataMap;
import com.google.android.gms.wearable.MessageEvent;
import com.google.android.gms.wearable.WearableListenerService;
import com.vlabs.androiweartest.activity.notification.NotificationPresenter;
import com.vlabs.androiweartest.events.data.OnAssetLoaded;
import com.vlabs.androiweartest.events.data.OnStations;
import com.vlabs.wearcontract.WearDataEvent;
import com.vlabs.wearcontract.WearMessage;

import java.util.List;

import javax.inject.Inject;

import de.greenrobot.event.EventBus;

public class WearListenerService extends WearableListenerService {

    @Inject
    EventBus mEventBus;

    NotificationPresenter mPresenter;

    @Override
    public void onCreate() {
        super.onCreate();
        WearApplication.instance().appComponent().inject(this);
        mPresenter = new NotificationPresenter(this);
    }

    @Override
    public void onMessageReceived(MessageEvent messageEvent) {
        final String path = messageEvent.getPath();

        Object event = null;
        for (WearMessage wearMessage : WearMessage.values()) {
            if (wearMessage.path().equals(path)) {
                event = wearMessage.toDomain(DataMap.fromByteArray(messageEvent.getData()));
                break;
            }
        }

        mPresenter.displayIfRemoved();
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
                case WearDataEvent.PATH_STATIONS_MY_STATIONS:
                case WearDataEvent.PATH_STATIONS_FOR_YOU:
                case WearDataEvent.PATH_STATIONS_SEARCH:
                    eventBusEvent = OnStations.fromDataEvent(event, path);
                    break;
                case WearDataEvent.PATH_IMAGE_LOADED:
                    eventBusEvent = OnAssetLoaded.fromDataEvent(event);
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
