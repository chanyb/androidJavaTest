package com.example.smartbox_dup.screen.function.ble;

import android.os.Bundle;
import android.widget.Button;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.example.smartbox_dup.R;
import com.example.smartbox_dup.screen.function.socket.TCPClient;

public class BleTest extends AppCompatActivity {
    Button btn_1, btn_2, btn_3;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        setContentView(R.layout.activity_function_ble);
        init();
        super.onCreate(savedInstanceState);
    }

    private void init() {
        btn_1 = findViewById(R.id.btn_1);
        btn_1.setOnClickListener((view) -> btn_1_action());

        btn_2 = findViewById(R.id.btn_2);
        btn_2.setOnClickListener((view) ->{
        });

        btn_3 = findViewById(R.id.btn_3);
        btn_3.setOnClickListener((view) -> {
        });
    }

    private void btn_1_action() {

    }
}
