package com.example.smartbox_dup.screen.function.alarm;

import android.content.Context;
import android.content.Intent;
import android.media.Ringtone;
import android.media.RingtoneManager;
import android.os.Build;
import android.os.Bundle;
import android.os.Vibrator;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.example.smartbox_dup.R;
import com.example.smartbox_dup.utils.ByteArrayManager;

import chanyb.android.java.GlobalApplcation;

public class FullScreenAlarmActivity extends AppCompatActivity {
    Context mContext;
    Button activityRingDismiss;
    TextView text_medicine, text_time;
    Vibrator vibrator;
    Intent intentService;
    Ringtone ringtone;
    ByteArrayManager<Alarm> bcTransformer;

    Alarm alarm;
    long[] pattern;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        displayOnTheTop();

        setContentView(R.layout.activity_fullscrenn_alarm);
        mContext = this;
        bcTransformer = new ByteArrayManager<>();

        text_medicine = findViewById(R.id.text_medicine);
        text_time = findViewById(R.id.text_time);

        String title = getIntent().getStringExtra("title");
        String time = getIntent().getStringExtra("time");
        text_medicine.setText(title);
        text_time.setText(time);

        intentService = new Intent(this, AlarmService.class);

        activityRingDismiss = findViewById(R.id.activity_ring_dismiss);
        activityRingDismiss.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(getIntent().getStringExtra("activity") != null) {
                    try {
                        Class<?> someClass = Class.forName(getIntent().getStringExtra("activity"));
                        Intent myIntent = new Intent(GlobalApplcation.getContext(), someClass);
                        startActivity(myIntent);
                    } catch (ClassNotFoundException e) {
                        e.printStackTrace();
                    }
                }
                vibrator.cancel();
                ringtone.stop();
                finish();
                stopService(intentService);
            }
        });

        this.alarm = bcTransformer.getClassFromByteArray(getIntent().getByteArrayExtra("alarm"));


        pattern = new long[] {1000, 1000};
        vibrator = (Vibrator) getSystemService(Context.VIBRATOR_SERVICE);
        vibrator.vibrate(pattern, 0);

        //소리
        ringtone = RingtoneManager.getRingtone(getApplicationContext(), RingtoneManager.getDefaultUri(RingtoneManager.TYPE_ALARM));
        ringtone.play();


    }

    @Override
    protected void onResume() {
        vibrator.vibrate(pattern, 0);
        ringtone.play();
        super.onResume();
    }

    @Override
    protected void onDestroy() {
//        vibrator.cancel();
        super.onDestroy();
    }

    private void displayOnTheTop() {
        //타이틀바 없애기
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        WindowManager.LayoutParams layoutParams = new WindowManager.LayoutParams();
        layoutParams.flags = WindowManager.LayoutParams.FLAG_DIM_BEHIND;
        layoutParams.dimAmount = 0.7f;
        getWindow().setAttributes(layoutParams);
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_SHOW_WHEN_LOCKED
                // 키잠금 해제하기
                | WindowManager.LayoutParams.FLAG_DISMISS_KEYGUARD
                // 화면 켜기
                | WindowManager.LayoutParams.FLAG_TURN_SCREEN_ON
                | WindowManager.LayoutParams.FLAG_FULLSCREEN);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O_MR1) {
            setShowWhenLocked(true);
            setTurnScreenOn(true);
        } else {
            getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON | WindowManager.LayoutParams.FLAG_ALLOW_LOCK_WHILE_SCREEN_ON);
        }
    }
}
