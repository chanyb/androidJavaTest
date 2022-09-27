package com.example.smartbox_dup.screen.function.alarm;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;

import com.example.smartbox_dup.utils.ByteArrayManager;

public class AlarmBroadcastReceiver extends BroadcastReceiver {
    Alarm alarm;

    @Override
    public void onReceive(Context context, Intent intent) {
        if(intent.getAction() != null && intent.getAction().equals(Intent.ACTION_BOOT_COMPLETED)) {
            Log.i("this", "AlarmBroadcastReceiver - BOOT_COMPLETED");
            return ;
        }

        Log.i("this", String.valueOf(intent.getAction()));

        getAlarm(intent.getByteArrayExtra("alarm"));
        if(alarm == null) return ;

        Log.i("this", alarm.getTitle());
        startAlarmService(context, intent);
    }

    private void getAlarm(byte[] bytes) {
        ByteArrayManager<Alarm> manager = new ByteArrayManager<>();
        alarm = manager.getClassFromByteArray(bytes);
    }

    private void startAlarmService(Context context, Intent parentIntent) {
        Intent intentService = new Intent(context, AlarmService.class);
        intentService.putExtra("alarm", parentIntent.getByteArrayExtra("alarm"));

        context.startService(intentService);
    }
}
