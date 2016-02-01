package com.vlabs.androiweartest.activity.notification;

import android.app.Notification;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.support.annotation.NonNull;
import android.support.v4.app.NotificationManagerCompat;
import android.support.v4.content.ContextCompat;
import android.support.v4.media.MediaBrowserCompat;

import com.vlabs.androiweartest.R;
import com.vlabs.androiweartest.activity.showMore.ShowMoreActivity;

public class NotificationPresenter {

    private static final int NOTIFICATION_ID = 1;

    private final Context mContext;

    public NotificationPresenter(final Context context) {
        mContext = context;
    }

    public void displayIfRemoved() {
        if (isNotificationDisplayed()) return;

        postNotificationActivity();
    }

    private boolean isNotificationDisplayed() {
        return NotificationActivity.isNotificationActivityActive;
    }

    private void postNotificationActivity() {
        final Intent viewIntent = new Intent(mContext, NotificationActivity.class);
        final PendingIntent viewPendingIntent = PendingIntent.getActivity(mContext, 0, viewIntent, PendingIntent.FLAG_UPDATE_CURRENT);

        Notification page = showMorePage(PendingIntent.FLAG_UPDATE_CURRENT);

        Notification myFullScreenNotification = new Notification.Builder(mContext)
                .setPriority(Notification.PRIORITY_MAX)
                .setSmallIcon(R.mipmap.ic_launcher)
                .setContentTitle(mContext.getString(R.string.app_name))
                .setAutoCancel(false)
                .extend(new Notification.WearableExtender()
                                .setBackground(defaultBackground())
                                .setCustomSizePreset(Notification.WearableExtender.SIZE_FULL_SCREEN)
                                .setDisplayIntent(viewPendingIntent)
                                .addPage(page))
                .build();

        NotificationManagerCompat.from(mContext).notify(NOTIFICATION_ID, myFullScreenNotification);
    }

    private Notification showMorePage(@MediaBrowserCompat.MediaItem.Flags int intentFlags) {
        return new Notification.Builder(mContext).extend(
                    new Notification.WearableExtender()
                            .setBackground(defaultBackground())
                            .setCustomSizePreset(Notification.WearableExtender.SIZE_FULL_SCREEN)
                            .setDisplayIntent(PendingIntent.getActivity(mContext, 0, showMoreActivityIntent(), intentFlags))
            ).build();
    }

    @NonNull
    private Intent showMoreActivityIntent() {
        return new Intent(mContext, ShowMoreActivity.class);
    }

    private Bitmap defaultBackground() {
        final int color = ContextCompat.getColor(mContext, R.color.notification_background);
        return Bitmap.createBitmap(new int[]{color}, 1, 1, Bitmap.Config.ARGB_8888);
    }

}
