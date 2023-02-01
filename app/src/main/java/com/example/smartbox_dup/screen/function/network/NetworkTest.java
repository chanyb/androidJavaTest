package com.example.smartbox_dup.screen.function.network;

import android.Manifest;
import android.app.Activity;
import android.app.ActivityManager;
import android.app.NotificationManager;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.util.Log;
import android.widget.Button;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import com.example.smartbox_dup.R;
import com.example.smartbox_dup.screen.function.notification.NotificationService;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

import chanyb.android.java.GlobalApplcation;

public class NetworkTest extends AppCompatActivity {
    private Button btn_1, btn_2, btn_3;
    private String channelId;
    private Context mContext;
    private static String APK_DOWNLOAD_PATH = "http://apk-link14.kworks.co.kr/apk/smartdtg2/";
    private static final String APK_VERSION_CHECK_FILE = "smartdtg2.txt";

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        setContentView(R.layout.activity_function_network);
        init();
        super.onCreate(savedInstanceState);
    }

    private void init() {
        mContext = this;
        btn_1 = findViewById(R.id.btn_1);
        btn_1.setOnClickListener((view) -> {
            btn_1_action();
        });

        btn_2 = findViewById(R.id.btn_2);
        btn_2.setOnClickListener((view) -> {
            btn_2_action();
        });

        btn_3 = findViewById(R.id.btn_3);
        btn_3.setOnClickListener((view) -> {
            btn_3_action();
        });

    }



    private void btn_1_action() {
        //getFile 테스트하기
        GetFile gf = new GetFile();
        Thread thread = new Thread(new Runnable() {
            @Override
            public void run() {
                Log.i("this", "1");
                gf.TextDownload(APK_DOWNLOAD_PATH, APK_VERSION_CHECK_FILE, getAbsolutePath(mContext));
                Log.i("this", "2");
            }
        });

        thread.start();
    }

    private void btn_2_action() {
        File apkFile = new File(getAbsolutePath(mContext) + File.separator + APK_VERSION_CHECK_FILE);
        InputStreamReader reader = null;
        BufferedReader fin = null;

        try {
            if (apkFile.exists()) {
                reader = new InputStreamReader(new FileInputStream(apkFile), "UTF-8");
                fin = new BufferedReader(reader);
                String ss;
                while ((ss = fin.readLine()) != null) {
                    if (ss.indexOf("=") > 0) {
                        continue;
                    }

                    Log.i("this", "read: " + ss);
                }
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        } finally {
            try {
                if (reader != null) {
                    reader.close();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
            try {
                if (fin != null) {
                    fin.close();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    public void btn_3_action() {
//        ActivityCompat.requestPermissions(this, new String[]{
//                Manifest.permission.WRITE_EXTERNAL_STORAGE,
//                Manifest.permission.READ_EXTERNAL_STORAGE,
//        }, 0);
//        Log.i("this", getVersionName(this));
        gotoPlayStore();
    }


    public static class GetFile {

        public GetFile() {
        }

        // http 프로토콜 화일 다운(text)
        public static boolean TextDownload(String Url, String FileName, String sSaveFile) {
            // 다운 받을 화일이 위치한 서버 경로
            Log.i("this", "----");
            URL Downloadurl;
            boolean isComplete = false;
            try {
                Downloadurl = new URL(Url + FileName);

                // http 프로토콜 연결
                HttpURLConnection conn = (HttpURLConnection) Downloadurl.openConnection();
                conn.setConnectTimeout(3 * 1000);
                conn.setReadTimeout(3 * 1000);
                BufferedReader in = new BufferedReader(new InputStreamReader((InputStream) conn.getInputStream(), "UTF-8"));
                // 임시 폴더에 다운받음
                FileOutputStream fos = new FileOutputStream(sSaveFile + "/" + FileName);
                Log.i("this", sSaveFile + "/" + FileName);

                byte[] contentInBytes;
                String inputLine;
                while ((inputLine = in.readLine()) != null) {
                    String data = inputLine + "\r\n";
                    contentInBytes = data.getBytes();
                    fos.write(contentInBytes);
                }
                fos.flush();
                fos.close();
                isComplete = true;
            } catch (Exception ex) {
                ex.printStackTrace();
                Log.i("this", "----2");
                isComplete = false;
            }
            Log.i("this", "----3");
            return isComplete;
        }

        // http 프로토콜 화일 다운(content)
        public static boolean ContentDownload(String Url, String FileName, String sSaveFile) {
            // 다운 받을 화일이 위치한 서버 경로
            URL Downloadurl;
            boolean isComplete = false;
            try {
                Downloadurl = new URL(Url + FileName);

                // http 프로토콜 연결
                HttpURLConnection conn = (HttpURLConnection) Downloadurl.openConnection();

                byte[] buffer = new byte[1024];
                int length = 0;

                InputStream is = conn.getInputStream();
                // 임시 폴더에 다운받음
                FileOutputStream fos = new FileOutputStream(sSaveFile + "/" + FileName);

                while ((length = is.read(buffer)) >= 0) {
                    fos.write(buffer, 0, length);
                }
                fos.flush();
                fos.close();
                is.close();
                isComplete = true;
            } catch (Exception ex) {
                ex.printStackTrace();
                isComplete = false;
            }
            return isComplete;
        }
    }

    public String getAbsolutePath(Context mContext) {
        String sDownloadDir = "";
        try {
            String SDPath = getSDPath(mContext);
            String absolutePath;
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
                absolutePath = mContext.getExternalFilesDir(null).getAbsolutePath();
            } else {
                absolutePath = Environment.getExternalStorageDirectory().getAbsolutePath();
            }
            if ("unmounted".equals(SDPath)) {
                sDownloadDir = absolutePath + "2";
            } else {
                sDownloadDir = absolutePath;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return sDownloadDir;
    }

    public String getSDPath(Context mContext) {
        String ext = Environment.getExternalStorageState();
        String SDPath = "";
        if (ext.equals(Environment.MEDIA_MOUNTED)) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
                SDPath = mContext.getExternalFilesDir(null).getAbsolutePath();
            } else {
                SDPath = Environment.getExternalStorageDirectory().getAbsolutePath();
            }
        } else {
            SDPath = Environment.MEDIA_UNMOUNTED;
        }
        return SDPath;
    }

    public String getVersionName(Context context) {
        PackageInfo i = null;
        try {
            i = context.getPackageManager().getPackageInfo(context.getPackageName(), 0);
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }

        return i.versionName;
    }

    public void gotoPlayStore() {
        Intent intent = new Intent(Intent.ACTION_VIEW);
        intent.setData(Uri.parse("market://details?id=" + "com.kworks.smartdgt"));
        startActivity(intent);
    }
}
