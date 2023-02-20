package com.example.smartbox_dup.screen.function.actrstlauncher;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.widget.Button;

import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.fragment.app.FragmentTransaction;

import com.example.smartbox_dup.R;
import com.example.smartbox_dup.screen.function.gallary.PhotoFragment;

import java.util.List;

public class ActivityResultLauncherTest extends AppCompatActivity {
    private Button btn_1;

    ActivityResultLauncher<Intent> mStartForResult = registerForActivityResult(
            new ActivityResultContracts.StartActivityForResult(),
            result -> {
                if(result.getResultCode() == RESULT_OK) {
                    Intent intent = result.getData();
                    Log.i("this", intent.getStringExtra("result"));
                }
            }
    );

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_function_actrstlauncher);
        init();
    }

    private void init() {
        btn_1 = findViewById(R.id.btn_1);
        btn_1.setOnClickListener((view) -> {
            btn_1_click_event();
        });
    }

    private void btn_1_click_event() {
        Intent intent = new Intent(this, ActivityResultLauncherTest2.class);
        intent.putExtra("key", "value");
        mStartForResult.launch(intent);
    }

}