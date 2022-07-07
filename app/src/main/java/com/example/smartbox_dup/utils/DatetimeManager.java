package com.example.smartbox_dup.utils;

import org.json.JSONException;
import org.json.JSONObject;

import java.time.LocalDate;
import java.time.LocalTime;
import java.time.ZoneId;
import java.time.ZonedDateTime;



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
            obj.put("datetime", localDate + "T" + localTime);
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

    public JSONObject getUTCDateTime() {
        JSONObject obj = new JSONObject();
        ZonedDateTime zonedDateTime = ZonedDateTime.now().withZoneSameInstant(ZoneId.of("UTC"));
        try {
            obj.put("datetime", String.valueOf(zonedDateTime));
            obj.put("date", zonedDateTime.getYear() + "-" + (String.valueOf(zonedDateTime.getMonthValue()).length()==1 ? "0"+zonedDateTime.getMonthValue():zonedDateTime.getMonthValue()) + "-" + (String.valueOf(zonedDateTime.getDayOfMonth()).length()==1 ? "0"+zonedDateTime.getDayOfMonth():zonedDateTime.getDayOfMonth()));
            obj.put("time", (String.valueOf(zonedDateTime.getHour()).length()==1 ? "0"+zonedDateTime.getHour():zonedDateTime.getHour()) + ":" + (String.valueOf(zonedDateTime.getMinute()).length()==1 ? "0"+zonedDateTime.getMinute():zonedDateTime.getMinute()) + ":" + (String.valueOf(zonedDateTime.getSecond()).length()==1 ? "0"+zonedDateTime.getSecond():zonedDateTime.getSecond()));
            obj.put("year", String.valueOf(zonedDateTime.getYear()));
            obj.put("month", String.valueOf(zonedDateTime.getMonthValue()));
            obj.put("day", String.valueOf(zonedDateTime.getDayOfMonth()));
            obj.put("hour", String.valueOf(zonedDateTime.getHour()));
            obj.put("minute", String.valueOf(zonedDateTime.getMinute()));
            obj.put("second", String.valueOf(zonedDateTime.getSecond()));
        } catch (Exception e) {
            e.printStackTrace();
        }

        return obj;
    }
}
