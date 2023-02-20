package com.example.smartbox_dup.screen.function;

import android.content.Intent;
import android.location.Location;
import android.os.Bundle;
import android.widget.Button;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.example.smartbox_dup.R;
import com.example.smartbox_dup.location.GoogleLocationManger;
import com.example.smartbox_dup.screen.function.actrstlauncher.ActivityResultLauncherTest;
import com.example.smartbox_dup.screen.function.adbshell.AdbShellTest;
import com.example.smartbox_dup.screen.function.alarm.AlarmCreateActivity;
import com.example.smartbox_dup.screen.function.calendarapp.CalendarTest;
import com.example.smartbox_dup.screen.function.camera.CameraTest;
import com.example.smartbox_dup.screen.function.draw.DrawTest;
import com.example.smartbox_dup.screen.function.fingerprint.FingerPrintTest;
import com.example.smartbox_dup.screen.function.fragment.FragmentActivity;
import com.example.smartbox_dup.screen.function.gallary.GallaryTest;
import com.example.smartbox_dup.screen.function.intent.IntentTest;
import com.example.smartbox_dup.screen.function.location.LocationTest;
import com.example.smartbox_dup.screen.function.network.NetworkTest;
import com.example.smartbox_dup.screen.function.notification.NotifcationTest;
import com.example.smartbox_dup.screen.function.printonothers.PrintOnOtherAppTest;
import com.example.smartbox_dup.screen.function.sensor.SensorTest;
import com.example.smartbox_dup.screen.function.socket.SocketTest;
import com.example.smartbox_dup.screen.function.sqlite.SQLiteTest;
import com.example.smartbox_dup.screen.function.viewbinding.ViewBindingTestActivity;
import com.example.smartbox_dup.screen.function.volley.VolleyTest;
import com.example.smartbox_dup.screen.function.webview.WebviewTest;

public class FunctionListActivity extends AppCompatActivity {

    Button btn_1, btn_2, btn_3, btn_4, btn_5, btn_6, btn_7, btn_8, btn_9, btn_10, btn_11, btn_12, btn_13, btn_14, btn_15, btn_16, btn_17, btn_18, btn_19, btn_20,
            btn_21, btn_22, btn_23, btn_24, btn_25, btn_26;

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
            Intent intent = new Intent(this, SQLiteTest.class);
            startActivity(intent);
        });

        btn_4 = findViewById(R.id.btn_4);
        btn_4.setOnClickListener((view) -> {
            startActivity(new Intent(this, VolleyTest.class));
        });

        btn_5 = findViewById(R.id.btn_5);
        btn_5.setOnClickListener((view) -> {
            startActivity(new Intent(this, IntentTest.class));
        });

        btn_6 = findViewById(R.id.btn_6);
        btn_6.setOnClickListener((view) -> {
            startActivity(new Intent(this, CameraTest.class));
        });

        btn_7 = findViewById(R.id.btn_7);
        btn_7.setOnClickListener((view) -> {
            startActivity(new Intent(this, DrawTest.class));
        });

        btn_8 = findViewById(R.id.btn_8);
        btn_8.setOnClickListener((view) -> {
            startActivity(new Intent(this, FragmentActivity.class));
        });

        btn_9 = findViewById(R.id.btn_9);
        btn_9.setOnClickListener((view) -> {
            startActivity(new Intent(this, LocationTest.class));
        });

        btn_10 = findViewById(R.id.btn_10);
        btn_10.setOnClickListener((view) -> {
            startActivity(new Intent(this, SocketTest.class));
        });

        btn_11 = findViewById(R.id.btn_11);
        btn_11.setOnClickListener((view) -> {
            startActivity(new Intent(this, GallaryTest.class));
        });

        btn_12 = findViewById(R.id.btn_12);
        btn_12.setOnClickListener((view) -> {
            startActivity(new Intent(this, WebviewTest.class));
        });

        btn_13 = findViewById(R.id.btn_13);
        btn_13.setOnClickListener((view) -> {
            startActivity(new Intent(this, FingerPrintTest.class));
        });

        btn_14 = findViewById(R.id.btn_14);
        btn_14.setOnClickListener((view) -> {
            startActivity(new Intent(this, CalendarTest.class));
        });

        btn_15 = findViewById(R.id.btn_15);
        btn_15.setOnClickListener((view) -> {
            startActivity(new Intent(this, AdbShellTest.class));
        });

        btn_16 = findViewById(R.id.btn_16);
        btn_16.setOnClickListener((view) -> {
            startActivity(new Intent(this, PrintOnOtherAppTest.class));
        });

        btn_17 = findViewById(R.id.btn_17);
        btn_17.setOnClickListener((view) -> {
            startActivity(new Intent(this, ViewBindingTestActivity.class));
        });

        btn_18 = findViewById(R.id.btn_18);
        btn_18.setOnClickListener((view) -> {
            startActivity(new Intent(this, SensorTest.class));
        });

        btn_19 = findViewById(R.id.btn_19);
        btn_19.setOnClickListener((view) -> {
            startActivity(new Intent(this, NotifcationTest.class));
        });

        btn_20 = findViewById(R.id.btn_20);
        btn_20.setOnClickListener((view) -> {
            startActivity(new Intent(this, NetworkTest.class));
        });

        btn_21 = findViewById(R.id.btn_21);
        btn_21.setOnClickListener((view) -> {
            startActivity(new Intent(this, ActivityResultLauncherTest.class));
        });
    }
}
