package com.example.smartbox_dup.screen.function.printonothers;

import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.PixelFormat;
import android.os.Build;
import android.os.Bundle;
import android.os.PowerManager;
import android.util.Log;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.NotificationCompat;

import com.example.smartbox_dup.R;
import com.example.smartbox_dup.screen.intro.IntroActivity;
import com.example.smartbox_dup.utils.PermissionManager;

import java.util.Calendar;

import chanyb.android.java.GlobalApplcation;

public class PrintOnOtherAppTest extends AppCompatActivity {

    public static View mFloatingView;
    public static WindowManager windowManager;
    private WindowManager.LayoutParams layoutParams;
    private int type;
    private ImageButton imgbtn;
    private PowerManager.WakeLock wakeLock;
    private Context mContext;
    private NotificationManager mNotificationManager;
    private NotificationCompat.Builder builder;


    Button btn_1;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        setContentView(R.layout.activity_function_print_on_other_app);
//        acquireWakeLock(this);
        init();
        super.onCreate(savedInstanceState);
    }

    @Override
    protected void onResume() {
        super.onResume();
        if(PermissionManager.getInstance().drawOverlaysRequiredButNotGranted()) {
            PermissionManager.getInstance().overlayPermissionCheck();
        } else {
            PermissionManager.getInstance().overlayPermissionCheck();
            initFloadtingView();
        }
    }

    @Override
    protected void onDestroy() {
//        releaseWakeLock();
        super.onDestroy();
    }

    private void init() {
        btn_1 = findViewById(R.id.btn_1);
        btn_1.setOnClickListener((v) -> btn_1_action());
    }

    private void btn_1_action() {
        // use service
        createNotificationChannel("floating", NotificationCompat.PRIORITY_MAX);
        Intent intent = new Intent(PrintOnOtherAppTest.this, FloatingService.class);
        startService(intent);
    }

    private void btn_1_action2() {
        // not user service

    }

    public void createNotificationChannel(String channelId, int importance) {
        // Create the NotificationChannel, but only on API 26+ because
        // the NotificationChannel class is new and not in the support library
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            NotificationChannel channel = new NotificationChannel(channelId, channelId, importance); // id, name, importance
            channel.enableLights(true);
            channel.setLightColor(Color.GREEN);
            channel.enableVibration(true);
            channel.setVibrationPattern(new long[]{1000, 2000, 1000, 2000});
            channel.setSound(null, null);
            // Register the channel with the system; you can't change the importance
            // or other notification behaviors after this
            NotificationManager notificationManager = GlobalApplcation.getContext().getSystemService(NotificationManager.class);
            notificationManager.createNotificationChannel(channel);
        }
    }



    private void acquireWakeLock(Context context) {
        if (wakeLock == null) {
            PowerManager pm = (PowerManager) context.getSystemService(Context.POWER_SERVICE);
            wakeLock = pm.newWakeLock(PowerManager.PARTIAL_WAKE_LOCK, "" + getPackageName());
            if (wakeLock != null) {
                wakeLock.acquire();
            }
        }
    }

    private void releaseWakeLock() {
        if (wakeLock != null) {
            wakeLock.release();
            wakeLock = null;
        }
    }

    View.OnTouchListener onTouchListener = new View.OnTouchListener() {
        private long startCkickTime;
        private int initialx, initialy;
        private float initialTouchX, initialTouchY;
        private int maxClickDuration = 100;

        @Override
        public boolean onTouch(View v, MotionEvent motionEvent) {
            switch (motionEvent.getAction()) {
                case MotionEvent.ACTION_DOWN:
                    startCkickTime = Calendar.getInstance().getTimeInMillis();
                    //imageClose.setVisibility(View.VISIBLE);
                    // 초기 위치 기억
                    initialx = layoutParams.x;
                    initialy = layoutParams.y;
                    //터치 위치 좌표 얻기
                    initialTouchX = motionEvent.getRawX();
                    initialTouchY = motionEvent.getRawY();
                    return true;

                case MotionEvent.ACTION_UP:
                    long clickDuration = Calendar.getInstance().getTimeInMillis() - startCkickTime;
                    //imageClose.setVisibility(view.GONE);
                    // 초기 좌표와 현재 좌표의 차이 가져 오기
                    layoutParams.x = initialx + (int) (initialTouchX - motionEvent.getRawX());
                    layoutParams.y = initialy + (int) (motionEvent.getRawY() - initialTouchY);
                    // 사용자가 플로팅 위젯을 제거 이미지로 끌어다 놓으면 서비스를 중지합니다.
                        /*if (clickDuration >= MAX_CLICK_DURATION) {
                            // 제거 이미지 주변 거리
                            if (layoutParams.y > (height * 0.6)) {
                                stopSelf();
                            }
                        }*/
                    if (clickDuration <= maxClickDuration) {
                        Toast.makeText(mContext, getString(R.string.app_name) + getString(R.string.app_ing), Toast.LENGTH_SHORT).show();
                        Intent intent = new Intent(mContext, IntroActivity.class);
                        intent.setAction(Intent.ACTION_MAIN);
                        intent.addCategory(Intent.CATEGORY_LAUNCHER);
                        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                        startActivity(intent);
                    }

                    return true;

                case MotionEvent.ACTION_MOVE:
                    // 초기 좌표와 현재 좌표의 차이 가져 오기
                    layoutParams.x = initialx + (int) (initialTouchX - motionEvent.getRawX());
                    layoutParams.y = initialy + (int) (motionEvent.getRawY() - initialTouchY);
                    // 새로운 X 및 Y 좌표로 레이아웃 업데이트
                    windowManager.updateViewLayout(mFloatingView, layoutParams);
                        /*if (layoutParams.y > (height * 0.6)) {
                            imageClose.setImageResource(R.drawable.fcm_icon);
                        } else {
                            imageClose.setImageResource(R.drawable.fcm_icon);
                        }*/
                    return true;
            }
            return false;
        }
    };

    private void initFloadtingView() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            type = WindowManager.LayoutParams.TYPE_APPLICATION_OVERLAY;
        } else {
            type = WindowManager.LayoutParams.TYPE_PHONE;
        }
        // 우리가 만든 플로팅 뷰 레이아웃 확장
        mFloatingView = LayoutInflater.from(this).inflate(R.layout.layout_widget, null);
        layoutParams = new WindowManager.LayoutParams(
                (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 88, getResources().getDisplayMetrics()),
                (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 114, getResources().getDisplayMetrics()),
                type,
                WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE,
                PixelFormat.TRANSLUCENT);

        // 보기 위치 지정
        // 처음에는보기가 오른쪽 상단 모서리에 추가되며 필요에 따라 x-y 좌표를 변경
        layoutParams.gravity = Gravity.TOP | Gravity.RIGHT;
        layoutParams.x = 0;
        layoutParams.y = 100;

        /*WindowManager.LayoutParams imageParams = new WindowManager.LayoutParams(140, 140,
                LAYOUT_FLAG_TYPE,
                WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE,
                PixelFormat.TRANSLUCENT);
        imageParams.gravity = Gravity.BOTTOM | Gravity.CENTER;
        imageParams.y = 100;*/

        windowManager = (WindowManager) getSystemService(WINDOW_SERVICE);
        //imageClose = new ImageView(mContext);
        //imageClose.setImageResource(R.drawable.fcm_icon);
        //imageClose.setVisibility(View.INVISIBLE);
        //windowManager.addView(imageClose, imageParams);
        windowManager.addView(mFloatingView, layoutParams);
        mFloatingView.setVisibility(View.GONE);

        //height = windowManager.getDefaultDisplay().getHeight();
        //width = windowManager.getDefaultDisplay().getWidth();

        imgbtn = (ImageButton) mFloatingView.findViewById(R.id.imgbtn);
        // 사용자의 터치 동작을 사용하여 플로팅 뷰를 드래그하여 이동
        imgbtn.setOnTouchListener(onTouchListener);
    }
}
