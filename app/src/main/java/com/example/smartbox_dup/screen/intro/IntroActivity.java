package com.example.smartbox_dup.screen.intro;

import android.app.NotificationManager;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.net.ConnectivityManager;
import android.os.Bundle;
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
import com.example.smartbox_dup.utils.PermissionManager;

import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.FutureTask;

import chanyb.android.java.GlobalApplcation;
import chanyb.android.java.ToastManager;

public class IntroActivity extends AppCompatActivity {
    NetworkBroadcastReceiver networkBroadcastReceiver;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_intro);
    }

    @Override
    protected void onResume() {
        super.onResume();
        checkpoints();
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
        GlobalApplcation.getContext().addListener(new GlobalApplcation.Listener() {
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
            FutureTask<String> futureTask = new FutureTask<>(new Callable<String>() {
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
        FutureTaskRunner futureTaskRunner = new FutureTaskRunner();

        futureTaskRunner.nextTask(() -> {
            runOnUiThread(() -> {
                PermissionManager.getInstance().overlayPermissionCheck();
            });
            while(true) {
                if(!PermissionManager.getInstance().drawOverlaysRequiredButNotGranted()) break;
            }
            return true;
        });

        futureTaskRunner.nextTask(() -> {
            runOnUiThread(() -> {
                setRingerMode(AudioManager.State.SILENT);
            });

            NotificationManager notificationManager;
            notificationManager = (NotificationManager) GlobalApplcation.getContext().getSystemService(Context.NOTIFICATION_SERVICE);

            while(true) {
                if(notificationManager.isNotificationPolicyAccessGranted()) break;
            }

            return true;
        });

        futureTaskRunner.nextTask(() -> {
//            ActivitySwitchManager.getInstance().changeActivity(GlobalApplcation.currentActivity, LoginActivity.class, true);
            ActivitySwitchManager.getInstance().changeActivity(GlobalApplcation.currentActivity, FunctionListActivity.class, true);
            return true;
        });

        futureTaskRunner.start();
        ToastManager.getInstance().showToast("hello, world!!~");
    }
}
