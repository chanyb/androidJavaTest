package com.example.smartbox_dup.utils;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.TextView;

import com.example.smartbox_dup.R;


public class CustomAlertDialog extends Dialog {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        WindowManager.LayoutParams lpWindow = new WindowManager.LayoutParams();
        lpWindow.flags = WindowManager.LayoutParams.FLAG_DIM_BEHIND;
        lpWindow.dimAmount = 0.3f;
        getWindow().setAttributes(lpWindow);

        setContentView(R.layout.layout_alert_dialog);

        setLayout();
        if (mCancel != null) {
            setLeft(mCancel);
        }
        if (mConfirm != null) {
            setRight(mConfirm);
        }

        if (mContent != null)
            setContent(mContent);
        setClickListener(mCancelClickListener, mConfirmClickListener);
    }

    public CustomAlertDialog(Context context, String content, String cancel, String confirm,
                             View.OnClickListener cancelListener, View.OnClickListener confirmListener) {
        super(context, android.R.style.Theme_Translucent_NoTitleBar);
        if (content != null)
            this.mContent = content;
        this.mCancel = cancel;
        this.mConfirm = confirm;
        this.mCancelClickListener = cancelListener;
        this.mConfirmClickListener = confirmListener;
    }

    private void setContent(String content) {
        mContentView.setText(content);
    }

    private void setLeft(String left) {
        mCancelButton.setText(left);
    }

    private void setRight(String right) {
        mConfirmButton.setText(right);
    }

    private void setClickListener(View.OnClickListener mCancelClickListener, View.OnClickListener mConfirmClickListener) {
        mCancelButton.setOnClickListener(mCancelClickListener);
        mConfirmButton.setOnClickListener(mConfirmClickListener);
    }

    /*
     * Layout
     */
    private TextView mContentView;
    private Button mCancelButton;
    private Button mConfirmButton;
    private String mContent;
    private String mCancel;
    private String mConfirm;

    private View.OnClickListener mCancelClickListener;
    private View.OnClickListener mConfirmClickListener;

    /*
     * Layout
     */
    private void setLayout() {
        mContentView = (TextView) findViewById(R.id.txtmsg);
        mCancelButton = (Button) findViewById(R.id.btn_cancel);
        mConfirmButton = (Button) findViewById(R.id.btn_confirm);
    }
}









