package com.example.smartbox_dup.screen.home;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Point;
import android.os.Bundle;
import android.util.Log;
import android.view.Display;
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
    Display display;
    Point size;
    double limit_max_y;
    double limit_min_y;
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

        display = getWindowManager().getDefaultDisplay();  // in
        size = new Point();
        display.getRealSize(size); // or getSize(size)

        limit_max_y = size.y*0.9;
        limit_min_y = size.y*0.5;



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


        /* getActivity().getWindowManager().getDefaultDisplay() */ // in Fragment


        gestureDetector = new GestureDetector(context, new GestureDetector.OnGestureListener() {
            float preY;
            @Override
            public boolean onDown(MotionEvent motionEvent) { return false; }

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
                if(motionEvent1.getRawY() > limit_max_y || motionEvent1.getRawY() < limit_min_y) {
                    // 범위 밖
                    return false;
                }

                // bias 계산
                float new_bias = motionEvent1.getRawY() / size.y;


                ConstraintLayout.LayoutParams params = (ConstraintLayout.LayoutParams) map_overlay_lo.getLayoutParams();
                float current_bias = params.verticalBias;

                // bias 조정
                while(true) {
                    if (new_bias > current_bias) {
                        current_bias += 0.0001;

                        // 종료조건
                        if (current_bias > new_bias) break;
                    }
                    else {
                        current_bias -= 0.0001;

                        // 종료조건
                        if (current_bias < new_bias) break;
                    }

                    if(current_bias > 0.9) current_bias = (float) 0.9;
                    if(current_bias < 0.5) current_bias = (float) 0.5;

                    params.verticalBias = current_bias;
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            map_overlay_lo.setLayoutParams(params);
                        }
                    });
                }


                return false;
            }

            @Override
            public void onLongPress(MotionEvent motionEvent) { }

            @Override
            public boolean onFling(MotionEvent motionEvent, MotionEvent motionEvent1, float v, float v1) { return false; }
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
