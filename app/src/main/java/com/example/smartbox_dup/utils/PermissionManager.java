package com.example.smartbox_dup.utils;

import android.app.Dialog;
import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.provider.Settings;

import com.example.smartbox_dup.R;

import chanyb.android.java.GlobalApplcation;

public class PermissionManager {
    private static PermissionManager instance;
    private PermissionManager () {}

    public static PermissionManager getInstance() {
        if(instance == null) instance = new PermissionManager();
        return instance;
    }

    public boolean drawOverlaysRequiredButNotGranted() {
        return drawOverlaysRequired() && !canDrawOverlays();
    }

    private boolean drawOverlaysRequired() {
        return Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q;
    }

    private boolean canDrawOverlays() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            return Settings.canDrawOverlays(GlobalApplcation.getContext());
        } else {
            return false;
        }
    }

    public void overlayPermissionCheck() {
        if(drawOverlaysRequiredButNotGranted()) {
            DialogManager.getInstance().showConfirmDialog(GlobalApplcation.getContext().getString(R.string.permission_text), (view) -> {
                Intent gotoSettingForStartActivity = new Intent(Settings.ACTION_MANAGE_OVERLAY_PERMISSION, Uri.parse("package:" + GlobalApplcation.getContext().getPackageName()));
                gotoSettingForStartActivity.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                GlobalApplcation.getContext().startActivity(gotoSettingForStartActivity);
            });
        } else {
            DialogManager.getInstance().dismissConfirmDialog();
        }
    }
}
