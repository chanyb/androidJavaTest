package com.example.smartbox_dup.utils;


import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Build;
import android.view.View;
import android.webkit.ConsoleMessage;
import android.webkit.CookieManager;
import android.webkit.JsResult;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;

import java.net.URLDecoder;

public class WebViewManagerTest {
    private static WebViewManagerTest instance = new WebViewManagerTest();
    private WebViewManagerTest () {}
    private Context context = null;

    public static WebViewManagerTest getInstance() {
        return instance;
    }

    public void setWebView(WebView webView, Context _context, String _url) {
        context = _context;
//        webView.getSettings().setCacheMode(WebSettings.LOAD_DEFAULT); // 웹뷰가 캐시를 사용하지 않도록 설정
        webView.getSettings().setJavaScriptEnabled(true); // 자바스크립트 허용
        webView.getSettings().setAllowFileAccess(false);
        webView.getSettings().setBuiltInZoomControls(false);
        webView.getSettings().setSupportZoom(false);
        webView.getSettings().setDomStorageEnabled(true); // 로컬 스토리지 사용 여부를 설정하는 속성으로 팝업창등을 '하루동안 보지 않기' 기능 사용에 필요합니다.
        webView.getSettings().setJavaScriptCanOpenWindowsAutomatically(true); // 자바스크립트가 window.open()을 사용할 수 있도록 설정
        webView.getSettings().setSupportMultipleWindows(true); // 새 창 열 때 필수설정(true)
        webView.getSettings().setTextZoom(100);
        webView.getSettings().setAllowFileAccessFromFileURLs(false);
        webView.getSettings().setAllowUniversalAccessFromFileURLs(false);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            WebView.setWebContentsDebuggingEnabled(true);
        }
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
//            webView.getSettings().setMixedContentMode(WebSettings.MIXED_CONTENT_ALWAYS_ALLOW); //HTTPS HTTP의 연동, 서로 호출 가능하도록
        }
        webView.setWebChromeClient(new CustomWebChromeClient());
        webView.setWebViewClient(new AxaWebViewClient());
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            webView.setLayerType(View.LAYER_TYPE_HARDWARE, null);
        } else {
            webView.setLayerType(View.LAYER_TYPE_SOFTWARE, null);
        }
//        webView.addJavascriptInterface(new WebviewInterface(this, mProgressDialog), "HybridApp");

        webView.clearCache(true);
        webView.clearHistory();
        webView.loadUrl(_url);
    }

    public class AxaWebViewClient extends WebViewClient {
        // 새로운 URL이 webview에 로드되려 할 경우 컨트롤을 대신할 기회를 줌
        public boolean shouldOverrideUrlLoading(WebView view, String url) {
            try {
                url = URLDecoder.decode(url, "utf-8");
            } catch (Exception e) {
                e.printStackTrace();
            }

            if (url.startsWith("tel:")) {
                Intent tel = new Intent(Intent.ACTION_DIAL, Uri.parse(url));
                context.startActivity(tel);
            } else {
                view.loadUrl(url);
            }
            return true;
        }

        // 로딩이 시작될 때
        @Override
        public void onPageStarted(WebView view, final String url, Bitmap favicon) {
            super.onPageStarted(view, url, favicon);

        }

        // 로딩이 완료됬을 때 한번 호출
        @Override
        public void onPageFinished(WebView view, final String url) {
            super.onPageFinished(view, url);

            CookieManager.getInstance().flush();
        }

        @Override
        public void onReceivedError(final WebView view, int errorCode, String description, final String failingUrl) {
            super.onReceivedError(view, errorCode, description, failingUrl);

            switch (errorCode) {
                case ERROR_AUTHENTICATION:
                    break;               // 서버에서 사용자 인증 실패
                case ERROR_BAD_URL:
                    break;                           // 잘못된 URL
                case ERROR_CONNECT:
                    break;                          // 서버로 연결 실패
                case ERROR_FAILED_SSL_HANDSHAKE:
                    break;    // SSL handshake 수행 실패
                case ERROR_FILE:
                    break;                                  // 일반 파일 오류
                case ERROR_FILE_NOT_FOUND:
                    break;               // 파일을 찾을 수 없습니다
                case ERROR_HOST_LOOKUP:
                    break;           // 서버 또는 프록시 호스트 이름 조회 실패
                case ERROR_IO:
                    break;                              // 서버에서 읽거나 서버로 쓰기 실패
                case ERROR_PROXY_AUTHENTICATION:
                    break;   // 프록시에서 사용자 인증 실패
                case ERROR_REDIRECT_LOOP:
                    break;               // 너무 많은 리디렉션
                case ERROR_TIMEOUT:
                    break;                          // 연결 시간 초과
                case ERROR_TOO_MANY_REQUESTS:
                    break;     // 페이지 로드중 너무 많은 요청 발생
                case ERROR_UNKNOWN:
                    break;                        // 일반 오류
                case ERROR_UNSUPPORTED_AUTH_SCHEME:
                    break; // 지원되지 않는 인증 체계
                case ERROR_UNSUPPORTED_SCHEME:
                    break;          // URI가 지원되지 않는 방식
            }
            view.loadUrl("about:blank");
        }
    }

    public class CustomWebChromeClient extends WebChromeClient {
        @Override
        public void onProgressChanged(WebView view, int newProgress) {
            super.onProgressChanged(view, newProgress);

            if (newProgress == 10) {
                ToastManager.getInstance().showToast(context,"progress 10");
//                if (!mProgressDialog.isShowing()) {
//                    mProgressDialog.setMessage(mContext.getString(R.string.please_wait));
//                    mProgressDialog.setCancelable(false);
//                    mProgressDialog.show();
//                }
            }
            if (newProgress == 100) {
                ToastManager.getInstance().showToast(context,"progress 100");
//                if (mProgressDialog.isShowing()) {
//                    mProgressDialog.dismiss();
//                }
            }
        }

        @Override
        public boolean onJsAlert(WebView view, String url, String message, final JsResult result) {
//            if (!isFinishing()) {
//                if (mJsConfirmDialog != null) if (mJsConfirmDialog.isShowing()) return true;
//                mJsConfirmDialog = new CustomConfirmDialog(mContext, message, mContext.getString(R.string.confirm), new View.OnClickListener() {
//                    @Override
//                    public void onClick(View v) {
//                        mJsConfirmDialog.dismiss();
//                        result.confirm();
//                    }
//                });
//                mJsConfirmDialog.setCancelable(false);
//                mJsConfirmDialog.show();
//            }
            return true;
        }

        @Override
        public boolean onJsConfirm(WebView view, String url, String message, final JsResult result) {
//            if (!isFinishing()) {
//                if (mJsAlertDialog != null) if (mJsAlertDialog.isShowing()) return true;
//                mJsAlertDialog = new CustomAlertDialog(mContext, message, mContext.getString(R.string.no), mContext.getString(R.string.yes), new View.OnClickListener() {
//                    @Override
//                    public void onClick(View v) {
//                        mJsAlertDialog.dismiss();
//                        result.cancel();
//                    }
//                }, new View.OnClickListener() {
//                    @Override
//                    public void onClick(View v) {
//                        mJsAlertDialog.dismiss();
//                        result.confirm();
//                    }
//                });
//                mJsAlertDialog.setCancelable(false);
//                mJsAlertDialog.show();
//            }
            return true;
        }

        // 자바스크립트 에러 발생 시 로그 출력부
        public boolean onConsoleMessage(ConsoleMessage cm) {
            return true;
        }
    }
}