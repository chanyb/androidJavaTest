package com.example.smartbox_dup.screen.intro;

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
import com.example.smartbox_dup.screen.login.LoginActivity;
import com.example.smartbox_dup.utils.AudioManager;
import com.example.smartbox_dup.utils.GlobalApplcation;

public class IntroActivity extends AppCompatActivity {
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_intro);
    }

    @Override
    protected void onResume() {
        super.onResume();
        checkpoints();
//        nextPage();
    }

    private void nextPage() {
        Intent nextPageIntent = new Intent(this, LoginActivity.class);
        startActivity(nextPageIntent);
        finish();
    }

    private void checkpoints() {
        // 브로드캐스트 등록
        NetworkBroadcastReceiver networkBroadcastReceiver = new NetworkBroadcastReceiver();
        IntentFilter intentFilter = new IntentFilter(ConnectivityManager.CONNECTIVITY_ACTION);
        intentFilter.addAction("com.example.smartbox_dup.firstBroadcast");
        BroadcastManager.getInstance().register(networkBroadcastReceiver, intentFilter);

        // foreground/background 상태 리스너 등록
        setGroundStateListener();

        // broadcast 테스트
        BroadcastManager.getInstance().sendBroadcast_test();

        // Ringermode 테스트
        setRingerMode(AudioManager.State.SILENT);

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
        AudioManager.getInstance().setRingerMode(this, state);
    }
}
