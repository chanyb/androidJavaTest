package com.example.smartbox_dup.screen.function.sensor;

import android.Manifest;
import android.content.ContentResolver;
import android.content.ContentUris;
import android.content.ContentValues;
import android.content.Context;
import android.content.pm.ActivityInfo;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.net.Uri;
import android.os.Bundle;
import android.provider.CalendarContract;
import android.util.Log;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import com.example.smartbox_dup.R;
import com.example.smartbox_dup.utils.DatetimeManager;
import com.example.smartbox_dup.utils.DeviceRotationManager;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.Calendar;
import java.util.Locale;

import chanyb.android.java.GlobalApplcation;

public class SensorTest extends AppCompatActivity {
    private static final int RADIAN_TO_DEGREE = -57;
    private SensorManager sensorManager;
    private Sensor sensor;
    private SensorEventListener sensorEventListener;
    private Button btn_1, btn_2, btn_3, btn_4, btn_5;
    private TextView txt_x, txt_y, txt_z;
    private Sensor accelSensor;
    private SensorEventListener accelSensorEventListener;
    private long lastTimestamp;
    private float[] lastAcceleration;
    private static final float NS2S = 1.0f / 1000000000.0f;
    private float currentSpeed_x, currentSpeed_y, currentSpeed_z;
    private LocationManager locationManager;
    private LocationListener locationListener = new LocationListener() {
        @Override
        public void onLocationChanged(@NonNull Location location) {
            double latitude = location.getLatitude();
            double longitude = location.getLongitude();
            JSONObject obj = DatetimeManager.getInstance().getSystemDateTime();
            try {
                txt_x.setText(obj.getString("datetime"));
            } catch (JSONException e) {
                e.printStackTrace();
            }
            txt_y.setText(latitude + "/" + longitude);
            txt_z.setText(location.getSpeed() + "/" + location.getAltitude());
        }
    };

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        setContentView(R.layout.activity_function_sensor);
        init();
        super.onCreate(savedInstanceState);
    }

    private void init() {
        locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
        sensorManager = (SensorManager) getSystemService(Context.SENSOR_SERVICE);

        btn_1 = findViewById(R.id.btn_1);
        btn_1.setOnClickListener((view) -> {
            btn_1_action();
        });

        btn_2 = findViewById(R.id.btn_2);
        btn_2.setOnClickListener((view) -> {
            btn_2_action();
        });

        btn_3 = findViewById(R.id.btn_3);
        btn_3.setOnClickListener((v) -> btn_3_action());

        btn_4 = findViewById(R.id.btn_4);
        btn_4.setOnClickListener((v) -> btn_4_action());

        btn_5 = findViewById(R.id.btn_5);
        btn_5.setOnClickListener((v) -> btn_5_action());

        txt_x = findViewById(R.id.txt_x);
        txt_y = findViewById(R.id.txt_y);
        txt_z = findViewById(R.id.txt_z);
    }


    private void btn_1_action() {
        DeviceRotationManager.getInstance().registerListener(this, new DeviceRotationManager.DeviceRotateListener() {
            @Override
            public void onPortrait() {
                Log.i("this", "onPortrait");
            }

            @Override
            public void onPortraitReverse() {
                Log.i("this", "onPortraitReverse");
            }

            @Override
            public void onLandscape() {
                Log.i("this", "onLandscape");
            }

            @Override
            public void onLandscapeReverse() {
                Log.i("this", "onLandscapeReverse");
            }
        });
    }

    private void btn_2_action() {
    }

    private void btn_3_action() {
        // 가속도 센서테스트
        accelSensor = sensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);
        currentSpeed_x = currentSpeed_y = currentSpeed_z = -99f;
        lastTimestamp = System.currentTimeMillis();

        accelSensorEventListener = new SensorEventListener() {
            @Override
            public void onSensorChanged(SensorEvent event) {

                if (currentSpeed_x == -99f) {
                    // init
                    currentSpeed_x = event.values[0];
                    currentSpeed_y = event.values[1];
                    currentSpeed_z = event.values[2];
                    return;
                }
                float acceleration_x = event.values[0]; // Get the acceleration value along the x-axis
                float acceleration_y = event.values[0]; // Get the acceleration value along the x-axis
                float acceleration_z = event.values[0]; // Get the acceleration value along the x-axis

                float timeDelta = (event.timestamp - lastTimestamp) / 1000000000.0f;

                currentSpeed_x = currentSpeed_x + acceleration_x * timeDelta;
                currentSpeed_y = currentSpeed_y + acceleration_y * timeDelta;
                currentSpeed_z = currentSpeed_z + acceleration_z * timeDelta;

                Log.i("this", currentSpeed_x + " / " + currentSpeed_y + " / " + currentSpeed_z);

            }

            @Override
            public void onAccuracyChanged(Sensor sensor, int accuracy) {
                Log.i("this", "onAccuracyChanged: " + accuracy);
            }
        };

        sensorManager.registerListener(accelSensorEventListener, accelSensor, SensorManager.SENSOR_DELAY_NORMAL);
    }

    private void btn_4_action() {
        // 자이로 센서테스트
    }

    private float calculateSpeed(float[] acceleration, float[] lastAcceleration, float timeInterval, float currentSpeed) {
        float deltaX = ((acceleration[0] + lastAcceleration[0]) / 2) * timeInterval * timeInterval;
        float deltaY = ((acceleration[1] + lastAcceleration[1]) / 2) * timeInterval * timeInterval;
        float deltaZ = ((acceleration[2] + lastAcceleration[2]) / 2) * timeInterval * timeInterval;
        float deltaSpeed = (float) Math.sqrt(deltaX * deltaX + deltaY * deltaY + deltaZ * deltaZ);
        return currentSpeed + deltaSpeed;
    }

    private void btn_5_action() {
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.
            Toast.makeText(this, "permission error", Toast.LENGTH_SHORT).show();
            return;
        }
        locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 0, 0, locationListener);
    }
}
