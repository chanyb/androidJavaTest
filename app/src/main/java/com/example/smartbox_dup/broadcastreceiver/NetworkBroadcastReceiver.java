package com.example.smartbox_dup.broadcastreceiver;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.util.Log;


public class NetworkBroadcastReceiver extends BroadcastReceiver {

    @Override
    public void onReceive(Context mContext, Intent intent) {
        ConnectivityManager connectivityManager = (ConnectivityManager) mContext.getSystemService(Context.CONNECTIVITY_SERVICE);
        if(connectivityManager.getActiveNetworkInfo()==null) {
            // network disconnected
            Log.i("this", "네트웤 연결 해제2");
        } else if (connectivityManager.getActiveNetworkInfo()!= null && connectivityManager.getActiveNetworkInfo().isConnected()) {
            Log.i("this", "네트웤 연결2");
        } else {
            Log.i("this", "네트웤??");
        }

        Log.i("this", intent.getAction());

    }
}
