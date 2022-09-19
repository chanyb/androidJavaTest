package com.example.smartbox_dup.broadcastreceiver;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.net.ConnectivityManager;
import android.util.Log;

import com.example.smartbox_dup.utils.GlobalApplcation;

public class BroadcastManager {
    private static volatile BroadcastManager instance = new BroadcastManager();
    private BroadcastManager() {
    }

    public static BroadcastManager getInstance() {
        return instance;
    }

    public void register(BroadcastReceiver broadcastReceiver, IntentFilter intentFilter) {
        Context context = GlobalApplcation.getContext();
        if(context != null) context.registerReceiver(broadcastReceiver, intentFilter);
        else throw new NullPointerException("GlobalApplication.getContext() is null value");
    }

    public void register(Context context, BroadcastReceiver broadcastReceiver, IntentFilter intentFilter) {
        if(context != null) context.registerReceiver(broadcastReceiver, intentFilter);
        else throw new NullPointerException("The context parameter is null value");
    }

    public void sendBroadcast(Intent intent) {
        Context context = GlobalApplcation.getContext();
        if(context == null) throw new NullPointerException("GlobalApplication.getContext() is null value");

        context.sendBroadcast(intent);
    }

    public void sendBroadcast_test() {
        Intent intent = new Intent();
        intent.setAction("com.example.smartbox_dup.firstBroadcast");
        sendBroadcast(intent);
    }

    public void unregister(BroadcastReceiver receiver) {
        if(receiver == null) throw new NullPointerException("The receiver parameter is null value");
        Context context = GlobalApplcation.getContext();
        if(context != null) context.unregisterReceiver(receiver);
        else throw new NullPointerException("GlobalApplication.getContext() is null value");
    }

    public void unregister(Context context, BroadcastReceiver receiver) {
        if(receiver == null) throw new NullPointerException("The receiver parameter is null value");
        if(context != null) context.unregisterReceiver(receiver);
        else throw new NullPointerException("The context parameter is null value");
    }

}