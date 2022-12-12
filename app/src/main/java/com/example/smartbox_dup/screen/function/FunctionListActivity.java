package com.example.smartbox_dup.screen.function;

import android.content.Intent;
import android.location.Location;
import android.os.Bundle;
import android.widget.Button;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.example.smartbox_dup.R;
import com.example.smartbox_dup.location.GoogleLocationManger;
import com.example.smartbox_dup.screen.function.alarm.AlarmCreateActivity;
import com.example.smartbox_dup.screen.function.camera.CameraTest;
import com.example.smartbox_dup.screen.function.draw.DrawTest;
import com.example.smartbox_dup.screen.function.fragment.FragmentActivity;
import com.example.smartbox_dup.screen.function.intent.IntentTest;
import com.example.smartbox_dup.screen.function.location.LocationTest;
import com.example.smartbox_dup.screen.function.socket.SocketTest;
import com.example.smartbox_dup.screen.function.sqlite.SQLiteTest;
import com.example.smartbox_dup.screen.function.volley.VolleyTest;

public class FunctionListActivity extends AppCompatActivity {

    Button btn_1, btn_2, btn_3, btn_4, btn_5, btn_6, btn_7, btn_8, btn_9, btn_10;

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

    }
}
