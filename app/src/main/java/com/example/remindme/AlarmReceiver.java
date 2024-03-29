package com.example.remindme;

import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;

import androidx.core.app.NotificationCompat;
import androidx.navigation.NavDeepLinkBuilder;



public class AlarmReceiver extends BroadcastReceiver {

    private NotificationManager notificationManager;
    // Notification ID.
    private static final int NOTIFICATION_ID = 0;
    // Notification channel ID.
    private static final String PRIMARY_CHANNEL_ID =
            "primary_notification_channel";

    private long taskId;
    private String taskTitle;
    private String taskDescription;

    @Override
    public void onReceive(Context context, Intent intent) {
        notificationManager = (NotificationManager)
                context.getSystemService(Context.NOTIFICATION_SERVICE);
        taskId = intent.getLongExtra("taskId", 0);
        taskTitle = intent.getStringExtra("taskTitle");
        taskDescription = intent.getStringExtra("taskDescription");

        deliverNotification(context);
    }

    /**
     * Builds and delivers the notification.
     *
     * @param context, activity context.
     */
    private void deliverNotification(Context context) {
        Bundle bundle = new Bundle();
        bundle.putLong("id", taskId);
        PendingIntent deeplinkPendingIntent = new NavDeepLinkBuilder(context)
                .setGraph(R.navigation.nav_graph)
                .setDestination(R.id.detailsFragment)
                .setArguments(bundle)
                .createPendingIntent();
        NotificationCompat.Builder builder = new NotificationCompat.Builder
                (context, PRIMARY_CHANNEL_ID)
                .setSmallIcon(R.drawable.ic_notifications_white_24dp)
                .setContentTitle(taskTitle)
                .setContentText(taskDescription)
                .setContentIntent(deeplinkPendingIntent)
                .setPriority(NotificationCompat.PRIORITY_HIGH)
                .setAutoCancel(true)
                .setDefaults(NotificationCompat.DEFAULT_ALL);

        notificationManager.notify(NOTIFICATION_ID, builder.build());
    }

}
