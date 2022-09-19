package com.example.smartbox_dup.utils;

import android.os.Build;
import android.util.Log;

import org.json.JSONException;
import org.json.JSONObject;

import java.sql.Timestamp;
import java.text.ParseException;
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
    private DatetimeManager() {
        datetimeFormatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.KOREA);
        dateFormatter = new SimpleDateFormat("yyyy-MM-dd", Locale.KOREA);
        timeFormatter = new SimpleDateFormat("HH:mm:ss", Locale.KOREA);
        yearFormatter = new SimpleDateFormat("yyyy", Locale.KOREA);
        monthFormatter = new SimpleDateFormat("MM", Locale.KOREA);
        dayFormatter = new SimpleDateFormat("dd", Locale.KOREA);
        hourFormatter = new SimpleDateFormat("HH", Locale.KOREA);
        minuteFormatter = new SimpleDateFormat("mm", Locale.KOREA);
        secondFormatter = new SimpleDateFormat("ss", Locale.KOREA);
    }

    public static DatetimeManager getInstance() {
        return instance;
    }

    SimpleDateFormat datetimeFormatter;
    SimpleDateFormat dateFormatter;
    SimpleDateFormat timeFormatter;
    SimpleDateFormat yearFormatter;
    SimpleDateFormat monthFormatter;
    SimpleDateFormat dayFormatter;
    SimpleDateFormat hourFormatter;
    SimpleDateFormat minuteFormatter;
    SimpleDateFormat secondFormatter;

    public String getDisplayName(int dayOfWeek) {

        switch (dayOfWeek) {
            case 1:
                return "일";
            case 2:
                return "월";
            case 3:
                return "화";
            case 4:
                return "수";
            case 5:
                return "목";
            case 6:
                return "금";
            case 7:
                return "토";
        }

        return "error";
    }

    public JSONObject getSystemDateTime() {
        JSONObject obj = new JSONObject();
        LocalDateTime localDateTime = null;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
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
            // 오레오 이하 버전 대응
            Calendar calendar = Calendar.getInstance();
            try {
                obj.put("timestamp", String.valueOf(calendar.getTimeInMillis()));
                obj.put("datetime", datetimeFormatter.format(calendar.getTime()));
                obj.put("date", dateFormatter.format(calendar.getTime()));
                obj.put("time", timeFormatter.format(calendar.getTime()));
                obj.put("year", yearFormatter.format(calendar.getTime()));
                obj.put("month", monthFormatter.format(calendar.getTime()));
                obj.put("day", dayFormatter.format(calendar.getTime()));
                obj.put("hour", hourFormatter.format(calendar.getTime()));
                obj.put("minute", minuteFormatter.format(calendar.getTime()));
                obj.put("second", secondFormatter.format(calendar.getTime()));
                obj.put("dayOfWeek", getDisplayName(calendar.get(Calendar.DAY_OF_WEEK)));
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }

        return obj;
    }

    public JSONObject getSystemDate() {
        JSONObject obj = new JSONObject();
        LocalDateTime localDateTime = null;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
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
        } else {
            // 오레오 이하 버전 대응
            Date date = new Date();
            date.setHours(0);
            date.setMinutes(0);
            date.setSeconds(0);
            Calendar calendar = Calendar.getInstance();
            calendar.setTime(date);
            try {
                obj.put("timestamp", String.valueOf(calendar.getTimeInMillis()));
                obj.put("date", dateFormatter.format(calendar.getTime()));
                obj.put("year", yearFormatter.format(calendar.getTime()));
                obj.put("month", monthFormatter.format(calendar.getTime()));
                obj.put("day", dayFormatter.format(calendar.getTime()));
                obj.put("dayOfWeek", getDisplayName(calendar.get(Calendar.DAY_OF_WEEK)));
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }

        return obj;
    }

    public double distanceFromTodayOf(JSONObject target) {
        JSONObject today = getSystemDate();
        double distance = 0;
        try {
            distance = target.getLong("timestamp") - today.getLong("timestamp");
        } catch (JSONException e) {
            e.printStackTrace();
        }

        Log.i("this", "distance: " + String.valueOf(distance));
        return (distance/86400000 + 0.0);
    }

    public JSONObject getJSONObjectFromLocalDate(LocalDate localDate) {
        JSONObject obj = new JSONObject();

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
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
        } else {
            throw new RuntimeException("You don't have to use Localdate in the Build Version");
        }

        return obj;
    }

    public JSONObject getJSONObjectFromLocalDateTime(LocalDateTime localDate) {
        JSONObject obj = new JSONObject();

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
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
        } else {
            throw new RuntimeException("You don't have to use Localdate in the Build Version");
        }

        return obj;
    }

    public JSONObject getJSONObjectFromCalendar(Calendar calendar) {
        JSONObject obj = new JSONObject();
        TimeZone tz = calendar.getTimeZone();
        ZoneId zid = null;
        LocalDate localDate = null;
        LocalTime localTime = null;

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            zid = tz == null ? ZoneId.systemDefault() : tz.toZoneId();
            localDate = LocalDateTime.ofInstant(calendar.toInstant(), zid).toLocalDate();
            localTime = LocalDateTime.ofInstant(calendar.toInstant(), zid).toLocalTime();

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
        } else {
            try {
                obj.put("timestamp", String.valueOf(calendar.getTimeInMillis()));
                obj.put("datetime", datetimeFormatter.format(calendar.getTime()));
                obj.put("date", dateFormatter.format(calendar.getTime()));
                obj.put("time", timeFormatter.format(calendar.getTime()));
                obj.put("year", yearFormatter.format(calendar.getTime()));
                obj.put("month", monthFormatter.format(calendar.getTime()));
                obj.put("day", dayFormatter.format(calendar.getTime()));
                obj.put("hour", hourFormatter.format(calendar.getTime()));
                obj.put("minute", minuteFormatter.format(calendar.getTime()));
                obj.put("second", secondFormatter.format(calendar.getTime()));
                obj.put("dayOfWeek", getDisplayName(calendar.get(Calendar.DAY_OF_WEEK)));
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
            } else {
                Date date = new Date(previousDayTimestamp);
                Calendar calendar = Calendar.getInstance();
                calendar.setTime(date);
                try {
                    obj.put("timestamp", String.valueOf(calendar.getTimeInMillis()));
                    obj.put("datetime", datetimeFormatter.format(calendar.getTime()));
                    obj.put("date", dateFormatter.format(calendar.getTime()));
                    obj.put("time", timeFormatter.format(calendar.getTime()));
                    obj.put("year", yearFormatter.format(calendar.getTime()));
                    obj.put("month", monthFormatter.format(calendar.getTime()));
                    obj.put("day", dayFormatter.format(calendar.getTime()));
                    obj.put("hour", hourFormatter.format(calendar.getTime()));
                    obj.put("minute", minuteFormatter.format(calendar.getTime()));
                    obj.put("second", secondFormatter.format(calendar.getTime()));
                    obj.put("dayOfWeek", getDisplayName(calendar.get(Calendar.DAY_OF_WEEK)));
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
            long nextDayTimestamp = (specificDay.getLong("timestamp") + 86400000L);
            LocalDateTime nextDatetime = null;
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                nextDatetime = LocalDateTime.ofInstant(Instant.ofEpochMilli(nextDayTimestamp),
                        TimeZone.getDefault().toZoneId());

                try {
                    obj.put("timestamp", nextDayTimestamp);
                    obj.put("datetime", nextDatetime.toString());
                    obj.put("date", nextDatetime.toString().split("T")[0]);
                    obj.put("time", nextDatetime.toString().split("T")[1]);
                    obj.put("year", String.valueOf(nextDatetime.getYear()));
                    obj.put("month", String.valueOf(nextDatetime.getMonthValue()));
                    obj.put("day", String.valueOf(nextDatetime.getDayOfMonth()));
                    obj.put("hour", String.valueOf(nextDatetime.getHour()));
                    obj.put("minute", String.valueOf(nextDatetime.getMinute()));
                    obj.put("second", String.valueOf(nextDatetime.getSecond()));
                    obj.put("dayOfWeek", nextDatetime.getDayOfWeek().getDisplayName(TextStyle.SHORT, Locale.KOREAN));
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            } else {
                Date date = new Date(nextDayTimestamp);
                Calendar calendar = Calendar.getInstance();
                calendar.setTime(date);
                try {
                    obj.put("timestamp", String.valueOf(calendar.getTimeInMillis()));
                    obj.put("datetime", datetimeFormatter.format(calendar.getTime()));
                    obj.put("date", dateFormatter.format(calendar.getTime()));
                    obj.put("time", timeFormatter.format(calendar.getTime()));
                    obj.put("year", yearFormatter.format(calendar.getTime()));
                    obj.put("month", monthFormatter.format(calendar.getTime()));
                    obj.put("day", dayFormatter.format(calendar.getTime()));
                    obj.put("hour", hourFormatter.format(calendar.getTime()));
                    obj.put("minute", minuteFormatter.format(calendar.getTime()));
                    obj.put("second", secondFormatter.format(calendar.getTime()));
                    obj.put("dayOfWeek", getDisplayName(calendar.get(Calendar.DAY_OF_WEEK)));
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
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
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
        } else {
            Calendar calendar = Calendar.getInstance();
            SimpleDateFormat formatter = new SimpleDateFormat("EE MMM dd HH:mm:ss zzz yyyy", Locale.US);
            formatter.setTimeZone(TimeZone.getTimeZone("UTC"));
            try {
                obj.put("timestamp", String.valueOf(calendar.getTimeInMillis()));
                obj.put("datetime", formatter.format(calendar.getTime()));
                obj.put("date", dateFormatter.format(calendar.getTime()));
                obj.put("time", timeFormatter.format(calendar.getTime()));
                obj.put("year", yearFormatter.format(calendar.getTime()));
                obj.put("month", monthFormatter.format(calendar.getTime()));
                obj.put("day", dayFormatter.format(calendar.getTime()));
                obj.put("hour", hourFormatter.format(calendar.getTime()));
                obj.put("minute", minuteFormatter.format(calendar.getTime()));
                obj.put("second", secondFormatter.format(calendar.getTime()));
                obj.put("dayOfWeek", getDisplayName(calendar.get(Calendar.DAY_OF_WEEK)));
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }

        return obj;
    }

    public LocalDateTime localDateTimeFormatter(String sDate, String pattern) {
        DateTimeFormatter formatter = null;
        LocalDateTime localDatetime = null;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            formatter = DateTimeFormatter.ofPattern(pattern);
            localDatetime = LocalDateTime.parse(sDate, formatter);
        } else {
            throw new RuntimeException("LocalDateTime can't use in this Build Version");
        }

        return localDatetime;
    }

    public Date stringToDate(String str, String pattern) {
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat(pattern);
        Date date = null;
        try {
            date = simpleDateFormat.parse(str);
        } catch (ParseException e) {
            e.printStackTrace();
        }

        return date;
    }

    public JSONObject getJSONObjectFromString(String str, String pattern) {
        JSONObject obj = new JSONObject();
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(stringToDate(str, pattern));

        try {
            obj.put("timestamp", String.valueOf(calendar.getTimeInMillis()));
            obj.put("datetime", datetimeFormatter.format(calendar.getTime()));
            obj.put("date", dateFormatter.format(calendar.getTime()));
            obj.put("time", timeFormatter.format(calendar.getTime()));
            obj.put("year", yearFormatter.format(calendar.getTime()));
            obj.put("month", monthFormatter.format(calendar.getTime()));
            obj.put("day", dayFormatter.format(calendar.getTime()));
            obj.put("hour", hourFormatter.format(calendar.getTime()));
            obj.put("minute", minuteFormatter.format(calendar.getTime()));
            obj.put("second", secondFormatter.format(calendar.getTime()));
            obj.put("dayOfWeek", getDisplayName(calendar.get(Calendar.DAY_OF_WEEK)));
        } catch (JSONException e) {
            e.printStackTrace();
        }

        return obj;
    }
}
