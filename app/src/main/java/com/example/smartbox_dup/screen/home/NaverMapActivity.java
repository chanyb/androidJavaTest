package com.example.smartbox_dup.screen.home;

import android.os.Bundle;
import android.util.Log;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.example.smartbox_dup.R;
import com.naver.maps.map.MapView;

public class NaverMapActivity extends AppCompatActivity {
    private MapView mapView;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activty_navermap);


        mapView = findViewById(R.id.naver_map);


    }


}
