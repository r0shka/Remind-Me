package com.example.videoreminder;

import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.util.Log;

import androidx.core.app.NotificationCompat;
import androidx.navigation.NavDeepLinkBuilder;

import com.example.videoreminder.db.entity.Task;
import com.example.videoreminder.ui.MainActivity;

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
    private int taskType;
    private int smallIcon;

    @Override
    public void onReceive(Context context, Intent intent) {
        notificationManager = (NotificationManager)
                context.getSystemService(Context.NOTIFICATION_SERVICE);

        taskId = intent.getLongExtra("taskId", 0);
        taskTitle = intent.getStringExtra("taskTitle");
        taskDescription = intent.getStringExtra("taskDescription");
        taskType = intent.getIntExtra("taskType", 0);
        Log.i("=====Alarm receiver:", "Task id:"+taskId);
        // Deliver the notification.
        setSmallIcon();
        deliverNotification(context);
    }

    /**
     * Builds and delivers the notification.
     *
     * @param context, activity context.
     */
    private void deliverNotification(Context context) {
        // Create the content intent for the notification, which launches
        // this activity
        Intent contentIntent = new Intent(context, MainActivity.class);

        PendingIntent contentPendingIntent = PendingIntent.getActivity
                (context, NOTIFICATION_ID, contentIntent, PendingIntent
                        .FLAG_UPDATE_CURRENT);

        Bundle bundle = new Bundle();
        bundle.putLong("id", taskId);
        PendingIntent deeplinkPendingIntent = new NavDeepLinkBuilder(context)
                .setGraph(R.navigation.nav_graph)
                .setDestination(R.id.detailsFragment)
                .setArguments(bundle)
                .createPendingIntent();
        // Build the notification
        NotificationCompat.Builder builder = new NotificationCompat.Builder
                (context, PRIMARY_CHANNEL_ID)
                .setSmallIcon(smallIcon)
                .setContentTitle(taskTitle)
                .setContentText(taskDescription)
                .setContentIntent(deeplinkPendingIntent)
                .setPriority(NotificationCompat.PRIORITY_HIGH)
                .setAutoCancel(true)
                .setDefaults(NotificationCompat.DEFAULT_ALL);

        // Deliver the notification
        notificationManager.notify(NOTIFICATION_ID, builder.build());
    }

    private void setSmallIcon(){
        switch (taskType){
            case Task.VIDEO_TYPE_TASK:
                smallIcon = R.drawable.ic_videocam_white_36dp; break;
            case Task.AUDIO_TYPE_TASK:
                smallIcon = R.drawable.ic_audio_white_36dp; break;
            case Task.TEXT_TYPE_TASK:
                smallIcon = R.drawable.ic_text_fields_white_36dp; break;
            default:
                smallIcon = R.drawable.ic_videocam_white_36dp;  break;

        }
    }
}
