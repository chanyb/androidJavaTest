package com.example.smartbox_dup.utils;

import android.os.Build;
import android.util.Log;

import org.json.JSONException;
import org.json.JSONObject;

import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.time.Instant;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.time.format.TextStyle;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;
import java.util.TimeZone;


public class DatetimeManager {
    private static DatetimeManager instance = new DatetimeManager();
    private DatetimeManager() {}

    public static DatetimeManager getInstance() {
        return instance;
    }

    public JSONObject getSystemDateTime() {
        JSONObject obj = new JSONObject();
        LocalDateTime localDateTime = null;
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O) {
            localDateTime = LocalDateTime.now();
            LocalDate localDate = LocalDate.from(localDateTime);
            LocalTime localTime = LocalTime.from(localDateTime);
            try {
                obj.put("timestamp", Timestamp.valueOf(localDate + " " + localTime).getTime());
                obj.put("datetime", localDate + "T" + localTime);
                obj.put("date", String.valueOf(localDate));
                obj.put("time", String.valueOf(localTime));
                obj.put("year", String.valueOf(localDate.getYear()));
                obj.put("month", String.valueOf(localDate.getMonthValue()));
                obj.put("day", String.valueOf(localDate.getDayOfMonth()));
                obj.put("hour", String.valueOf(localTime.getHour()));
                obj.put("minute", String.valueOf(localTime.getMinute()));
                obj.put("second", String.valueOf(localTime.getSecond()));
                obj.put("dayOfWeek", localDate.getDayOfWeek().getDisplayName(TextStyle.SHORT, Locale.KOREAN));
            } catch (JSONException e) {
                e.printStackTrace();
            }
        } else {
            // BELOW API LEVEL 25
            Date date = new Date();
            Log.i("this", "getDate: " + date.getDate());
            Log.i("this", "getYear: " + date.getYear());
            Log.i("this", "getMonth: " + date.getMonth());
            Log.i("this", "getDay: " + date.getDay());
            Log.i("this", "getHours: " + date.getHours());
            Log.i("this", "getMinutes: " + date.getMinutes());
            Log.i("this", "getSeconds: " + date.getSeconds());

        }

