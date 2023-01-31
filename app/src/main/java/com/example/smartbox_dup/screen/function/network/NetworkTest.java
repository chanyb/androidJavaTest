package com.example.smartbox_dup.screen.function.network;

import android.app.Activity;
import android.app.ActivityManager;
import android.app.NotificationManager;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.widget.Button;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.example.smartbox_dup.R;
import com.example.smartbox_dup.screen.function.notification.NotificationService;

import chanyb.android.java.GlobalApplcation;

public class NetworkTest extends AppCompatActivity {
    private Button btn_1;
    private String channelId;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        setContentView(R.layout.activity_function_network);
        init();
        super.onCreate(savedInstanceState);
    }

    private void init() {
        btn_1 = findViewById(R.id.btn_1);
        btn_1.setOnClickListener((view) -> {
            btn_1_action();
        });
    }



    private void btn_1_action() {
        Log.i("this", "btn_1_action()");
    }

}
