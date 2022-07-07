package com.example.smartbox_dup.utils;

import org.json.JSONException;
import org.json.JSONObject;

import java.time.LocalDate;
import java.time.LocalTime;


public class DatetimeManager {
    private static DatetimeManager instance = new DatetimeManager();
    private DatetimeManager () {}

    public static DatetimeManager getInstance() {
        return instance;
    }

    public JSONObject getSystemDateTime() {
        JSONObject obj = new JSONObject();
        LocalDate localDate = LocalDate.now();
        LocalTime localTime = LocalTime.now();

        try {
            obj.put("datetime", localDate + " " + localTime);
            obj.put("date", String.valueOf(localDate));
            obj.put("time", String.valueOf(localTime));
            obj.put("year", String.valueOf(localDate.getYear()));
            obj.put("month", String.valueOf(localDate.getMonthValue()));
            obj.put("day", String.valueOf(localDate.getDayOfMonth()));
            obj.put("hour", String.valueOf(localTime.getHour()));
            obj.put("minute", String.valueOf(localTime.getMinute()));
            obj.put("second", String.valueOf(localTime.getSecond()));
        } catch (JSONException e) {
            e.printStackTrace();
        }

        return obj;
    }
}
