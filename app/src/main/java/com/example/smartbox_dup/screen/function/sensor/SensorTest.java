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
import android.hardware.TriggerEventListener;
import android.location.GnssStatus;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.PowerManager;
import android.provider.CalendarContract;
import android.util.Log;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
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


public class SensorTest extends AppCompatActivity {
    private static final int RADIAN_TO_DEGREE = -57;
    private SensorManager sensorManager;
    private Sensor sensor;
    private SensorEventListener sensorEventListener;
    private Button btn_1, btn_2, btn_3, btn_4, btn_5, btn_6, btn_7, btn_8, btn_9, btn_10;
    private TextView txt_x, txt_y, txt_z;
    private Sensor accelSensor, gravitySensor, stepCounterSensor, stepDetectorSensor, significationMotionSensor, orientationSensor;
    private SensorEventListener accelSensorEventListener, gravitySensorListener, stepCounterListener, stepDetectorListener, significationMotionListener, orientationListener;
    private long lastTimestamp;
    private float[] lastAcceleration, currentVelocity, position;
    private float lastX, lastY, lastZ;
    private static final float NS2S = 1.0f / 1000000000.0f;
    private float currentSpeed_x, currentSpeed_y, currentSpeed_z;
    private float speed;
    private static final int SHAKE_THRESHOLD = 600;
    private static final int MAX_SENSOR_VALUES = 100;
    private static final int MIN_SENSOR_VALUES = 10;
    private PowerManager.WakeLock wakeLock;

    private LocationManager locationManager;
    private LocationListener locationListener = new LocationListener() {
        @Override
        public void onLocationChanged(@NonNull Location location) {
            double latitude = location.getLatitude();
            double longitude = location.getLongitude();
            JSONObject obj = DatetimeManager.getInstance().getSystemDateTime();
            txt_x.setText(String.format("%.5f / %.5f / %f", latitude,longitude, location.getAccuracy()));
            txt_y.setText(location.getSpeed() + "");
        }
    };

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        setContentView(R.layout.activity_function_sensor);
        init();
        super.onCreate(savedInstanceState);

