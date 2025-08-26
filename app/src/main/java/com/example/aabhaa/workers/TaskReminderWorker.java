package com.example.aabhaa.workers;

import android.content.Context;
import android.content.pm.PackageManager;
import android.os.Build;

import androidx.annotation.NonNull;
import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;
import androidx.core.content.ContextCompat;
import androidx.work.Worker;
import androidx.work.WorkerParameters;

import com.example.aabhaa.R;

public class TaskReminderWorker extends Worker {

    public TaskReminderWorker(@NonNull Context context, @NonNull WorkerParameters params) {
        super(context, params);
    }

    @NonNull
    @Override
    public Result doWork() {
        String title = getInputData().getString("task_title");
        String description = getInputData().getString("task_description");

        // Only send notification if permission is granted
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU &&
                ContextCompat.checkSelfPermission(getApplicationContext(), android.Manifest.permission.POST_NOTIFICATIONS)
                        != PackageManager.PERMISSION_GRANTED) {
            return Result.failure();
        }

        NotificationCompat.Builder builder = new NotificationCompat.Builder(getApplicationContext(), "task_reminder_channel")
                .setSmallIcon(R.drawable.ic_duration)
                .setContentTitle(title)
                .setContentText(description)
                .setPriority(NotificationCompat.PRIORITY_HIGH)
                .setAutoCancel(true);

        NotificationManagerCompat manager = NotificationManagerCompat.from(getApplicationContext());
        manager.notify((int) System.currentTimeMillis(), builder.build());

        return Result.success();
    }
}
