package com.example.smartbox_dup.utils;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.provider.Settings;

import java.util.Iterator;
import java.util.Set;

public class IntentManager {
    private IntentManager() {}
    private static IntentManager instance = new IntentManager();
    public static IntentManager getInstance() {
        return instance;
    }

    public Intent getSettingIntent() {
        return new Intent(Settings.ACTION_SETTINGS);
    }

    public Intent getWifiSettingIntent() {
        return new Intent(Settings.ACTION_WIFI_SETTINGS);
    }

    public Intent getDataSettingIntent() {
        return new Intent(Settings.ACTION_DATA_USAGE_SETTINGS);
    }

    public Intent getBluetoothSettingIntent() {
        return new Intent(Settings.ACTION_BLUETOOTH_SETTINGS);
    }

    public Intent getApplicationSettingIntent() {
        return new Intent(Settings.ACTION_APPLICATION_SETTINGS);
    }

    public Intent getOtherApplicationRunIntent(Context mContext, String packageName) {
        return mContext.getPackageManager().getLaunchIntentForPackage(packageName);
    }

    public void copyExtras(Intent original, Intent duplicated) {
        Bundle bundle = original.getExtras();

        Set<String> keys = bundle.keySet();
        for (Iterator<String> iterator = keys.iterator(); iterator.hasNext();) {
            String key = iterator.next();
            if(original.getStringExtra(key) != null) duplicated.putExtra(key, original.getStringExtra(key));
            else if(original.getByteArrayExtra(key) != null) duplicated.putExtra(key, original.getStringExtra(key));
            else if(original.getIntExtra(key, -95958) != -95958) duplicated.putExtra(key, original.getStringExtra(key));
            else duplicated.putExtra(key, "error");
        }

    }
}
