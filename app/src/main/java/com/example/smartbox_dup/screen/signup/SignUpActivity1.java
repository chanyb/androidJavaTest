package com.example.smartbox_dup.screen.signup;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.view.ViewCompat;

import com.example.smartbox_dup.R;
import com.example.smartbox_dup.WebViewActivity;
import com.example.smartbox_dup.location.GoogleLocationManger;

public class SignUpActivity1 extends AppCompatActivity implements View.OnClickListener {
    private ImageView iv_logo;
    private LinearLayout lo_next;
    private ImageView iv_backBtn;
    private Intent intent;
    private ImageView cb_agreeAllConditions;
    private TextView tv_agreeAllConditions;
    private ImageView cb_agreeUsageCondition;
    private TextView tv_agreeUsageCondition;
    private ImageView cb_agreePrivacyCondition;
    private TextView tv_agreePrivacyCondition;
    private ImageView iv_moveUsageCondition;
    private ImageView iv_movePrivacyCondition;
    public TextView tv_termsConditions_header;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sigup1);
        this.init();
        this.setOnClickListener();
        ViewCompat.setTransitionName(tv_termsConditions_header, "signup");

    }

    public void init() {
        this.iv_logo = findViewById(R.id.iv_logo);
        this.lo_next = findViewById(R.id.lo_next);
        this.iv_backBtn = findViewById(R.id.iv_backBtn);
        this.cb_agreeAllConditions = findViewById(R.id.cb_agreeAllConditions);
        this.tv_agreeAllConditions = findViewById(R.id.tv_agreeAllConditions);
        this.cb_agreeUsageCondition = findViewById(R.id.cb_agreeUsageCondition);
        this.tv_agreeUsageCondition = findViewById(R.id.tv_agreeUsageCondition);
        this.cb_agreePrivacyCondition = findViewById(R.id.cb_agreePrivacyCondition);
        this.tv_agreePrivacyCondition = findViewById(R.id.tv_agreePrivacyCondition);
        this.iv_moveUsageCondition = findViewById(R.id.iv_moveUsageCondition);
        this.iv_movePrivacyCondition = findViewById(R.id.iv_movePrivacyCondition);
        this.tv_termsConditions_header = findViewById(R.id.tv_termsConditions_header);

    }

    public void setOnClickListener() {
        this.iv_logo.setOnClickListener(this);
        this.iv_backBtn.setOnClickListener(this);
        this.cb_agreeAllConditions.setOnClickListener(this);
        this.tv_agreeAllConditions.setOnClickListener(this);
        this.cb_agreeUsageCondition.setOnClickListener(this);
        this.cb_agreePrivacyCondition.setOnClickListener(this);
        this.iv_moveUsageCondition.setOnClickListener(this);
        this.iv_movePrivacyCondition.setOnClickListener(this);
        this.tv_agreeUsageCondition.setOnClickListener(this);
        this.tv_agreePrivacyCondition.setOnClickListener(this);
        /* 동적으로 달아주기 위해 주석처리 */
        //this.lo_next.setOnClickListener(this);
    }

    public void changeCheckBoxResource(int resId) {
        ImageView target = findViewById(resId);
        if(target.getTag() == null) {
            target.setTag(true);
            target.setImageResource(R.drawable.check_on);
        } else {
            target.setTag(null);
            target.setImageResource(R.drawable.check_off);
        }

        this.isPossibleToGoNext();
    }

    public void changeCheckBoxResource(int resId, Object _tag) {
        ImageView target = findViewById(resId);
        if(_tag == null) {
            target.setTag(null);
            target.setImageResource(R.drawable.check_off);
        } else {
            target.setTag(true);
            target.setImageResource(R.drawable.check_on);
        }

        this.isPossibleToGoNext();
    }

    public boolean isPossibleToGoNext() {
        if(String.valueOf(this.cb_agreeUsageCondition.getTag()).equals("true") && String.valueOf(this.cb_agreePrivacyCondition.getTag()).equals("true")) {
            this.lo_next.setBackgroundResource(R.drawable.next_btn_active);
            this.lo_next.setOnClickListener(this);
            return true;
        }

        this.lo_next.setBackgroundResource(R.drawable.next_btn);
        this.lo_next.setOnClickListener(null);

        return false;
    }

    @Override
    public void onClick(View view) {
        switch(view.getId()) {
            case R.id.iv_logo:
                Log.i("this", "click in signup1");
                break;
            case R.id.lo_next:
                intent = new Intent(this, SignUpActivity2.class);
                startActivity(intent);
                break;
            case R.id.iv_backBtn:
                this.onBackPressed();
                break;
            case R.id.tv_agreeAllConditions:
            case R.id.cb_agreeAllConditions:
                Object tag = cb_agreeAllConditions.getTag();

                // tag 일치화 작업
                cb_agreeUsageCondition.setTag(tag);
                cb_agreePrivacyCondition.setTag(tag);

                changeCheckBoxResource(R.id.cb_agreeAllConditions);
                changeCheckBoxResource(R.id.cb_agreeUsageCondition);
                changeCheckBoxResource(R.id.cb_agreePrivacyCondition);
                break;
            case R.id.cb_agreeUsageCondition:
            case R.id.tv_agreeUsageCondition:
                changeCheckBoxResource(R.id.cb_agreeUsageCondition);
                if (String.valueOf(cb_agreeUsageCondition.getTag()).equals(String.valueOf(cb_agreePrivacyCondition.getTag()))) {
                    // 같을 때
                    changeCheckBoxResource(R.id.cb_agreeAllConditions, cb_agreeUsageCondition.getTag());
                } else {
                    // 다를 떄
                    changeCheckBoxResource(R.id.cb_agreeAllConditions, null);
                }
                break;
            case R.id.cb_agreePrivacyCondition:
            case R.id.tv_agreePrivacyCondition:
                changeCheckBoxResource(R.id.cb_agreePrivacyCondition);
                if (String.valueOf(cb_agreeUsageCondition.getTag()).equals(String.valueOf(cb_agreePrivacyCondition.getTag()))) {
                    // 같을 때
                    changeCheckBoxResource(R.id.cb_agreeAllConditions, cb_agreeUsageCondition.getTag());
                } else {
                    // 다를 떄
                    changeCheckBoxResource(R.id.cb_agreeAllConditions, null);
                }
                break;
            case R.id.iv_moveUsageCondition:
                Intent WebViewIntent = new Intent(this, WebViewActivity.class);
                WebViewIntent.putExtra("title", "이용약관");
                startActivity(WebViewIntent);
                break;
            case R.id.iv_movePrivacyCondition:
                WebViewIntent = new Intent(this, WebViewActivity.class);
                WebViewIntent.putExtra("title", "개인정보 처리방침");
                startActivity(WebViewIntent);
                break;
        }
    }

    @Override
    public void onBackPressed() {
        Log.i("this", "onBackPressed!");
        tv_termsConditions_header.setText("회원가입");
        finishAfterTransition();
        super.onBackPressed();
    }
}