        PowerManager powerManager = (PowerManager) getSystemService(Context.POWER_SERVICE);
        wakeLock = powerManager.newWakeLock(PowerManager.PARTIAL_WAKE_LOCK, "MyApp::MyWakelockTag");
    }

    @RequiresApi(api = Build.VERSION_CODES.N)
    private class GnssStatusCallback extends GnssStatus.Callback {

        @Override
        public void onSatelliteStatusChanged(GnssStatus status) {
            int satelliteCount = status.getSatelliteCount();
            int usedInFixCount = 0;

            for (int i = 0; i < satelliteCount; i++) {
                if (status.usedInFix(i)) {
                    usedInFixCount++;
                }
            }

            txt_z.setText(String.format("총 위성 수: %d\n사용된 위성 수: %d", satelliteCount, usedInFixCount));
        }
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

        btn_6 = findViewById(R.id.btn_6);
        btn_6.setOnClickListener((v) -> btn_6_action());

        btn_7 = findViewById(R.id.btn_7);
        btn_7.setOnClickListener((v) -> btn_7_action());

        btn_8 = findViewById(R.id.btn_8);
        btn_8.setOnClickListener((v) -> btn_8_action());

        btn_9 = findViewById(R.id.btn_9);
        btn_9.setOnClickListener((v) -> btn_9_action());

        btn_10 = findViewById(R.id.btn_10);
        btn_10.setOnClickListener((v) -> btn_9_action());

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
        accelSensor = sensorManager.getDefaultSensor(Sensor.TYPE_LINEAR_ACCELERATION);
        currentSpeed_x = currentSpeed_y = currentSpeed_z = -99f;
        lastTimestamp = -1l;
        speed = 0;

        lastAcceleration = new float[3];
        currentVelocity = new float[3];
        position = new float[3];

        accelSensorEventListener = new SensorEventListener() {
            @Override
            public void onSensorChanged(SensorEvent event) {

                float x = event.values[0];
                float y = event.values[1];
                float z = event.values[2];

                txt_x.setText("x: " + x + "\ny: " + y + "\nz: " + z);
                if (lastTimestamp == -1l) {
                   lastTimestamp = event.timestamp;
                   lastX = x;
                   lastY = y;
                   lastZ = z;
                   return ;
                }

                long timeInterval = event.timestamp - lastTimestamp;
                if(timeInterval < 100) return;

                float deltaX = x - lastX;
                float deltaY = y - lastY;
                float deltaZ = z - lastZ;

                lastX = x;
                lastY = y;
                lastZ = z;

                float speedDelta = Math.abs(deltaX + deltaY + deltaZ) / timeInterval * 10000; // m/s^2를 m/s로 바꾸기 위해
                float acceleration = (float) Math.sqrt(x * x + y * y + z * z);
                speed = speedDelta*0.1f;

                acceleration = acceleration - 0.01f;
                if(acceleration < 0) acceleration = 0f;

//                txt_x.setText(String.format(Locale.getDefault(), "%.5f", acceleration));
                lastTimestamp = event.timestamp;



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
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            locationManager.registerGnssStatusCallback(new GnssStatusCallback(), null);
        }
        locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 0, 0, locationListener);
    }

    private void btn_6_action() {
        gravitySensor = sensorManager.getDefaultSensor(Sensor.TYPE_GRAVITY);

        if (gravitySensor == null) {
            Toast.makeText(this, "이 기기에서는 중력 센서를 사용할 수 없습니다.", Toast.LENGTH_SHORT).show();
        }

        gravitySensorListener = new SensorEventListener() {
            @Override
            public void onSensorChanged(SensorEvent event) {
                float x = event.values[0];
                float y = event.values[1];
                float z = event.values[2];

                txt_z.setText("x: " + x + "\ny: " + y + "\nz: " + z);
            }

            @Override
            public void onAccuracyChanged(Sensor sensor, int i) {

            }
        };

        sensorManager.registerListener(gravitySensorListener, gravitySensor, SensorManager.SENSOR_DELAY_NORMAL);
    }

    private void btn_7_action() {
        stepCounterSensor = sensorManager.getDefaultSensor(Sensor.TYPE_STEP_COUNTER);
        stepCounterListener = new SensorEventListener() {
            @Override
            public void onSensorChanged(SensorEvent sensorEvent) {
                txt_x.setText("걸음 수: " + (int) sensorEvent.values[0]);
                Log.i("this", "걸음 수: " + String.valueOf(sensorEvent.values[0]));
            }

            @Override
            public void onAccuracyChanged(Sensor sensor, int accuracy) {

            }
        };


        sensorManager.registerListener(stepCounterListener, stepCounterSensor, SensorManager.SENSOR_DELAY_NORMAL);
    }

    private void btn_8_action() {
        stepDetectorSensor = sensorManager.getDefaultSensor(Sensor.TYPE_STEP_DETECTOR);
        stepDetectorListener = new SensorEventListener() {
            @Override
            public void onSensorChanged(SensorEvent sensorEvent) {
                Log.i("this", String.valueOf(sensorEvent.values[0]));
            }

            @Override
            public void onAccuracyChanged(Sensor sensor, int accuracy) {

            }
        };

        if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACTIVITY_RECOGNITION)
                != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.ACTIVITY_RECOGNITION}, 1);
        }

        sensorManager.registerListener(stepDetectorListener, stepDetectorSensor, SensorManager.SENSOR_DELAY_NORMAL);
    }

    private void btn_9_action() {
        significationMotionSensor = sensorManager.getDefaultSensor(Sensor.TYPE_SIGNIFICANT_MOTION);
        significationMotionListener = new SensorEventListener() {
            @Override
            public void onSensorChanged(SensorEvent sensorEvent) {
                Log.i("this", "Significant motion detected");
                wakeLock.acquire(10 * 60 * 1000L /* 10 minutes */);

                // 중요한 움직임이 감지되었을 때 수행할 작업을 여기에 추가합니다.
                Toast.makeText(getApplicationContext(), "Significant motion detected!", Toast.LENGTH_SHORT).show();

                // Wake lock을 해제합니다.
                wakeLock.release();
            }

            @Override
            public void onAccuracyChanged(Sensor sensor, int accuracy) {

            }
        };

        if (significationMotionSensor == null) {
            Toast.makeText(this, "Significant motion sensor not supported on this device.", Toast.LENGTH_LONG).show();
        } else {
            Toast.makeText(this, "Significant motion sensor supported on this device.", Toast.LENGTH_LONG).show();
        }

        sensorManager.registerListener(significationMotionListener, significationMotionSensor, SensorManager.SENSOR_DELAY_NORMAL);
    }

    private void btn_10_action() {
        // Rotation matrix based on current readings from accelerometer and magnetometer.
        final float[] rotationMatrix = new float[9];
        SensorManager.getRotationMatrix(rotationMatrix, null, accelerometerReading, magnetometerReading);

// Express the updated rotation matrix as three orientation angles.
        final float[] orientationAngles = new float[3];
        SensorManager.getOrientation(rotationMatrix, orientationAngles);
    }
}
