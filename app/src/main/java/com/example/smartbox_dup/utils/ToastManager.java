package com.example.smartbox_dup.utils;

import android.content.Context;
import android.widget.Toast;

public class ToastManager {
    private static ToastManager instance = new ToastManager();
    private Toast toast;

    private ToastManager () {}

    public static ToastManager getInstance() {
        return instance;
    }

    public void showToast(Context context, String msg) {
        toast = Toast.makeText(context, msg, Toast.LENGTH_SHORT);
        toast.show();
    }


}
