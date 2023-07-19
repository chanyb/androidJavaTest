package com.example.smartbox_dup.screen.function.ftp;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.Looper;
import android.os.PersistableBundle;
import android.util.Log;
import android.widget.Button;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import com.example.smartbox_dup.R;
import com.example.smartbox_dup.utils.DatetimeManager;
import com.example.smartbox_dup.utils.LogUpload;

import org.apache.commons.net.ftp.FTPClient;
import org.json.JSONException;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.PrintStream;
import java.nio.file.Files;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;
import java.util.Scanner;
import java.util.StringTokenizer;
import java.util.TimeZone;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;

public class FtpTest extends AppCompatActivity {
    Context mContext;
    Handler mHandler;
    Button btn_1, btn_2;
    String folderParentPath;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_function_ftp);
        mContext = this;
        mHandler = new Handler(Looper.getMainLooper());
        init();

    }

    private void init() {
        btn_1 = findViewById(R.id.btn_1);
        btn_1.setOnClickListener((v) -> {
            Thread thread = new Thread() {
                @Override
                public void run() {
                    
                }
            };
            thread.start();
        });

        btn_2 = findViewById(R.id.btn_2);
        btn_2.setOnClickListener((v) -> {
            LogUpload logUpload = new LogUpload();
            logUpload.zipAndUpload(Environment.getExternalStorageDirectory().getAbsolutePath() + File.separator + "Download", "behavior_pattern", "test");
        });

    }


    public void writeLog(String fileName, String str) {
        if (true) {
            String FileName;
            String sFolder;

            TimeZone tz = TimeZone.getTimeZone("Asia/Seoul");
            Locale currentLocale = new Locale("KOREAN", "KOREA");

            SimpleDateFormat dFormat = new SimpleDateFormat("yyyyMMdd", currentLocale);
            dFormat.setTimeZone(tz);

            Date m_strTime = new Date();

            dFormat = new SimpleDateFormat("yyyyMMddHHmmss.SSS", currentLocale);
            dFormat.setTimeZone(tz);


            String basePath = Environment.getExternalStorageDirectory().getAbsolutePath();
            String dir = File.separator + "Download" + File.separator + "behavior_pattern" + File.separator;
            String srcPath = basePath + dir;

            FileName = srcPath + fileName + ".txt";
            sFolder = srcPath;
            File desti = new File(sFolder);
            if (!desti.exists()) {
                desti.mkdirs();
            }
            try {
                PrintStream ps = new PrintStream(new FileOutputStream(FileName, true));
                ps.println(str);
            } catch (Exception ex) {
                Log.e("this", "writeLog - Exception", ex);
//                ex.printStackTrace();
            }
        }
    }

    public void makeFile(String fileName) {
        StringBuilder sb = new StringBuilder();
        for(int i=0; i<10000; i++) {
            sb.append("asdfsadfdsafdsafadsfjla;kdsfjklas;dfjklasd;fjdskalfqweropqweriopqweuriopqweuriopqwruioqweruwqvuwuwnuwenuvwioprwiorvnevwqpoiqwiopnuvreqnurqwiopneviqo1438903248912-4389891-45uiopqwiopwqvriopqewioprvwriopqwvnuiopqwevuiopqwrvnuiopqwnuioqpwvnuqwpvrunioqwevrnuiopqwevnuipqwevnuipqwvrnuiop\n");
            sb.append("asdfsadfdsafdsafadsfjla;kdsfjklas;dfjklasd;fjdskalfqweropqweriopqweuriopqweuriopqwruioqweruwqvuwuwnuwenuvwioprwiorvnevwqpoiqwiopnuvreqnurqwiopneviqo1438903248912-4389891-45uiopqwiopwqvriopqewioprvwriopqwvnuiopqwevuiopqwrvnuiopqwnuioqpwvnuqwpvrunioqwevrnuiopqwevnuipqwevnuipqwvrnuiop\n");
            sb.append("asdfsadfdsafdsafadsfjla;kdsfjklas;dfjklasd;fjdskalfqweropqweriopqweuriopqweuriopqwruioqweruwqvuwuwnuwenuvwioprwiorvnevwqpoiqwiopnuvreqnurqwiopneviqo1438903248912-4389891-45uiopqwiopwqvriopqewioprvwriopqwvnuiopqwevuiopqwrvnuiopqwnuioqpwvnuqwpvrunioqwevrnuiopqwevnuipqwevnuipqwvrnuiop\n");
            sb.append("asdfsadfdsafdsafadsfjla;kdsfjklas;dfjklasd;fjdskalfqweropqweriopqweuriopqweuriopqwruioqweruwqvuwuwnuwenuvwioprwiorvnevwqpoiqwiopnuvreqnurqwiopneviqo1438903248912-4389891-45uiopqwiopwqvriopqewioprvwriopqwvnuiopqwevuiopqwrvnuiopqwnuioqpwvnuqwpvrunioqwevrnuiopqwevnuipqwevnuipqwvrnuiop\n");
            sb.append("asdfsadfdsafdsafadsfjla;kdsfjklas;dfjklasd;fjdskalfqweropqweriopqweuriopqweuriopqwruioqweruwqvuwuwnuwenuvwioprwiorvnevwqpoiqwiopnuvreqnurqwiopneviqo1438903248912-4389891-45uiopqwiopwqvriopqewioprvwriopqwvnuiopqwevuiopqwrvnuiopqwnuioqpwvnuqwpvrunioqwevrnuiopqwevnuipqwevnuipqwvrnuiop\n");
        }
        writeLog(fileName, sb.toString());
    }
}