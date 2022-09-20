package com.example.smartbox_dup.screen.function.alarm;

import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.widget.EditText;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.NotificationCompat;

import com.example.smartbox_dup.R;
import com.example.smartbox_dup.utils.GlobalApplcation;
import com.example.smartbox_dup.utils.ToastManager;

public class AlarmCreateActivity extends AppCompatActivity {

    EditText edt_hour, edt_minute;
    Button btn_1;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_function_alarm);
        init();
        createAlarmServiceChannel();
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
    }

    private void createAlarmServiceChannel() {
        GlobalApplcation.getContext().createNotificationChannel(GlobalApplcation.getContext().getString(R.string.alarm_service_channel), NotificationCompat.PRIORITY_MAX);
    }

}
