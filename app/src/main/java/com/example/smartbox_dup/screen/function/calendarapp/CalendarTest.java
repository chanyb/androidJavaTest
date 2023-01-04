package com.example.smartbox_dup.screen.function.calendarapp;

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
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import com.example.smartbox_dup.R;

import chanyb.android.java.GlobalApplcation;

public class CalendarTest extends AppCompatActivity {

    private static final int PROJECTION_ID_INDEX = 0;
    private static final int PROJECTION_ACCOUNT_NAME_INDEX = 1;
    private static final int PROJECTION_DISPLAY_NAME_INDEX = 2;
    private static final int PROJECTION_OWNER_ACCOUNT_INDEX = 3;

    public static final String[] CALENDAR_PROJECTION = new String[] {
            CalendarContract.Calendars._ID,                           // 0
            CalendarContract.Calendars.ACCOUNT_NAME,                  // 1
            CalendarContract.Calendars.CALENDAR_DISPLAY_NAME,         // 2
            CalendarContract.Calendars.OWNER_ACCOUNT,                  // 3
            CalendarContract.Calendars.NAME
    };

    public static final String[] EVENT_PROJECTION = new String[] {
            CalendarContract.Events.CALENDAR_ID,                           // 0
            CalendarContract.Calendars.ACCOUNT_NAME,                  // 1
            CalendarContract.Calendars.CALENDAR_DISPLAY_NAME,         // 2
            CalendarContract.Calendars.OWNER_ACCOUNT,                  // 3
            CalendarContract.Calendars.NAME
    };

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
        String[] permissions = {
                Manifest.permission.READ_CALENDAR, Manifest.permission.WRITE_CALENDAR
        };

        int CAMERA_PERMISSION = ContextCompat.checkSelfPermission(GlobalApplcation.getContext(), permissions[0]);

        if(CAMERA_PERMISSION == PackageManager.PERMISSION_DENIED) {
            ActivityCompat.requestPermissions(GlobalApplcation.currentActivity, permissions, 0);
        } else {
            // Run query
            Cursor cur = null;
            ContentResolver cr = getContentResolver();
            Uri uri = CalendarContract.Calendars.CONTENT_URI;
            // Submit the query and get a Cursor object back.
            cur = cr.query(uri, EVENT_PROJECTION, null, null, null);

            while (cur.moveToNext()) {
                long calID = 0;
                String displayName = null;
                String accountName = null;
                String ownerName = null;
                String name = null;

                calID = cur.getLong(PROJECTION_ID_INDEX);
                displayName = cur.getString(PROJECTION_DISPLAY_NAME_INDEX);
                accountName = cur.getString(PROJECTION_ACCOUNT_NAME_INDEX);
                ownerName = cur.getString(PROJECTION_OWNER_ACCOUNT_INDEX);
                name = cur.getString(4);

                Log.i("this", calID + " / " + displayName + " / " + accountName + " / " + ownerName + " / " + name);
            }
        }
    }
}
