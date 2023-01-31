package com.example.smartbox_dup.screen.function.notification;

import android.app.PendingIntent;
import android.app.Service;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.IBinder;
import android.util.Log;

import androidx.annotation.Nullable;
import androidx.core.app.NotificationCompat;

import com.example.smartbox_dup.R;
import com.example.smartbox_dup.screen.login.LoginActivity;

public class NotificationService extends Service {

    /**
     * 이게 기본형이고, manifest파일에 service를 등록해주면 끝
     * 서비스는 종료하기 전까지는 종료되지 않는다.
     * 만약 종료되어도, START_STICKY를 반환했기 때문에, OS가 다시 실행해 줄 것
     */

    @Override
    public void onCreate() {
        super.onCreate();
        Log.i("this", "NotificationService - onCreate");
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        Log.i("this", "NotificationService - onStartCommand");
        generateForegroundNotification();
        return START_STICKY;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        stopForeground(true);
    }


    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        // bindService()로 호출 할 수 있다.
        return null;
    }


    public void generateForegroundNotification() {
        Intent intent = new Intent(this, LoginActivity.class);
        intent.setAction(Intent.ACTION_MAIN);
        intent.addCategory(Intent.CATEGORY_LAUNCHER);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        PendingIntent pendingIntent = PendingIntent.getActivity(this, 0, intent, PendingIntent.FLAG_IMMUTABLE);

        NotificationCompat.Builder builder = new NotificationCompat.Builder(this, "hello")
                .setSmallIcon(R.drawable.logo)
                .setPriority(NotificationCompat.PRIORITY_LOW)
                .setCategory(NotificationCompat.CATEGORY_SERVICE)
                .setContentText("foreground noti test")
                .setContentIntent(pendingIntent);
//                .setFullScreenIntent(fullScreenPendingIntent, true);

        startForeground(2, builder.build());
    }

}
