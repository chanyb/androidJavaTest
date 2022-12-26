package com.example.smartbox_dup.screen.function.webview;

import android.os.Build;
import android.os.Bundle;
import android.webkit.WebChromeClient;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Button;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.example.smartbox_dup.R;
import com.example.smartbox_dup.utils.ByteArrayManager;

public class WebviewTest extends AppCompatActivity {
    WebView webview, webview_dup;
    Button btn_1;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_function_webview);
        init();
    }

    private void init() {
        webview = findViewById(R.id.webview);
        webview.getSettings().setJavaScriptEnabled(true);
        webview.setWebChromeClient(new WebChromeClient());
        webview.setWebViewClient(new WebViewClient());
        webview.loadUrl("https://m.naver.com");
        webview_dup = findViewById(R.id.webview_dup);
        btn_1 = findViewById(R.id.btn_1);
        btn_1.setOnClickListener((v) -> {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                webview_dup.getSettings().setJavaScriptEnabled(true);
                webview_dup.setWebChromeClient(webview.getWebChromeClient());
                webview_dup.setWebViewClient(webview.getWebViewClient());
                webview_dup.loadUrl(webview.getUrl());
            }

        });

    }

    /**
     * 결론: 웹뷰는 Serializable Class가 아니기 때문에, ByteArrayManager를 통해 byte array로 변환 할 수 없다.
     * 따라서, 객체의 이동이 안되며, 했다고 해도 webviewA = webviewB 를 통해 B를 A에 대입할 수 없으므로, 웹뷰의 각종 세팅을 꺼내서 동일하게 맞춰주면 세션을 유지시킬 수 있는 것으로 보인다.
     */
}
