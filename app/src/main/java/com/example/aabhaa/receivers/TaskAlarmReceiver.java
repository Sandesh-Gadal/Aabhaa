package com.example.aabhaa.receivers;

import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Build;

import androidx.core.app.NotificationCompat;

import com.example.aabhaa.R;

public class TaskAlarmReceiver extends BroadcastReceiver {

    @Override
    public void onReceive(Context context, Intent intent) {
        String taskTitle = intent.getStringExtra("task_title");
        int taskId = intent.getIntExtra("task_id", 0);

        // Build notification
        NotificationManager notificationManager =
                (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);

        String channelId = "task_channel";
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            NotificationChannel channel = new NotificationChannel(channelId,
                    "Tasks", NotificationManager.IMPORTANCE_HIGH);
            notificationManager.createNotificationChannel(channel);
        }

        NotificationCompat.Builder builder = new NotificationCompat.Builder(context, channelId)
                .setContentTitle("Task Reminder")
                .setContentText(taskTitle)
                .setSmallIcon(R.drawable.ic_duration)
                .setAutoCancel(true);

        notificationManager.notify(taskId, builder.build());
    }
}
