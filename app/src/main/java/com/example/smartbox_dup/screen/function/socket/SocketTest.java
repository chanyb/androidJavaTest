package com.example.smartbox_dup.screen.function.socket;

import android.os.Bundle;
import android.widget.Button;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.example.smartbox_dup.R;

public class SocketTest extends AppCompatActivity {
    Button btn_1, btn_2, btn_3;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        setContentView(R.layout.activity_function_socket);
        init();
        super.onCreate(savedInstanceState);
    }

    private void init() {
        btn_1 = findViewById(R.id.btn_1);
        btn_1.setOnClickListener((view) -> {
            TCPClient client = new TCPClient("183.107.16.241", 8000);
            client.connect();
        });

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
