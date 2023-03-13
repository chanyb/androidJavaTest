package com.example.smartbox_dup.screen.function.adbshell;

import android.Manifest;
import android.content.ContentResolver;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.provider.CalendarContract;
import android.util.Log;
import android.widget.Button;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import com.example.smartbox_dup.R;


public class AdbShellTest extends AppCompatActivity {

    ConstraintLayout lo_touchpad;
    DrawingView drawingView;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        setContentView(R.layout.activity_function_adbshell);
        init();
        super.onCreate(savedInstanceState);
    }

    private void init() {
        lo_touchpad = findViewById(R.id.lo_touchpad);
        drawingView = new DrawingView(this);
        lo_touchpad.addView(drawingView);
    }
}