        return obj;
    }

    public JSONObject getSystemDate() {
        JSONObject obj = new JSONObject();
        LocalDateTime localDateTime = null;
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O) {
            localDateTime = LocalDateTime.now();
            LocalDate localDate = LocalDate.from(localDateTime);
            try {
                obj.put("timestamp", Timestamp.valueOf(localDate + " " + "00:00:00").getTime());
                obj.put("date", String.valueOf(localDate));
                obj.put("year", String.valueOf(localDate.getYear()));
                obj.put("month", String.valueOf(localDate.getMonthValue()));
                obj.put("day", String.valueOf(localDate.getDayOfMonth()));
                obj.put("dayOfWeek", localDate.getDayOfWeek().getDisplayName(TextStyle.SHORT, Locale.KOREAN));
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }

        return obj;
    }

    public long distanceFromTodayOf(JSONObject target) {
        JSONObject today = getSystemDate();
        Long distance = 0L;
        try {
            distance = target.getLong("timestamp") - today.getLong("timestamp");
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return distance/86400000L;
    }

    public JSONObject getJSONObjectFromLocalDate(LocalDate localDate) {
        JSONObject obj = new JSONObject();

        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O) {
            try {
                obj.put("timestamp", Timestamp.valueOf(localDate + " " + "00:00:00").getTime());
                obj.put("datetime", localDate + "T" + "00:00:00");
                obj.put("date", String.valueOf(localDate));
                obj.put("time", "00:00:00");
                obj.put("year", String.valueOf(localDate.getYear()));
                obj.put("month", String.valueOf(localDate.getMonthValue()));
                obj.put("day", String.valueOf(localDate.getDayOfMonth()));
                obj.put("hour", "0");
                obj.put("minute", "0");
                obj.put("second", "0");
                obj.put("dayOfWeek", localDate.getDayOfWeek().getDisplayName(TextStyle.SHORT, Locale.KOREAN));
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }

        return obj;
    }

    public JSONObject getJSONObjectFromLocalDateTime(LocalDateTime localDate) {
        JSONObject obj = new JSONObject();

        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O) {
            try {
                String tmp = localDate.toString().replace("T", " ");
                if(tmp.length()==16) tmp = tmp + ":00";
                obj.put("timestamp", Timestamp.valueOf(tmp).getTime());
                obj.put("datetime", localDate.toString());
                obj.put("date", String.valueOf(localDate));
                obj.put("time", "00:00:00");
                obj.put("year", String.valueOf(localDate.getYear()));
                obj.put("month", String.valueOf(localDate.getMonthValue()));
                obj.put("day", String.valueOf(localDate.getDayOfMonth()));
                obj.put("hour", String.valueOf(localDate.getHour()));
                obj.put("minute", String.valueOf(localDate.getMinute()));
                obj.put("second", String.valueOf(localDate.getSecond()));
                obj.put("dayOfWeek", localDate.getDayOfWeek().getDisplayName(TextStyle.SHORT, Locale.KOREAN));
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }

        return obj;
    }

    public JSONObject getJSONObjectFromCalendar(Calendar calendar) {
        JSONObject obj = new JSONObject();
        TimeZone tz = calendar.getTimeZone();
        ZoneId zid = null;
        LocalDate localDate = null;
        LocalTime localTime = null;

        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O) {
            zid = tz == null ? ZoneId.systemDefault() : tz.toZoneId();
            localDate = LocalDateTime.ofInstant(calendar.toInstant(), zid).toLocalDate();
            localTime = LocalDateTime.ofInstant(calendar.toInstant(), zid).toLocalTime();
        }

        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O) {
            try {
                if(localTime.toString().length() == 5) obj.put("timestamp", Timestamp.valueOf(localDate + " " + localTime +":00").getTime());
                else obj.put("timestamp", Timestamp.valueOf(localDate + " " + localTime).getTime());
                obj.put("datetime", localDate + "T" + localTime);
                obj.put("date", String.valueOf(localDate));
                obj.put("time", String.valueOf(localTime));
                obj.put("year", String.valueOf(localDate.getYear()));
                obj.put("month", String.valueOf(localDate.getMonthValue()));
                obj.put("day", String.valueOf(localDate.getDayOfMonth()));
                obj.put("hour", String.valueOf(localTime.getHour()));
                obj.put("minute", String.valueOf(localTime.getMinute()));
                obj.put("second", String.valueOf(localTime.getSecond()));
                obj.put("dayOfWeek", localDate.getDayOfWeek().getDisplayName(TextStyle.SHORT, Locale.KOREAN));
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }

        return obj;
    }

    public JSONObject getPrevicousDayFrom(JSONObject specificDay) {
        JSONObject obj = new JSONObject();

        try {
            long previousDayTimestamp = (specificDay.getLong("timestamp") - 86400000L);
            LocalDateTime previousDatetime = null;
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                previousDatetime = LocalDateTime.ofInstant(Instant.ofEpochMilli(previousDayTimestamp),
                        TimeZone.getDefault().toZoneId());
            }


            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                try {
                    obj.put("timestamp", previousDayTimestamp);
                    obj.put("datetime", previousDatetime.toString());
                    obj.put("date", previousDatetime.toString().split("T")[0]);
                    obj.put("time", previousDatetime.toString().split("T")[1]);
                    obj.put("year", String.valueOf(previousDatetime.getYear()));
                    obj.put("month", String.valueOf(previousDatetime.getMonthValue()));
                    obj.put("day", String.valueOf(previousDatetime.getDayOfMonth()));
                    obj.put("hour", String.valueOf(previousDatetime.getHour()));
                    obj.put("minute", String.valueOf(previousDatetime.getMinute()));
                    obj.put("second", String.valueOf(previousDatetime.getSecond()));
                    obj.put("dayOfWeek", previousDatetime.getDayOfWeek().getDisplayName(TextStyle.SHORT, Locale.KOREAN));
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }

        } catch (JSONException e) {
            e.printStackTrace();
        }

        return obj;
    }

    public JSONObject getNextDayFrom(JSONObject specificDay) {
        JSONObject obj = new JSONObject();

        try {
            long previousDayTimestamp = (specificDay.getLong("timestamp") + 86400000L);
            LocalDateTime previousDatetime = null;
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                previousDatetime = LocalDateTime.ofInstant(Instant.ofEpochMilli(previousDayTimestamp),
                        TimeZone.getDefault().toZoneId());
            }


            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                try {
                    obj.put("timestamp", previousDayTimestamp);
                    obj.put("datetime", previousDatetime.toString());
                    obj.put("date", previousDatetime.toString().split("T")[0]);
                    obj.put("time", previousDatetime.toString().split("T")[1]);
                    obj.put("year", String.valueOf(previousDatetime.getYear()));
                    obj.put("month", String.valueOf(previousDatetime.getMonthValue()));
                    obj.put("day", String.valueOf(previousDatetime.getDayOfMonth()));
                    obj.put("hour", String.valueOf(previousDatetime.getHour()));
                    obj.put("minute", String.valueOf(previousDatetime.getMinute()));
                    obj.put("second", String.valueOf(previousDatetime.getSecond()));
                    obj.put("dayOfWeek", previousDatetime.getDayOfWeek().getDisplayName(TextStyle.SHORT, Locale.KOREAN));
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }

        } catch (JSONException e) {
            e.printStackTrace();
        }

        return obj;
    }

    public JSONObject getUTCDateTime() {
        JSONObject obj = new JSONObject();
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O) {
            ZonedDateTime zonedDateTime = ZonedDateTime.now().withZoneSameInstant(ZoneId.of("UTC"));
            DateTimeFormatter df = DateTimeFormatter.ofPattern("yyyy-MM-dd hh:mm:ss.ms");
            try {
                obj.put("timestamp", Timestamp.valueOf(df.format(zonedDateTime)).getTime());
                obj.put("datetime", String.valueOf(zonedDateTime));
                obj.put("date", zonedDateTime.getYear() + "-" + (String.valueOf(zonedDateTime.getMonthValue()).length() == 1 ? "0" + zonedDateTime.getMonthValue() : zonedDateTime.getMonthValue()) + "-" + (String.valueOf(zonedDateTime.getDayOfMonth()).length() == 1 ? "0" + zonedDateTime.getDayOfMonth() : zonedDateTime.getDayOfMonth()));
                obj.put("time", (String.valueOf(zonedDateTime.getHour()).length() == 1 ? "0" + zonedDateTime.getHour() : zonedDateTime.getHour()) + ":" + (String.valueOf(zonedDateTime.getMinute()).length() == 1 ? "0" + zonedDateTime.getMinute() : zonedDateTime.getMinute()) + ":" + (String.valueOf(zonedDateTime.getSecond()).length() == 1 ? "0" + zonedDateTime.getSecond() : zonedDateTime.getSecond()));
                obj.put("year", String.valueOf(zonedDateTime.getYear()));
                obj.put("month", String.valueOf(zonedDateTime.getMonthValue()));
                obj.put("day", String.valueOf(zonedDateTime.getDayOfMonth()));
                obj.put("hour", String.valueOf(zonedDateTime.getHour()));
                obj.put("minute", String.valueOf(zonedDateTime.getMinute()));
                obj.put("second", String.valueOf(zonedDateTime.getSecond()));
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        return obj;
    }
}
