package com.example.smartbox_dup.broadcastreceiver;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.widget.Toast;

import com.example.smartbox_dup.SampleForegroundService;



public class MyBroadcastReceiver extends BroadcastReceiver {



    @Override
    public void onReceive(Context context, Intent intent) {

        StringBuilder sb = new StringBuilder();
        sb.append("Action: " + intent.getAction() + "\n");
        sb.append("URI: " + intent.toUri(Intent.URI_INTENT_SCHEME).toString() + "\n");
        String log = sb.toString();
        Toast.makeText(context, log, Toast.LENGTH_SHORT).show();
        Log.i("this",log);


        switch (intent.getAction()) {
            case Intent.ACTION_BOOT_COMPLETED:
                context.startService(new Intent(context, SampleForegroundService.class));
                break;
        }
    }
}
