package com.example.smartbox_dup;

import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;

import androidx.annotation.NonNull;
import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;
import androidx.work.Worker;
import androidx.work.WorkerParameters;

import com.example.smartbox_dup.screen.login.LoginActivity;

public class WakeupWorker extends Worker {

    Context context;

    public WakeupWorker(@NonNull Context context, @NonNull WorkerParameters workerParams) {
        super(context, workerParams);
        this.context = context;
    }

    @NonNull
    @Override
    public Result doWork() {
        return Result.success();
    }

    public void fullscreenIntent() {
        Intent fullScreenIntent = new Intent(context, LoginActivity.class);
        PendingIntent fullScreenPendingIntent = PendingIntent.getActivity(context, 0, fullScreenIntent, PendingIntent.FLAG_IMMUTABLE);


        // 화면 띄워서 클릭시 앱으로 이동 또는 전체화면 노티 표시
        NotificationCompat.Builder builder = new NotificationCompat.Builder(context, context.getString(R.string.channel_name))
                .setSmallIcon(R.drawable.logo)
                .setContentTitle("smartbox_dup")
                .setContentText("Much longer text that cannot fit one line..")
                .setPriority(NotificationCompat.PRIORITY_HIGH)
                .setCategory(NotificationCompat.CATEGORY_ALARM)
                .setFullScreenIntent(fullScreenPendingIntent, true);

        NotificationManagerCompat notificationManager = NotificationManagerCompat.from(context);
        notificationManager.notify(1, builder.build());
    }
}
