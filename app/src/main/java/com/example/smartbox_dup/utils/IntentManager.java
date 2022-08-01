package com.example.smartbox_dup.utils;

import android.content.Context;
import android.content.Intent;
import android.provider.Settings;

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
}
