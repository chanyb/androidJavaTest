package com.example.smartbox_dup.screen.function.volley;

import android.os.Bundle;
import android.util.Log;
import android.widget.Button;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.example.smartbox_dup.R;

import org.json.JSONObject;

import chanyb.android.java.RequestManager;

public class VolleyTest extends AppCompatActivity {
    private Button btn_1, btn_2, btn_3, btn_4;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_function_volley);
        init();
    }

    private void init() {
        btn_1 = findViewById(R.id.btn_1);
        btn_1.setOnClickListener((view) -> {
            btn_1_action();
        });

        btn_2 = findViewById(R.id.btn_2);
        btn_2.setOnClickListener((view) -> {

        });

        btn_3 = findViewById(R.id.btn_3);
        btn_3.setOnClickListener((view) -> {

        });

        btn_4 = findViewById(R.id.btn_4);
        btn_4.setOnClickListener((view) -> {

        });
    }

    private void btn_1_action() {
        JSONObject obj = new JSONObject();
        RequestManager.Listener listener = new RequestManager.Listener() {
            @Override
            public void onSuccess(Object response) {
                JSONObject obj = (JSONObject) response;
                Log.i("this", obj.toString());
            }

            @Override
            public void onError(String errStr) {
                Log.i("this", errStr);
            }
        };
        RequestManager.getInstance().addRequest(RequestManager.POST, "http://192.168.0.43:9001/user/test", obj, listener);
    }
}
