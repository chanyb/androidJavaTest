package com.example.smartbox_dup.broadcastreceiver;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.util.Log;


public class NetworkBroadcastReceiver extends BroadcastReceiver {
    private String TAG = "NetworkBroadcastReceiver";

    @Override
    public void onReceive(Context mContext, Intent intent) {
        ConnectivityManager connectivityManager = (ConnectivityManager) mContext.getSystemService(Context.CONNECTIVITY_SERVICE);
        if(connectivityManager.getActiveNetworkInfo()==null) {
            // network disconnected
            Log.i(TAG, "Network disconnected");
        } else if (connectivityManager.getActiveNetworkInfo()!= null && connectivityManager.getActiveNetworkInfo().isConnected()) {
            Log.i(TAG, "Network connected");
        } else {
            Log.i(TAG, "UNKNOWN");
        }
    }
}
