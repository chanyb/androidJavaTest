package com.example.smartbox_dup.screen.function.printonothers;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.graphics.PixelFormat;
import android.os.Build;
import android.os.IBinder;
import android.os.PowerManager;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.WindowManager;
import android.widget.ImageButton;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.core.app.NotificationCompat;

import com.example.smartbox_dup.R;
import com.example.smartbox_dup.screen.intro.IntroActivity;

import java.util.Calendar;

import chanyb.android.java.GlobalApplcation;

public class FloatingService extends Service {

    public static View mFloatingView;
    public static WindowManager windowManager;
    private WindowManager.LayoutParams layoutParams;
    private int type;
    private ImageButton imgbtn;
    private PowerManager.WakeLock wakeLock;
    private Context mContext;
    private NotificationManager mNotificationManager;
    private NotificationCompat.Builder builder;
    @Override
    public void onCreate() {
        super.onCreate();
        mContext = this;
        acquireWakeLock(mContext);
        initFloadtingView();
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        PendingIntent pIntent = PendingIntent.getActivity(mContext, 0, intent, PendingIntent.FLAG_UPDATE_CURRENT | PendingIntent.FLAG_MUTABLE);

        builder = new NotificationCompat.Builder(mContext, "floating");
        builder.setOngoing(true)
                .setSmallIcon(R.mipmap.ic_launcher)
                .setContentText(getString(R.string.app_name) + getString(R.string.app_ing))
                .setPriority(NotificationManager.IMPORTANCE_MIN)
                .setCategory(Notification.CATEGORY_SERVICE)
                .setContentIntent(pIntent)
                .build();


        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            mNotificationManager = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
            startForeground(3, builder.build());
        }
        return super.onStartCommand(intent, flags, startId);
    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        if (mFloatingView != null) {
            if (windowManager != null) {
                windowManager.removeView(mFloatingView);
                windowManager = null;
            }
            mFloatingView = null;
        }

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            stopForeground(true); // Foreground service 종료
        }

        releaseWakeLock();
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
        mFloatingView.setVisibility(View.VISIBLE);

        //height = windowManager.getDefaultDisplay().getHeight();
        //width = windowManager.getDefaultDisplay().getWidth();

        imgbtn = (ImageButton) mFloatingView.findViewById(R.id.imgbtn);
        // 사용자의 터치 동작을 사용하여 플로팅 뷰를 드래그하여 이동
        imgbtn.setOnTouchListener(onTouchListener);
    }
}
