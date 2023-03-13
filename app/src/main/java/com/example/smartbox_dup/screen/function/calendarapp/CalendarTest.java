package com.example.smartbox_dup.screen.function.calendarapp;

import android.Manifest;
import android.content.ContentResolver;
import android.content.ContentUris;
import android.content.ContentValues;
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
import com.example.smartbox_dup.utils.GlobalApplication;

import java.util.Calendar;


public class CalendarTest extends AppCompatActivity {

    public static final String[] CALENDAR_PROJECTION = new String[] {
            CalendarContract.Calendars._ID,
            CalendarContract.Calendars.NAME,
            CalendarContract.Calendars.CALENDAR_DISPLAY_NAME,
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
            CalendarContract.Events.RRULE, // FREQ=YEARLY;COUNT=10;INTERVAL=1;WKST=SU;BYMONTHDAY=27;BYMONTH=1
            CalendarContract.Events.DESCRIPTION // 메모
    };

    private Button btn_0, btn_1, btn_2, btn_3;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        setContentView(R.layout.activity_function_calendarapp);
        init();
        super.onCreate(savedInstanceState);
    }

    private void init() {
        btn_0 = findViewById(R.id.btn_0);
        btn_0.setOnClickListener((view) -> btn_0_action());

        btn_1 = findViewById(R.id.btn_1);
        btn_1.setOnClickListener((view) -> {
            btn_1_action();
        });

        btn_2 = findViewById(R.id.btn_2);
        btn_2.setOnClickListener((view) -> btn_2_action());

        btn_3 = findViewById(R.id.btn_3);
        btn_3.setOnClickListener((view) -> btn_3_action());
    }

    private void btn_0_action() {
        String[] permissions = {
                Manifest.permission.READ_CALENDAR, Manifest.permission.WRITE_CALENDAR
        };

        int CAMERA_PERMISSION = ContextCompat.checkSelfPermission(GlobalApplication.getContext(), permissions[0]);

        if(CAMERA_PERMISSION == PackageManager.PERMISSION_DENIED) {
            ActivityCompat.requestPermissions(GlobalApplication.currentActivity, permissions, 0);
        } else {
            // Run query
            Cursor cur = null;
            ContentResolver cr = getContentResolver();
            Uri uri = CalendarContract.Events.CONTENT_URI;
            // Submit the query and get a Cursor object back.
            cur = cr.query(uri, CALENDAR_PROJECTION, null, null, null);

            while (cur.moveToNext()) {
                String ID = cur.getString(0);
                String NAME = cur.getString(1);
                String CALENDAR_DISPLAY_NAME = cur.getString(2);
                Log.i("this", ID + " / " + NAME + " / " + CALENDAR_DISPLAY_NAME);
            }
        }
    }

    private void btn_1_action() {
        String[] permissions = {
                Manifest.permission.READ_CALENDAR, Manifest.permission.WRITE_CALENDAR
        };

        int CAMERA_PERMISSION = ContextCompat.checkSelfPermission(GlobalApplication.getContext(), permissions[0]);

        if(CAMERA_PERMISSION == PackageManager.PERMISSION_DENIED) {
            ActivityCompat.requestPermissions(GlobalApplication.currentActivity, permissions, 0);
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

    private void btn_2_action() {
        Calendar beginTime = Calendar.getInstance();
        beginTime.set(2023, 0, 12, 0, 0);
        long startMillis = beginTime.getTimeInMillis();

        Calendar endTime = Calendar.getInstance();
        endTime.set(2023, 0, 12, 24, 00);
        long endMillis = endTime.getTimeInMillis();

        ContentResolver contentResolver = getContentResolver();
        ContentValues contentValues = new ContentValues();

        contentValues.put(CalendarContract.Events.DTSTART, startMillis);
        contentValues.put(CalendarContract.Events.DTEND, endMillis);
        contentValues.put(CalendarContract.Events.TITLE, "캘린더입력");
        contentValues.put(CalendarContract.Events.DESCRIPTION, "테스트");
        contentValues.put(CalendarContract.Events.CALENDAR_ID, 5);
        contentValues.put(CalendarContract.Events.EVENT_TIMEZONE, "Asia/Seoul");

        contentResolver.insert(CalendarContract.Events.CONTENT_URI, contentValues);
    }

    private void btn_3_action() {
        ContentResolver cr = getContentResolver();
        Uri deleteUri = null;
        deleteUri = ContentUris.withAppendedId(CalendarContract.Events.CONTENT_URI, -55);
        int rows = cr.delete(deleteUri, null, null);
        Log.i("this", "Rows deleted: " + rows);
    }
}
