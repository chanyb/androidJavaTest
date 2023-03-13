package com.example.smartbox_dup.utils;
import android.app.Dialog;
import android.app.NotificationManager;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.provider.Settings;
import android.view.View;

import androidx.activity.result.ActivityResultLauncher;
import androidx.appcompat.app.AppCompatActivity;

public class AudioManager {
    public enum State {
        VIBRATE, NORMAL, SILENT
    };
    private static AudioManager instance;
    private AudioManager() {
        systemAudioManager = (android.media.AudioManager) GlobalApplication.getContext().getSystemService(Context.AUDIO_SERVICE);
    }
    private android.media.AudioManager systemAudioManager;

    public static AudioManager getInstance() {
        if(instance == null) {
            instance = new AudioManager();
        }
        return instance;
    }

    public void setRingerMode(State state) {
        switch (state) {
            case NORMAL:
                systemAudioManager.setRingerMode(android.media.AudioManager.RINGER_MODE_NORMAL);
                break;
            case VIBRATE:
                systemAudioManager.setRingerMode(android.media.AudioManager.RINGER_MODE_VIBRATE);
                break;
            case SILENT:
                NotificationManager notificationManager;
                notificationManager = (NotificationManager) GlobalApplication.getContext().getSystemService(Context.NOTIFICATION_SERVICE);

                if (!notificationManager.isNotificationPolicyAccessGranted()) {
                    DialogManager.getInstance().showConfirmDialog("허용해야 함", new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            Intent intent = new Intent(Settings.ACTION_NOTIFICATION_POLICY_ACCESS_SETTINGS);
                            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK); // Activity가 아닌 곳에서 startActivity를 할 때 필요한 Flag
                            GlobalApplication.currentActivity.startActivity(intent);
                        }
                    });
                } else {
                    DialogManager.getInstance().dismissConfirmDialog();
                    systemAudioManager.setRingerMode(android.media.AudioManager.RINGER_MODE_SILENT);
                }
                break;
            default:
                throw new IllegalArgumentException("Please check a parameter");
        }
    }

}
