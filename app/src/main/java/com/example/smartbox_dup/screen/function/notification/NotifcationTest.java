package com.example.smartbox_dup.screen.function.notification;

import android.app.Activity;
import android.app.ActivityManager;
import android.app.NotificationManager;
import android.content.Context;
import android.content.Intent;
import android.hardware.Sensor;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.example.smartbox_dup.R;
import com.example.smartbox_dup.utils.DeviceRotationManager;

import chanyb.android.java.GlobalApplcation;

public class NotifcationTest extends AppCompatActivity {
    private Button btn_1, btn_2;
    private String channelId;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        setContentView(R.layout.activity_function_notification);
        init();
        super.onCreate(savedInstanceState);
    }

    private void init() {
        btn_1 = findViewById(R.id.btn_1);
        btn_1.setOnClickListener((view) -> {
            btn_1_action();
        });

        btn_2 = findViewById(R.id.btn_2);
        btn_2.setOnClickListener((view) -> {
            btn_2_action();
        });

        channelId = "hello";
        createNotificationChannel(channelId);
    }



    private void btn_1_action() {
        Log.i("this", "btn_1_action()");
        Log.i("this", "service start: " + serviceStart(this, NotificationService.class));
    }

    public boolean serviceStart(Context mContext, Class aClass) {
        boolean chk = false;
        try {
            if (!isServiceRunningCheck(aClass)) {

            }
            chk = true;
        } catch (Exception e) {
            Log.i("this", "exception!!: " + e);
        }

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            Log.i("this", "startForegroundService");
            mContext.startForegroundService(new Intent(mContext, aClass));
        } else {
            mContext.startService(new Intent(mContext, aClass));
        }

        return chk;
    }

    public boolean isServiceRunningCheck(Class serviceClass) {
        ActivityManager manager = (ActivityManager) getSystemService(Activity.ACTIVITY_SERVICE);
        for (ActivityManager.RunningServiceInfo service : manager.getRunningServices(Integer.MAX_VALUE)) {
            if (serviceClass.getName().equals(service.service.getClassName())) {
                return true;
            }
        }
        return false;
    }

    private void createNotificationChannel(String channelId) {
        // Create the NotificationChannel, but only on API 26+ because
        // the NotificationChannel class is new and not in the support library
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            if (!GlobalApplcation.getContext().isNotificationChannelEnabled(channelId)) ;
            GlobalApplcation.getContext().createNotificationChannel(channelId, NotificationManager.IMPORTANCE_LOW);
        }
    }

    private void btn_2_action() {
        stopService(new Intent(GlobalApplcation.getContext(), NotificationService.class));
    }

}
