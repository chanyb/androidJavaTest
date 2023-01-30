package com.example.smartbox_dup.screen.function.sensor;

import android.Manifest;
import android.content.ContentResolver;
import android.content.ContentUris;
import android.content.ContentValues;
import android.content.Context;
import android.content.pm.ActivityInfo;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.net.Uri;
import android.os.Bundle;
import android.provider.CalendarContract;
import android.util.Log;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import com.example.smartbox_dup.R;
import com.example.smartbox_dup.utils.DeviceRotationManager;

import java.util.Calendar;

import chanyb.android.java.GlobalApplcation;

public class SensorTest extends AppCompatActivity {
    private static final int RADIAN_TO_DEGREE = -57;
    private SensorManager sensorManager;
    private Sensor sensor;
    private SensorEventListener sensorEventListener;
    private Button btn_1, btn_2;
    private TextView txt_x, txt_y, txt_z;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        setContentView(R.layout.activity_function_sensor);
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

        txt_x = findViewById(R.id.txt_x);
        txt_y = findViewById(R.id.txt_y);
        txt_z = findViewById(R.id.txt_z);
    }



    private void btn_1_action() {
        DeviceRotationManager.getInstance().registerListener(this,new DeviceRotationManager.DeviceRotateListener() {
            @Override
            public void onPortrait() {
                Log.i("this", "onPortrait");
            }

            @Override
            public void onPortraitReverse() {
                Log.i("this", "onPortraitReverse");
            }

            @Override
            public void onLandscape() {
                Log.i("this", "onLandscape");
            }

            @Override
            public void onLandscapeReverse() {
                Log.i("this", "onLandscapeReverse");
            }
        });
    }

    private void btn_2_action() {}

}
