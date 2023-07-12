package com.example.smartbox_dup.screen.function.file;

import android.Manifest;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.util.Log;
import android.widget.Button;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.biometric.BiometricPrompt;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import com.example.smartbox_dup.R;
import com.example.smartbox_dup.utils.FutureTaskRunner;
import com.example.smartbox_dup.utils.GlobalApplication;
import com.example.smartbox_dup.utils.PermissionManager;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.PrintStream;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;
import java.util.Scanner;
import java.util.TimeZone;
import java.util.concurrent.FutureTask;
import java.util.concurrent.atomic.AtomicReference;

public class FileTest extends AppCompatActivity {
    Button btn_1, btn_3;
    private Map<String, Integer> permissionInfo;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        setContentView(R.layout.activity_function_file);
        GlobalApplication.currentActivity = this;
        init();
        super.onCreate(savedInstanceState);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        for(int idx=0; idx<permissions.length; idx++) {
            Log.i("this", permissions[idx] + ": " + grantResults[idx]);
            permissionInfo.put(permissions[idx], grantResults[idx]);
        }
    }

    private void init() {
        permissionInfo = new HashMap<>();
        btn_1 = findViewById(R.id.btn_1);
        btn_1.setOnClickListener((view) -> {
            btn_1_action();
        });

        btn_3 = findViewById(R.id.btn_3);
        btn_3.setOnClickListener((view) -> {
            btn_3_action();
        });

    }

    private void btn_1_action() {
        // ExternalStorage 사용 시 권한 필요
        // 1. READ_EXTERNAL_STORAGE
        // 2. WRITE_EXTERNAL_STORAGE
        // ANDROID 11 (API LEVEL 30) 이상에서 사용할 수 없음!

        FutureTaskRunner<Boolean> futureTaskRunner = new FutureTaskRunner<>();
        futureTaskRunner.nextTask(() -> {
            runOnUiThread(() -> PermissionManager.getInstance().requestPermissions(new String[]{
                    Manifest.permission.READ_EXTERNAL_STORAGE,
                    Manifest.permission.WRITE_EXTERNAL_STORAGE
            }));

            while(true) {
                if(permissionInfo.keySet().size() == 2) break;
            }

            if(Build.VERSION_CODES.R <= Build.VERSION.SDK_INT) {
                return true;
            }

            if(permissionInfo.get(Manifest.permission.READ_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED) {
                Log.i("this", "READ_EXTERNAL_STORAGE: granted");
            } else {
                return false;
            }
            if(permissionInfo.get(Manifest.permission.WRITE_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED) {
                Log.i("this", "WRITE_EXTERNAL_STORAGE: granted");
            } else {
                return false;
            }
            return true;
        });

        futureTaskRunner.nextTask(() -> {
            writeLog("hello, world!");
            return true;
        });

        futureTaskRunner.start();


    }

    public void writeLog(String str) {
        if (true) {
            String FileName;
            String sFolder;

            TimeZone tz = TimeZone.getTimeZone("Asia/Seoul");
            Locale currentLocale = new Locale("KOREAN", "KOREA");

            SimpleDateFormat dFormat = new SimpleDateFormat("yyyyMMdd", currentLocale);
            dFormat.setTimeZone(tz);

            Date m_strTime = new Date();
            String m_DateF1 = dFormat.format(m_strTime);

            dFormat = new SimpleDateFormat("yyyyMMddHHmmss.SSS", currentLocale);
            dFormat.setTimeZone(tz);

            String m_DateF = dFormat.format(m_strTime);

            String basePath = Environment.getExternalStorageDirectory().getAbsolutePath();
            String dir = File.separator + "Download" + File.separator + "smbd" + File.separator;
            String srcPath = basePath + dir;

            m_DateF = m_DateF + "->" + str;
            FileName = srcPath + "data" + "_" + m_DateF1 + "s1.txt";
            sFolder = srcPath;
            File desti = new File(sFolder);
            if (!desti.exists()) {
                desti.mkdirs();
            }
            try {
                PrintStream ps = new PrintStream(new FileOutputStream(FileName, true));
                ps.println(m_DateF);
                System.out.println(m_DateF);
            } catch (Exception ex) {
                Log.e("this", "writeLog_Exception", ex);
            }
        }
    }

    public void btn_3_action() {
        readLog();
    }

    public void readLog() {
        String FileName;
        String sFolder;

        TimeZone tz = TimeZone.getTimeZone("Asia/Seoul");
        Locale currentLocale = new Locale("KOREAN", "KOREA");

        SimpleDateFormat dFormat = new SimpleDateFormat("yyyyMMdd", currentLocale);
        dFormat.setTimeZone(tz);

        Date m_strTime = new Date();
        String m_DateF1 = dFormat.format(m_strTime);

        dFormat = new SimpleDateFormat("yyyyMMddHHmmss.SSS", currentLocale);
        dFormat.setTimeZone(tz);

        String m_DateF = dFormat.format(m_strTime);

        String basePath = Environment.getExternalStorageDirectory().getAbsolutePath();
        String dir = File.separator + "Download" + File.separator + "smbd" + File.separator;
        String srcPath = basePath + dir;

        FileName = srcPath + "data" + "_" + m_DateF1 + "s1.txt";
        sFolder = srcPath;

        File file = new File(FileName);
        Scanner scanner = null;
        try {
            scanner = new Scanner(file);
            while (scanner.hasNextLine()) {
                String line = scanner.nextLine();
                Log.i("this", line);
            }
            scanner.close();
        } catch (FileNotFoundException e) {
            Log.e("this", "FileNotFoundException - readLog", e);
        }


    }
}
