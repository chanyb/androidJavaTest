package com.example.smartbox_dup.utils;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.TextView;

import com.example.smartbox_dup.R;

public class CustomConfirmDialog extends Dialog {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        WindowManager.LayoutParams lpWindow = new WindowManager.LayoutParams();
        lpWindow.flags = WindowManager.LayoutParams.FLAG_DIM_BEHIND;
        lpWindow.dimAmount = 0.3f;
        getWindow().setAttributes(lpWindow);

        setContentView(R.layout.layout_confirm_dialog);

        setLayout();
        if (mConfirm != null) {
            setConfirm(mConfirm);
        }

        if (mContent != null)
            setContent(mContent);
        setClickListener(mConfirmClickListener);
    }

    public CustomConfirmDialog(Context context, String content, String right, View.OnClickListener confirmListener) {
        super(context, android.R.style.Theme_Translucent_NoTitleBar);
        this.mContent = content;
        this.mConfirm = right;
        this.mConfirmClickListener = confirmListener;
    }

    private void setContent(String content) {
        mContentView.setText(content);
    }

    private void setConfirm(String confirm) {
        mConfirmButton.setText(confirm);
    }

    private void setClickListener(View.OnClickListener mConfirmClickListener) {
        mConfirmButton.setOnClickListener(mConfirmClickListener);
    }

    /*
     * Layout
     */
    private TextView mContentView;
    private Button mConfirmButton;
    private String mContent;
    private String mConfirm;

    private View.OnClickListener mConfirmClickListener;

    /*
     * Layout
     */
    private void setLayout() {
        mContentView = (TextView) findViewById(R.id.txtmsg);
        mConfirmButton = (Button) findViewById(R.id.btn_confirm);
    }
}









