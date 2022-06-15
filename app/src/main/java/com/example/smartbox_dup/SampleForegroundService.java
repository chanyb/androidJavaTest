package com.example.smartbox_dup;

import android.app.PendingIntent;
import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
import android.util.Log;

import androidx.annotation.Nullable;
import androidx.core.app.NotificationCompat;

import com.example.smartbox_dup.screen.login.LoginActivity;

public class SampleForegroundService extends Service {

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

        return super.onStartCommand(intent, flags, startId);
    }

    public void generateForegroundNotification() {
        Intent fullScreenIntent = new Intent(this, LoginActivity.class);
        PendingIntent fullScreenPendingIntent = PendingIntent.getActivity(this, 0, fullScreenIntent, PendingIntent.FLAG_IMMUTABLE);

        NotificationCompat.Builder builder = new NotificationCompat.Builder(this, this.getString(R.string.channel_name))
                .setSmallIcon(R.drawable.logo)
                .setContentTitle("smartbox_dup")
                .setContentText("Much longer text that cannot fit one line..")
                .setPriority(NotificationCompat.PRIORITY_HIGH)
                .setCategory(NotificationCompat.CATEGORY_ALARM)
                .setFullScreenIntent(fullScreenPendingIntent, true);

        startForeground(1, builder.build());
    }
}
