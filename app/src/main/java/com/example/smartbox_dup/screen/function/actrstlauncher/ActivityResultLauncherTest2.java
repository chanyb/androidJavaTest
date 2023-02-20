package com.example.smartbox_dup.screen.function.actrstlauncher;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.example.smartbox_dup.R;

public class ActivityResultLauncherTest2 extends AppCompatActivity {
    private Button btn_1;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_function_actrstlauncher2);
        init();
    }

    private void init() {
        btn_1 = findViewById(R.id.btn_1);
        btn_1.setOnClickListener((view) -> {
            btn_1_click_event();
        });
    }

    private void btn_1_click_event() {
        Intent intent = new Intent(this, ActivityResultLauncherTest.class);
        intent.putExtra("result","resultData");
        setResult(RESULT_OK, intent);
        finish();
    }

}