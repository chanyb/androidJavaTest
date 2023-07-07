package com.example.smartbox_dup.screen.function.alarm;

import android.app.Notification;
import android.app.PendingIntent;
import android.app.Service;
import android.bluetooth.BluetoothAdapter;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Build;
import android.os.IBinder;
import android.os.PowerManager;
import android.util.Log;

import androidx.annotation.Nullable;
import androidx.core.app.NotificationCompat;

import com.example.smartbox_dup.R;
import com.example.smartbox_dup.screen.intro.IntroActivity;
import com.example.smartbox_dup.utils.ByteArrayManager;
import com.example.smartbox_dup.utils.GlobalApplication;


public class AlarmService extends Service {
    Alarm alarm;
    Uri ringtone;
    PowerManager powerManager;
    boolean isScreenOn;
    private BluetoothAdapter mBluetoothAdapter;
    private int ID=2;
    private ByteArrayManager<Alarm> bcTransformer;

    @Override
    public void onCreate() {
        super.onCreate();
        ringtone = RingtoneManager.getActualDefaultRingtoneUri(this.getBaseContext(), RingtoneManager.TYPE_ALARM);
        powerManager = (PowerManager) getSystemService(Context.POWER_SERVICE);
        isScreenOn = powerManager.isInteractive();
        bcTransformer = new ByteArrayManager<>();
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        try {
            byte[] bytes = intent.getByteArrayExtra("alarm");
            alarm = bcTransformer.getClassFromByteArray(bytes);

//            Intent notificationIntent;
//            notificationIntent = new Intent(this, FullScreenAlarmActivity.class);
//            notificationIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
//            notificationIntent.putExtra("isScreenOn", isScreenOn);
//            notificationIntent.putExtra("alarm", bytes);
//
//            if(GlobalApplication.getContext().isForeground()) { // foreground면, 현재 Activity 기억하기
//                if (GlobalApplication.currentActivity != null)
//                    notificationIntent.putExtra("activity", GlobalApplication.currentActivity.getClass().getName());
//            }

            Intent introIntent = new Intent(this, IntroActivity.class);


            PendingIntent pendingIntent = null;
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
                pendingIntent = PendingIntent.getActivity(this, 0, introIntent, PendingIntent.FLAG_UPDATE_CURRENT | PendingIntent.FLAG_MUTABLE);
            } else {
                pendingIntent = PendingIntent.getActivity(this, 0, introIntent, PendingIntent.FLAG_UPDATE_CURRENT);
            }

            String alarmTitle = "";
            if (alarm != null) {
                alarmTitle = alarm.getTitle();
            }

            Notification notification = new NotificationCompat.Builder(this, GlobalApplication.getContext().getString(R.string.alarm_service_channel))
                    .setContentText(alarmTitle)
                    .setSmallIcon(R.drawable.logo)
                    .setSound(null)
                    .setOngoing(false)
                    .setAutoCancel(true)
                    .setCategory(NotificationCompat.CATEGORY_ALARM)
                    .setVisibility(NotificationCompat.VISIBILITY_PUBLIC)
                    .setDefaults(NotificationCompat.DEFAULT_ALL)
                    .setPriority(NotificationCompat.PRIORITY_HIGH)
                    .setFullScreenIntent(pendingIntent, true)
                    .build();


            startForeground(ID, notification);
//            startActivity(notificationIntent);

            int status = getPackageManager().getComponentEnabledSetting(new ComponentName(getPackageName(), getPackageName()+".screen.intro.IntroActivity"));
            Log.i("this", "status: " + status);
            try{
                if(status == PackageManager.COMPONENT_ENABLED_STATE_ENABLED) {
                    getPackageManager().setComponentEnabledSetting(new ComponentName(getPackageName(), getPackageName()+".screen.intro.IntroActivity"), PackageManager.COMPONENT_ENABLED_STATE_DISABLED,PackageManager.DONT_KILL_APP);
                    getPackageManager().setComponentEnabledSetting(new ComponentName(getPackageName(), getPackageName()+".screen.intro.IntroActivityAlias"), PackageManager.COMPONENT_ENABLED_STATE_ENABLED, PackageManager.DONT_KILL_APP);
                }
                else {
                    getPackageManager().setComponentEnabledSetting(new ComponentName(getPackageName(), getPackageName()+".screen.intro.IntroActivity"), PackageManager.COMPONENT_ENABLED_STATE_ENABLED,PackageManager.DONT_KILL_APP);
                    getPackageManager().setComponentEnabledSetting(new ComponentName(getPackageName(), getPackageName()+".screen.intro.IntroActivityAlias"), PackageManager.COMPONENT_ENABLED_STATE_DISABLED, PackageManager.DONT_KILL_APP);
                }
            } catch (Exception e) {
                Log.e("this", "setComponentEnabledSetting Error", e);
            }


        } catch (Exception e) {
            e.printStackTrace();
        }

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
        return null;
    }
}
