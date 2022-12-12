package com.example.smartbox_dup.screen.function.gallary;

import android.os.Bundle;
import android.widget.Button;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.example.smartbox_dup.R;
import com.example.smartbox_dup.screen.function.socket.TCPClient;

public class GallaryTest extends AppCompatActivity {
    Button btn_1, btn_2;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_function_gallary);
    }

    private void init() {
        btn_1 = findViewById(R.id.btn_1);
        btn_1.setOnClickListener((view) -> {
        });

        btn_2 = findViewById(R.id.btn_2);
        btn_2.setOnClickListener((view) ->{
        });
    }
}