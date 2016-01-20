package com.vlabs.androiweartest;

import android.app.Notification;
import android.app.PendingIntent;
import android.content.Intent;
import android.graphics.Bitmap;
import android.support.v4.app.NotificationManagerCompat;
import android.util.Log;

import com.google.android.gms.common.data.FreezableUtils;
import com.google.android.gms.wearable.DataEvent;
import com.google.android.gms.wearable.DataEventBuffer;
import com.google.android.gms.wearable.MessageEvent;
import com.google.android.gms.wearable.WearableListenerService;
import com.vlabs.androiweartest.activity.notification.NotificationActivity;
import com.vlabs.androiweartest.activity.showMore.ShowMoreActivity;
import com.vlabs.androiweartest.events.data.OnStations;
import com.vlabs.androiweartest.events.message.OnFeedback;
import com.vlabs.androiweartest.events.message.OnPlayerAction;
import com.vlabs.androiweartest.events.message.OnPlayerState;
import com.vlabs.wearcontract.Data;
import com.vlabs.wearcontract.Message;
import com.vlabs.wearmanagers.message.MessageManager;

import java.util.List;

import javax.inject.Inject;

import de.greenrobot.event.EventBus;

public class WearListenerService extends WearableListenerService {

    private static final int NOTIFICATION_ID = 1;

    @Inject
    MessageManager mMessageManager;

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
        return Bitmap.createBitmap(new int[]{getResources().getColor(R.color.notification_background)}, 1, 1, Bitmap.Config.ARGB_8888);
    }

    @Override
    public void onCreate() {
        super.onCreate();
        WearApplication.instance().appComponent().inject(this);
    }

    @Override
    public void onMessageReceived(MessageEvent messageEvent) {
        final String path = messageEvent.getPath();

        postNotificationActivity();

        final Object event;
        switch (path) {
            case Message.PATH_STATE:
                event = OnPlayerState.fromMessageEvent(messageEvent);
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
