package com.example.smartbox_dup.utils;

import android.Manifest;
import android.app.Dialog;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;
import android.provider.Settings;
import android.util.Log;

import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import com.example.smartbox_dup.R;

import chanyb.android.java.GlobalApplcation;

public class PermissionManager {
    private static PermissionManager instance;
    private PermissionManager() {}

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
            DialogManager.getInstance().showConfirmDialog(GlobalApplcation.getContext().getString(R.string.vCancel_text), (view) -> {
                Intent gotoSettingForStartActivity = new Intent(Settings.ACTION_MANAGE_OVERLAY_PERMISSION);
                gotoSettingForStartActivity.setData(Uri.parse("package:" + GlobalApplcation.getContext().getPackageName()));
                gotoSettingForStartActivity.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                GlobalApplcation.getContext().startActivity(gotoSettingForStartActivity);
            });
        } else {
            DialogManager.getInstance().dismissConfirmDialog();
        }
    }

    public void requestCameraPermission() {
        String[] permissions = {
                Manifest.permission.CAMERA
        };

        int CAMERA_PERMISSION = ContextCompat.checkSelfPermission(GlobalApplcation.getContext(), permissions[0]);

        if(CAMERA_PERMISSION == PackageManager.PERMISSION_DENIED) {
            ActivityCompat.requestPermissions(GlobalApplcation.currentActivity, permissions, 0);
        }
    }

    public boolean cameraPermissionCheck() {
        String[] permissions = {
                Manifest.permission.CAMERA
        };

        int CAMERA_PERMISSION = ContextCompat.checkSelfPermission(GlobalApplcation.getContext(), permissions[0]);
        return CAMERA_PERMISSION == PackageManager.PERMISSION_GRANTED;
    }

    public boolean isPermissionAlreadyDenied(String permission) {
        if(ActivityCompat.shouldShowRequestPermissionRationale(GlobalApplcation.currentActivity, permission)) return true;
        return false;
    }

    public boolean writeExternalStoragePermissionCheck() {
        String permission = Manifest.permission.WRITE_EXTERNAL_STORAGE;

        int WRITE_EXTERNAL_STORAGE_PERMISSION = ContextCompat.checkSelfPermission(GlobalApplcation.getContext(), permission);
        return WRITE_EXTERNAL_STORAGE_PERMISSION == PackageManager.PERMISSION_GRANTED;
    }

    public boolean requestWriteExternalStoragePermission() {
        String[] permissions = {
                Manifest.permission.WRITE_EXTERNAL_STORAGE
        };

        if(isPermissionAlreadyDenied(permissions[0])) return false;

        int WRITE_EXTERNAL_STORAGE_PERMISSION = ContextCompat.checkSelfPermission(GlobalApplcation.getContext(), permissions[0]);

        if(WRITE_EXTERNAL_STORAGE_PERMISSION == PackageManager.PERMISSION_DENIED) {
            ActivityCompat.requestPermissions(GlobalApplcation.currentActivity, permissions, 0);
        }

        return true;
    }

    public void requestPermissions(String[] permissions) {
        if(GlobalApplcation.currentActivity == null) throw new NullPointerException("GlobalApplication.currentActivity is null");
        ActivityCompat.requestPermissions(GlobalApplcation.currentActivity, permissions, 0);
    }

}
