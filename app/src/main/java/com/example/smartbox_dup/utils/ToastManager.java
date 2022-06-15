package com.example.smartbox_dup.utils;

import android.content.Context;
import android.os.Handler;
import android.os.Looper;
import android.widget.Toast;

public class ToastManager {
    private static ToastManager instance = new ToastManager();
    private Toast toast;

    private ToastManager () {}

    public static ToastManager getInstance() {
        return instance;
    }

    public void showToast(Context context, String msg) {
        Handler handler = new Handler(Looper.getMainLooper());
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                toast = Toast.makeText(context, msg, Toast.LENGTH_SHORT);
                toast.show();
            }
        }, 0);

    }


}
