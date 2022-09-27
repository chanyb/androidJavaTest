package com.example.smartbox_dup.screen.function.alarm;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.SystemClock;
import android.widget.Button;
import android.widget.EditText;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.NotificationCompat;

import com.example.smartbox_dup.R;

import chanyb.android.java.GlobalApplcation;
import chanyb.android.java.ToastManager;

public class AlarmCreateActivity extends AppCompatActivity {

    EditText edt_hour, edt_minute;
    Button btn_1, btn_2;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_function_alarm);
        init();
        createAlarmServiceChannel();

        Intent duplicated = new Intent();
    }

    private void init() {
        edt_hour = findViewById(R.id.edt_hour);
        edt_minute = findViewById(R.id.edt_minute);
        btn_1 = findViewById(R.id.btn_1);
        btn_1.setOnClickListener((view) -> {
            ToastManager.getInstance().showToast(this, "알람 등록");
            int hour = Integer.parseInt(edt_hour.getText().toString());
            int minute = Integer.parseInt(edt_minute.getText().toString());

            Alarm alarm = new Alarm();
            alarm.setHour(hour);
            alarm.setMinute(minute);
            alarm.setTitle("알람33");

            alarm.schedule(13);
        });

        btn_2 = findViewById(R.id.btn_2);
        btn_2.setOnClickListener((view) -> {
            ToastManager.getInstance().showToast(this, "등록한 알람 취소");
            AlarmManager alarmManager = (AlarmManager) GlobalApplcation.getContext().getSystemService(Context.ALARM_SERVICE);

            Intent target = new Intent(GlobalApplcation.getContext(), AlarmBroadcastReceiver.class);
            target.setAction("com.smartbox_dup.actionTest");
            PendingIntent targetPending = PendingIntent.getBroadcast(GlobalApplcation.getContext(), 13, target, PendingIntent.FLAG_UPDATE_CURRENT | PendingIntent.FLAG_MUTABLE);
            alarmManager.cancel(targetPending);
        });
    }

    private void createAlarmServiceChannel() {
        GlobalApplcation.getContext().createNotificationChannel(GlobalApplcation.getContext().getString(R.string.alarm_service_channel), NotificationCompat.PRIORITY_MAX);
    }

}
