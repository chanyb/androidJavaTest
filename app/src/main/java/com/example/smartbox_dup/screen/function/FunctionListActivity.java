package com.example.smartbox_dup.screen.function;

import android.os.Bundle;
import android.widget.Button;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.example.smartbox_dup.R;
import com.example.smartbox_dup.screen.function.alarm.AlarmCreateActivity;
import com.example.smartbox_dup.utils.ActivitySwitchManager;

public class FunctionListActivity extends AppCompatActivity {

    Button btn_1;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_function);
        init();
    }

    private void init() {
        btn_1 = findViewById(R.id.btn_1);
        btn_1.setOnClickListener((view) -> {
            ActivitySwitchManager.getInstance().changeActivity(this, AlarmCreateActivity.class, false);
        });

    }
}
