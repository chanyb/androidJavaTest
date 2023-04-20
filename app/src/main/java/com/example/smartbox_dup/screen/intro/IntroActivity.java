package com.example.smartbox_dup.screen.intro;

import android.app.NotificationManager;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.PackageManager;
import android.content.res.Configuration;
import android.net.ConnectivityManager;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.util.Log;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.example.smartbox_dup.R;
import com.example.smartbox_dup.broadcastreceiver.BroadcastManager;
import com.example.smartbox_dup.broadcastreceiver.NetworkBroadcastReceiver;
import com.example.smartbox_dup.screen.function.FunctionListActivity;
import com.example.smartbox_dup.screen.login.LoginActivity;
import com.example.smartbox_dup.utils.ActivitySwitchManager;
import com.example.smartbox_dup.utils.AudioManager;
import com.example.smartbox_dup.utils.FutureTaskRunner;
import com.example.smartbox_dup.utils.GlobalApplication;
import com.example.smartbox_dup.utils.PermissionManager;

import java.util.Locale;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.FutureTask;


public class IntroActivity extends AppCompatActivity {
    NetworkBroadcastReceiver networkBroadcastReceiver;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_intro);

        Log.i("this", "Intro - onCreate");
    }

    public void set_language_code(String language){
        Locale locale = new Locale(language);
        Locale.setDefault(locale);
        Configuration config = new Configuration();
        config.locale = locale;
        getBaseContext().getResources().updateConfiguration(config, getBaseContext().getResources().getDisplayMetrics());
    }

    @Override
    protected void onResume() {
        super.onResume();
        checkpoints();
//        if(getIntent().getStringExtra("lang") == null) {
//            Log.i("this", "lang is null");
//            set_language_code("en");
//            Handler mHandler = new Handler(Looper.getMainLooper());
//            mHandler.postDelayed(() -> {
//                runOnUiThread(() -> {
//                    set_language_code("kr");
//                    Intent intent = new Intent(this, IntroActivity.class);
//                    intent.putExtra("lang", "kr");
//                    startActivity(intent);
//                    overridePendingTransition(0, 0);
//                    finish();
//                });
//            }, 1000);
//        } else {
//            Log.i("this", "lang is not null");
//        }

    }

    private void nextPage() {
        Intent nextPageIntent = new Intent(this, LoginActivity.class);
        startActivity(nextPageIntent);
        finish();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }

    @Override
    protected void onPause() {
        BroadcastManager.getInstance().unregister(networkBroadcastReceiver);
        super.onPause();
    }

    private void checkpoints() {
        // 브로드캐스트 등록
        networkBroadcastReceiver = new NetworkBroadcastReceiver();
        IntentFilter intentFilter = new IntentFilter(ConnectivityManager.CONNECTIVITY_ACTION);
        BroadcastManager.getInstance().register(networkBroadcastReceiver, intentFilter);

        // foreground/background 상태 리스너 등록
        setGroundStateListener();

        // broadcast 테스트
        BroadcastManager.getInstance().sendBroadcast_test();

        // Ringermode 테스트


        // drawOverlay

        // futureTask 테스트
        futureTaskManager_test();
    }

    private void setGroundStateListener() {
        GlobalApplication.getContext().addListener(new GlobalApplication.Listener() {
            @Override
            public void onBecameForeground() {
                Log.i("this", "onBecameForeground");
            }
            @Override
            public void onBecameBackground() {
                Log.i("this", "onBecameBackground");
            }
        });
    }

    private void setRingerMode(AudioManager.State state) {
        AudioManager.getInstance().setRingerMode(state);
    }

    private void futureTask_test() {
        new Thread(() -> {
            FutureTask<String> futureTask = new FutureTask<>(new Callable() {
                @Override
                public String call() throws Exception {
                    Log.i("this", "작업 시작");
                    Thread.sleep(10000);
                    return "Alicd";
                }
            });

            futureTask.run();

            // 리턴 값이 와야 아래로 진행됨
            try {
                Log.i("this", futureTask.get());
            } catch (ExecutionException e) {
                e.printStackTrace();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }).start();

    }

    private void futureTaskManager_test() {
        FutureTaskRunner<Boolean> futureTaskRunner = new FutureTaskRunner<>();

//        futureTaskRunner.nextTask(() -> {
//            runOnUiThread(() -> {
//                PermissionManager.getInstance().overlayPermissionCheck();
//            });
//            while(true) {
//                if(!PermissionManager.getInstance().drawOverlaysRequiredButNotGranted()) break;
//            }
//            return true;
//        });

//        futureTaskRunner.nextTask(() -> {
//            runOnUiThread(() -> {
//                setRingerMode(AudioManager.State.SILENT);
//            });
//
//            NotificationManager notificationManager;
//            notificationManager = (NotificationManager) GlobalApplication.getContext().getSystemService(Context.NOTIFICATION_SERVICE);
//
//            while(true) {
//                if(notificationManager.isNotificationPolicyAccessGranted() && GlobalApplication.getContext().isForeground()) break;
//            }
//
//            return true;
//        });

//        futureTaskRunner.setCallback(res -> {
//            startActivity(new Intent(this, FunctionListActivity.class));
//            finish();
//            return true;
//        });


        // 액티비티 활성화? 를 통한 앱 아이콘 변경
//        futureTaskRunner.nextTask(() -> {
//            int status = getPackageManager().getComponentEnabledSetting(new ComponentName(GlobalApplication.getContext(), IntroActivity.class));
//
//            if(status == PackageManager.COMPONENT_ENABLED_STATE_ENABLED) {
//                getPackageManager().setComponentEnabledSetting(new ComponentName(GlobalApplication.getContext(), IntroActivity.class), PackageManager.COMPONENT_ENABLED_STATE_DISABLED, PackageManager.DONT_KILL_APP);
//                getPackageManager().setComponentEnabledSetting(new ComponentName(GlobalApplication.getContext(), IntroActivityAlias.class), PackageManager.COMPONENT_ENABLED_STATE_ENABLED, PackageManager.DONT_KILL_APP);
//            }
//            else {
//                getPackageManager().setComponentEnabledSetting(new ComponentName(GlobalApplication.getContext(), IntroActivity.class), PackageManager.COMPONENT_ENABLED_STATE_ENABLED, PackageManager.DONT_KILL_APP);
//                getPackageManager().setComponentEnabledSetting(new ComponentName(GlobalApplication.getContext(), IntroActivityAlias.class), PackageManager.COMPONENT_ENABLED_STATE_DISABLED, PackageManager.DONT_KILL_APP);
//            }
//            return true;
//        });

        futureTaskRunner.nextTask(() -> {
            startActivity(new Intent(this, FunctionListActivity.class));

            finish();
            return true;
        });

        futureTaskRunner.start();
    }
}
