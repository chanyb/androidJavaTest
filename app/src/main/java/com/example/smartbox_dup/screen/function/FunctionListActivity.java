package com.example.smartbox_dup.screen.function;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.database.Cursor;
import android.location.Location;
import android.location.LocationManager;
import android.net.Uri;
import android.os.Bundle;
import android.provider.Settings;
import android.util.Log;
import android.widget.Button;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.example.smartbox_dup.R;
import com.example.smartbox_dup.location.GoogleLocationManger;
import com.example.smartbox_dup.screen.function.alarm.AlarmCreateActivity;
import com.example.smartbox_dup.sqlite.MyDBHandler;
import com.example.smartbox_dup.utils.ActivitySwitchManager;
import com.example.smartbox_dup.utils.FutureTaskRunner;
import com.google.android.gms.tasks.Task;

import java.util.concurrent.atomic.AtomicReference;

import chanyb.android.java.GlobalApplcation;
import chanyb.android.java.ToastManager;

public class FunctionListActivity extends AppCompatActivity {

    Button btn_1, btn_2, btn_3, btn_4, btn_5, btn_6;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_function);
        init();
    }

    private void init() {
        btn_1 = findViewById(R.id.btn_1);
        btn_1.setOnClickListener((view) -> {
            startActivity(new Intent(this, AlarmCreateActivity.class));
        });

        btn_2 = findViewById(R.id.btn_2);
        btn_2.setOnClickListener((view) -> {
            GoogleLocationManger.getInstance().getUserLocation(res -> {
                Location location = (Location) res;
                runOnUiThread(() -> {
                    btn_2.setText(location.toString());
                });
                return null;
            });
        });

        btn_3 = findViewById(R.id.btn_3);
        btn_3.setOnClickListener((view) -> {
            MyDBHandler dbHandler = new MyDBHandler(GlobalApplcation.getContext(), "smartbox_dup.db");
            dbHandler.insert("bbbb", 20, "010");
            Cursor cursor = dbHandler.select("student","aaaaa");
            cursor.moveToFirst();
            do {
                Log.i("this", cursor.getString(1));
            } while(cursor.moveToNext());
        });

        btn_4 = findViewById(R.id.btn_4);
        btn_4.setOnClickListener((view) -> {
        });

        btn_5 = findViewById(R.id.btn_5);
        btn_5.setOnClickListener((view) -> {
        });

        btn_6 = findViewById(R.id.btn_6);
        btn_6.setOnClickListener((view) -> {
        });
    }
}
