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
            CalendarContract.Events.CALENDAR_DISPLAY_NAME,
            CalendarContract.Events.TITLE,
            CalendarContract.Events.EVENT_LOCATION,
            CalendarContract.Events.DTSTART, // 시작 datetime
            CalendarContract.Events.DTEND, // 끝 datetime
            CalendarContract.Events.HAS_ALARM, // 알람 존재여부 1/0
            CalendarContract.Events.DURATION, // P1D, P3600S, null
            CalendarContract.Events.EVENT_TIMEZONE, // UTC, Asia/Seoul, ...
            CalendarContract.Events.EVENT_END_TIMEZONE, // null
            CalendarContract.Events.STATUS, // STATUS_TENTATIVE(0), STATUSCONFIRMED(1), STATUS_CANCELED(2)
            CalendarContract.Events.ALL_DAY, // isAllDay 1/0
            CalendarContract.Events.LAST_DATE, // 일정이 끝난 시간을 나타내는 timestamp 1672790400000
            CalendarContract.Events.RDATE, // null
            CalendarContract.Events.RRULE // FREQ=YEARLY;COUNT=10;INTERVAL=1;WKST=SU;BYMONTHDAY=27;BYMONTH=1
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
            Uri uri = CalendarContract.Events.CONTENT_URI;
            // Submit the query and get a Cursor object back.
            cur = cr.query(uri, EVENT_PROJECTION, null, null, null);

            while (cur.moveToNext()) {


                String displayName = cur.getString(0);
                String title = cur.getString(1);
                String location = cur.getString(2);
                String dtStart = cur.getString(3);
                String dtEnd = cur.getString(4);
                int hasAlarm = cur.getInt(5);
                String duration = cur.getString(6);
                String eventTimeZone = cur.getString(7);
                String eventEndTimeZone = cur.getString(8);
                int status = cur.getInt(9);
                int allDay = cur.getInt(10);
                long lastDate = cur.getLong(11);
                String rDate = cur.getString(12);
                String rRule = cur.getString(13);
                Log.i("this", displayName + " / " + title + " / " + location + " / " + dtStart + " / " + dtEnd + " / " + hasAlarm + " / " + duration + " / " + eventTimeZone + " / " + eventEndTimeZone + " / " + status + " / " + allDay + " / " + lastDate + " / " + rDate + " / " + rRule);
            }
        }
    }
}
