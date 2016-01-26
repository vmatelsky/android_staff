package com.vlabs.androiweartest;

import android.app.Notification;
import android.app.PendingIntent;
import android.content.Intent;
import android.graphics.Bitmap;
import android.support.v4.app.NotificationManagerCompat;
import android.support.v4.content.ContextCompat;

import com.google.android.gms.common.data.FreezableUtils;
import com.google.android.gms.wearable.DataEvent;
import com.google.android.gms.wearable.DataEventBuffer;
import com.google.android.gms.wearable.DataMap;
import com.google.android.gms.wearable.MessageEvent;
import com.google.android.gms.wearable.WearableListenerService;
import com.vlabs.androiweartest.activity.notification.NotificationActivity;
import com.vlabs.androiweartest.activity.showMore.ShowMoreActivity;
import com.vlabs.androiweartest.events.data.OnStations;
import com.vlabs.wearcontract.WearMessage;
import com.vlabs.wearcontract.WearDataEvent;
import com.vlabs.wearcontract.dataevent.AssetLoadedEvent;

import java.util.List;

import javax.inject.Inject;

import de.greenrobot.event.EventBus;

public class WearListenerService extends WearableListenerService {

    private static final int NOTIFICATION_ID = 1;

    @Inject
    EventBus mEventBus;

    private void postNotificationActivity() {
        final Intent viewIntent = new Intent(this, NotificationActivity.class);
        final PendingIntent viewPendingIntent = PendingIntent.getActivity(this, 0, viewIntent, 0);

        Notification page = new Notification.Builder(this).extend(
                new Notification.WearableExtender()
                        .setBackground(defaultBackground())
                        .setCustomSizePreset(Notification.WearableExtender.SIZE_FULL_SCREEN)
                        .setDisplayIntent(PendingIntent.getActivity(this, 0, new Intent(this, ShowMoreActivity.class), PendingIntent.FLAG_UPDATE_CURRENT))
        ).build();

        Notification myFullScreenNotification = new Notification.Builder(this)
                .setPriority(Notification.PRIORITY_MAX)
                .setSmallIcon(R.mipmap.ic_launcher)
                .setContentTitle(getString(R.string.app_name))
                .setAutoCancel(false)
                .extend(new Notification.WearableExtender()
                                .setBackground(defaultBackground())
                                .setCustomSizePreset(Notification.WearableExtender.SIZE_FULL_SCREEN)
                                .setDisplayIntent(viewPendingIntent)
                .addPage(page))
                .build();

        NotificationManagerCompat.from(this).notify(NOTIFICATION_ID, myFullScreenNotification);
    }

    private Bitmap defaultBackground() {
        final int color = ContextCompat.getColor(this, R.color.notification_background);
        return Bitmap.createBitmap(new int[]{color}, 1, 1, Bitmap.Config.ARGB_8888);
    }

    @Override
    public void onCreate() {
        super.onCreate();
        WearApplication.instance().appComponent().inject(this);
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
        postIfNotNull(event);

        postNotificationActivity();
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
                default:
                    if (isAssetEvent(event)) {
                        eventBusEvent = new AssetLoadedEvent(event);
                    } else {
                        eventBusEvent = null;
                    }
                }

                postIfNotNull(eventBusEvent);
            }
        }

    private boolean isAssetEvent(final DataEvent event) {
        if (event.getType() == DataEvent.TYPE_CHANGED) {
            byte[] bytes = event.getDataItem().getData();
            if (bytes.length > 0) {
                final DataMap dataMap = DataMap.fromByteArray(bytes);
                return dataMap.containsKey(AssetLoadedEvent.KEY_IMAGE_ASSET);
            }
        }
        return false;
    }

    private void postIfNotNull(final Object event) {
        if (event != null) {
            mEventBus.post(event);
        }
    }

}
