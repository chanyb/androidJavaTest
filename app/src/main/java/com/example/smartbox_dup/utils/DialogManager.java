package com.example.smartbox_dup.utils;

import android.content.Context;
import android.view.View;

import com.example.smartbox_dup.R;

import chanyb.android.java.GlobalApplcation;

public class DialogManager {
    private static DialogManager instance;
    private DialogManager() {}
    private CustomAlertDialog customAlertDialog;
    private CustomConfirmDialog customConfirmDialog;

    public static DialogManager getInstance() {
        if(instance == null) instance = new DialogManager();
        return instance;
    }

    public void showConfirmDialog(String msg, View.OnClickListener clickListener) {
        if (customConfirmDialog != null) if (customConfirmDialog.isShowing()) return;
        customConfirmDialog = new CustomConfirmDialog(GlobalApplcation.currentActivity, msg, GlobalApplcation.getContext().getString(R.string.vConfirm_text), clickListener);

        customConfirmDialog.show();
    }

    public void dismissConfirmDialog() {
        if (customConfirmDialog != null) if (customConfirmDialog.isShowing()) customConfirmDialog.dismiss();
    }

    public void showAlertDialog(String msg, View.OnClickListener cancelListener, View.OnClickListener confirmListener) {
        if (customAlertDialog != null) if (customAlertDialog.isShowing()) return;
        customAlertDialog = new CustomAlertDialog(GlobalApplcation.currentActivity, msg, GlobalApplcation.getContext().getString(R.string.vCancel_text),GlobalApplcation.getContext().getString(R.string.vConfirm_text), cancelListener, confirmListener);

        customAlertDialog.show();
    }

    public void dismissAlertDialog() {
        if (customAlertDialog != null) if (customAlertDialog.isShowing()) customAlertDialog.dismiss();
    }
}
