package com.example.smartbox_dup.screen.function.alarm;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.SystemClock;

import com.example.smartbox_dup.utils.ByteArrayManager;

import java.io.Serializable;
import java.util.Calendar;

import chanyb.android.java.GlobalApplcation;

public class Alarm implements Serializable {
    private String title;
    private int hour, minute;

    public void setHour(int hour) {
        this.hour = hour;
    }

    public void setMinute(int minute) {
        this.minute = minute;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public int getHour() {
        return hour;
    }

    public int getMinute() {
        return minute;
    }

    public String getTitle() {
        return title;
    }

    public void schedule(int id) {
        AlarmManager alarmManager = (AlarmManager) GlobalApplcation.getContext().getSystemService(Context.ALARM_SERVICE);
        ByteArrayManager<Alarm> bcTransformer = new ByteArrayManager<>();
        Intent myIntent = new Intent(GlobalApplcation.getContext(), AlarmBroadcastReceiver.class);
        myIntent.putExtra("alarm", bcTransformer.getByteArrayFromClassObject(this));

        myIntent.setAction("com.smartbox_dup.actionTest");

        PendingIntent alarmPendingIntent = null;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
            alarmPendingIntent = PendingIntent.getBroadcast(GlobalApplcation.getContext(), id, myIntent,PendingIntent.FLAG_UPDATE_CURRENT | PendingIntent.FLAG_MUTABLE);
        } else {
            alarmPendingIntent = PendingIntent.getBroadcast(GlobalApplcation.getContext(), id, myIntent, PendingIntent.FLAG_UPDATE_CURRENT);
        }


        Calendar calendar = Calendar.getInstance();
        calendar.setTimeInMillis(System.currentTimeMillis());
        calendar.set(Calendar.HOUR_OF_DAY, hour);
        calendar.set(Calendar.MINUTE, minute);
        calendar.set(Calendar.SECOND, 0);
        calendar.set(Calendar.MILLISECOND, 0);

        if (calendar.getTimeInMillis() <= System.currentTimeMillis()) {
            calendar.set(Calendar.DAY_OF_MONTH, calendar.get(Calendar.DAY_OF_MONTH) + 1);
        }

        alarmManager.setRepeating(AlarmManager.ELAPSED_REALTIME_WAKEUP, SystemClock.elapsedRealtime(), 60000, alarmPendingIntent);
//        alarmManager.setAlarmClock(new AlarmManager.AlarmClockInfo(calendar.getTimeInMillis(), alarmPendingIntent), alarmPendingIntent);

    }
}
