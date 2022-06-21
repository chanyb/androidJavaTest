package com.example.smartbox_dup.screen.home;

import android.annotation.SuppressLint;
import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.View;
import android.widget.ImageView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.constraintlayout.widget.ConstraintSet;
import androidx.core.view.GestureDetectorCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.PagerSnapHelper;
import androidx.recyclerview.widget.RecyclerView;

import com.example.smartbox_dup.R;
import com.example.smartbox_dup.viewmodel.User;
import com.naver.maps.map.MapView;

public class NaverMapActivity extends AppCompatActivity {
    private Context context;
    private MapView mapView;
    private ImageView map_overlay_slide_button;
    private ConstraintLayout map_overlay_lo;
    private GestureDetector gestureDetector;
    private RecyclerView userItemView;
    private NaverMapOveralyAdapter naverMapOveralyAdapter;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activty_navermap);
        init();

        User user = new User();
        user.setUsername("병찬");
        user.setLocation("도안북로 54-53");
        user.setLocatedtime("15:44");
        naverMapOveralyAdapter.addItem(user);

        user.setUsername("병찬");
        user.setLocation("도안북로 54-53");
        user.setLocatedtime("15:44");

        naverMapOveralyAdapter.addItem(user);
        userItemView.setAdapter(naverMapOveralyAdapter);

        PagerSnapHelper pagerSnapHelper = new PagerSnapHelper();
        pagerSnapHelper.attachToRecyclerView(userItemView);
    }

    @SuppressLint("ClickableViewAccessibility")
    public void init() {
        this.context = this;
        mapView = findViewById(R.id.naver_map);
        map_overlay_lo = findViewById(R.id.map_overlay_lo);
        map_overlay_slide_button = findViewById(R.id.map_overlay_slide_button);
        userItemView = findViewById(R.id.userItemView);
        naverMapOveralyAdapter = new NaverMapOveralyAdapter();
        userItemView.setLayoutManager(new LinearLayoutManager(this, RecyclerView.HORIZONTAL, false));

        gestureDetector = new GestureDetector(context, new GestureDetector.OnGestureListener() {
            float preY;
            @Override
            public boolean onDown(MotionEvent motionEvent) {
                return false;
            }

            @Override
            public void onShowPress(MotionEvent motionEvent) {
                Log.i("this", "onShowPress");
            }

            @Override
            public boolean onSingleTapUp(MotionEvent motionEvent) {
                return false;
            }

            @Override
            public boolean onScroll(MotionEvent motionEvent, MotionEvent motionEvent1, float v, float v1) {
                if(Math.abs(preY - motionEvent1.getRawY()) > 60 || Math.abs(v1) < 2) {
                    // 오동작 방지
                    preY = motionEvent1.getRawY();
                    return false;
                }

                ConstraintLayout.LayoutParams params = (ConstraintLayout.LayoutParams) map_overlay_lo.getLayoutParams();

                v1 = v1 < 40 ? v1*3:v1;
                v1 = Math.abs(Math.abs(v1) > 10 ? v1:v1+10);
                v1 = v1 > 60 ? 60: v1;
                if(preY > motionEvent1.getRawY()) {
                    for(float i = v1; i>0; i--) {
                        if(params.verticalBias > 0.5) {
                            params.verticalBias -= 0.0003f;
                            runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    map_overlay_lo.setLayoutParams(params);
                                }
                            });
                        }
                    }
                }
                else {
                    for(float i = v1; i>0; i--) {
                        if(params.verticalBias < 0.9) {
                            params.verticalBias += 0.0003f;
                            runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    map_overlay_lo.setLayoutParams(params);
                                }
                            });
                        }
                    }

                }

                preY = motionEvent1.getRawY();

                return false;
            }

            @Override
            public void onLongPress(MotionEvent motionEvent) {

            }

            @Override
            public boolean onFling(MotionEvent motionEvent, MotionEvent motionEvent1, float v, float v1) {
                Log.i("this", "onFling");
                return false;
            }
        });

        map_overlay_lo.setOnTouchListener((view, motionEvent) -> {

            switch (motionEvent.getAction()) {
                case MotionEvent.ACTION_DOWN:
//                    gestureDetector.onTouchEvent(motionEvent);
                    break;
                case MotionEvent.ACTION_MOVE:
                    gestureDetector.onTouchEvent(motionEvent);
                    break;
                case MotionEvent.ACTION_UP:
                    break;
            }


            return true;
        });
    }


}
