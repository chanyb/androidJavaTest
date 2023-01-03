package com.example.smartbox_dup.screen.function.calendarapp;

import android.os.Bundle;
import android.widget.Button;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.example.smartbox_dup.R;

public class CalendarTest extends AppCompatActivity {

    Button btn_1;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        setContentView(R.layout.activity_function_calendarapp);
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

    }
}
