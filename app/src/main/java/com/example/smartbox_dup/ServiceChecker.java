package com.example.smartbox_dup;

import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.IBinder;
import android.util.Log;

import androidx.annotation.Nullable;
import androidx.core.app.NotificationCompat;

import com.example.smartbox_dup.screen.login.LoginActivity;
import com.example.smartbox_dup.utils.GlobalApplication;


public class ServiceChecker extends Service {

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        // 서비스는 언제든지 시스템에 의해 종료될 수 있다.
        // 이러한 상황에 대비하여 우아한 재시작 루틴을 설계해야 한다.
        // 설계 시 선택할 수 있는 옵션은 onStartCommand 함수의 반환값을 통해 결정된다.
        // 어떤것을 반환하느냐에 따라 재시작 될 때 onStartCommand가 호출되는 방식이 달라진다.

        // START_NOT_STICKY: 다시 만들지 않는다.
        // START_STICKTY: 서비스를다시 만들지만, 마지막 Intent를 onStartCommand의 인자로 다시 전달하지 않는다.
        // START_REDELIVER_INTENT: 마지막 intent를 onStartCommand의 인자로 다시 전달해준다.
        Log.i("this", "onStartCommand");
        this.generateForegroundNotification();

        if(!GlobalApplication.getContext().isServiceRunningCheck(SampleForegroundService.class)) {
            Intent sampleForegroundService = new Intent(this, SampleForegroundService.class);
            GlobalApplication.getContext().startService(sampleForegroundService);
        }

        stopSelf();

        return START_NOT_STICKY;
    }

    public void generateForegroundNotification() {
        createNotificationChannel("service_checker_channel");
        Intent intent = new Intent(this, LoginActivity.class);
        intent.setAction(Intent.ACTION_MAIN);
        intent.addCategory(Intent.CATEGORY_LAUNCHER);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        PendingIntent pendingIntent = PendingIntent.getActivity(this, 0, intent, PendingIntent.FLAG_IMMUTABLE);

        NotificationCompat.Builder builder = new NotificationCompat.Builder(this, "service_channel")
                .setSmallIcon(R.drawable.logo)
                .setPriority(NotificationCompat.PRIORITY_LOW)
                .setCategory(NotificationCompat.CATEGORY_SERVICE)
                .setContentText("this is service checker")
                .setContentIntent(pendingIntent);
//                .setFullScreenIntent(fullScreenPendingIntent, true);

        startForeground(1, builder.build());
    }

    public void createNotificationChannel(String channelId) {
        // Create the NotificationChannel, but only on API 26+ because
        // the NotificationChannel class is new and not in the support library
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            if(!GlobalApplication.getContext().isNotificationChannelEnabled(channelId));
            GlobalApplication.getContext().createNotificationChannel(channelId, NotificationManager.IMPORTANCE_LOW);
        }
    }
}
