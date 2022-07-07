package com.example.smartbox_dup;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Adapter;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;


public class MapsActivity extends AppCompatActivity implements OnMapReadyCallback {
    private TextView textView;
    public TextView button;
    private RecyclerView recyclerView;
    private adapter mAdapter;
    private GoogleMap mMap;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_map);
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);


        textView = findViewById(R.id.ev_id);
        button = (TextView) findViewById(R.id.btn_confirm);
        // click listener
        button.setOnClickListener(new Button.OnClickListener() {

            @Override
            public void onClick(View view) {
                String tmp = String.valueOf(textView.getText());

                String res[] = tmp.split(",");

                mMap.addMarker(new MarkerOptions().position(new LatLng(Double.parseDouble(res[0]), Double.parseDouble(res[1]))));
                mAdapter.setArrayData(tmp);
                recyclerView.setAdapter(mAdapter);
                textView.setText("");
            }
        });

        recyclerView = findViewById(R.id.recyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(this, RecyclerView.VERTICAL, false));

        mAdapter = new adapter();


        recyclerView.setAdapter(mAdapter);




    }

    @Override
    public void onMapReady(@NonNull GoogleMap googleMap) {
        mMap = googleMap;
    }
}
