package com.example.smartbox_dup;

import android.graphics.Bitmap;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

public class WebViewActivity extends AppCompatActivity implements View.OnClickListener {

    WebView wv_condition;
    WebSettings wv_setting;
    ImageView iv_backBtn;
    TextView tv_title;
    TextView tv_confirm;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signup_webviwe);

        this.init();
        this.init_wvCondition();
        this.setOnclickListener();
    }

    public void init() {
        wv_condition = findViewById(R.id.wv_condition);
        iv_backBtn = findViewById(R.id.iv_backBtn);
        tv_title = findViewById(R.id.tv_title);
        tv_confirm = findViewById(R.id.tv_confirm);


        tv_title.setText(getIntent().getStringExtra("title"));
    }

    public void setOnclickListener() {
        iv_backBtn.setOnClickListener(this);
        tv_confirm.setOnClickListener(this);
    }

    public void init_wvCondition() {
        wv_condition.loadUrl("https://m.naver.com");
        wv_condition.setWebViewClient(new WebViewClient());
        wv_setting = wv_condition.getSettings();
        wv_setting.setJavaScriptEnabled(true);
    }


    @Override
    public void onClick(View view) {
        switch(view.getId()) {
            case R.id.iv_backBtn:
            case R.id.tv_confirm:
                this.onBackPressed();
                break;
        }
    }
}
