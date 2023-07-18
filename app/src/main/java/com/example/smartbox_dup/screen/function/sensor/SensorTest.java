package com.example.smartbox_dup.screen.function.sensor;

import android.Manifest;
import android.content.Context;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.graphics.DashPathEffect;
import android.graphics.drawable.Drawable;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.location.GnssClock;
import android.location.GnssMeasurement;
import android.location.GnssMeasurementsEvent;
import android.location.GnssStatus;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Build;
import android.os.Bundle;
import android.os.PowerManager;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
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
import com.example.smartbox_dup.utils.GlobalApplication;
import com.github.mikephil.charting.charts.LineChart;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.LineData;
import com.github.mikephil.charting.data.LineDataSet;
import com.github.mikephil.charting.formatter.IFillFormatter;
import com.github.mikephil.charting.highlight.Highlight;
import com.github.mikephil.charting.interfaces.dataprovider.LineDataProvider;
import com.github.mikephil.charting.interfaces.datasets.ILineDataSet;
import com.github.mikephil.charting.listener.ChartTouchListener;
import com.github.mikephil.charting.listener.OnChartGestureListener;
import com.github.mikephil.charting.listener.OnChartValueSelectedListener;

import org.json.JSONException;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.AbstractMap;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;


public class SensorTest extends AppCompatActivity {
    private static final int RADIAN_TO_DEGREE = -57;
    private SensorManager sensorManager;
    private Sensor sensor;
    private SensorEventListener sensorEventListener;
    private Button btn_1, btn_2, btn_3, btn_4, btn_5, btn_6, btn_7, btn_8, btn_9, btn_10, btn_11, btn_12, btn_13, btn_14, btn_15, btn_16, btn_17, btn_18, btn_19, btn_20;
    private TextView txt_x, txt_y, txt_z;
    private Sensor rotationSensor, accelSensor, gravitySensor, stepCounterSensor, stepDetectorSensor, significationMotionSensor, orientationSensor, accelerometerSensor, magneticFieldSensor, proximitySensor, lightSensor, pressureSensor, humiditySensor, temperatureSensor, gyroscopeSensor, gameRotationSensor, magneticUnCalibratedSensor;
    private SensorEventListener rotationListener, accelSensorEventListener, gravitySensorListener, stepCounterListener, stepDetectorListener, significationMotionListener, orientationListener, accelerometerListener, magneticFieldListener, proximityListener, lightListener, pressureListener, humidityListener, temperatureListener, gyroscopeListener, gameRotationListener, magneticUncalibratedListener;
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
    private final float[] accelerometerReading = new float[3];
    private final float[] magnetometerReading = new float[3];
    private final float[] rotationMatrix = new float[9];
    private final float[] orientationAngles = new float[3];
    private float max_noise, min_noise;
    private ArrayList<Entry> valuesX, valuesY, valuesZ;

    private GnssMeasurementsEvent.Callback mGnssMeasurementsListener;
    private GnssMeasurement mLastMeasurement;
    private GnssClock gnssClock;

    private LineChart xChart;
    private OnChartGestureListener chartGestureListener;

    private Map<Integer, GnssMeasurement> measurementMap;

    private LocationManager locationManager;
    private LocationListener locationListener = new LocationListener() {
        @Override
        public void onLocationChanged(@NonNull Location location) {
            double latitude = location.getLatitude();
            double longitude = location.getLongitude();
            JSONObject obj = DatetimeManager.getInstance().getSystemDateTime();
            Log.i("this", "onLocationChanged: "+String.format("%.5f / %.5f / %f", latitude, longitude, location.getAccuracy()));
            txt_x.setText(String.format("%.5f / %.5f / %f", latitude, longitude, location.getAccuracy()));
            txt_y.setText(location.getSpeed() + "");

            Log.i("this", location.getExtras().toString());
            Bundle bundle = location.getExtras();
            for(String key : bundle.keySet()) {
                Log.i("this", "key: " + key + " / " + bundle.get(key).toString());
            }


        }
    };

    public SensorTest() {
    }

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
                    int svid = status.getSvid(i);
                    int constellationType = status.getConstellationType(i);
                    GnssMeasurement measurement = measurementMap.get(constellationType*100+svid);
                    if(measurement == null) {
                        Log.i("this", "measurement가 들어있지 않은데 onSatelliteStatusChanged 발생: " + (constellationType*100+svid));
                        continue;
                    }
                    long receivedSvTimeNanos = measurement.getReceivedSvTimeNanos(); // GNSS위성에서 내 안드로이드 기기로 신호를 전송할 때의 Time
                    long getTimeNanos = gnssClock.getTimeNanos();
                    long timeDifferenceNanos = getTimeNanos - receivedSvTimeNanos;
                    double timeOffsetNanos = measurement.getTimeOffsetNanos();
                    double speedOfLight = 299792458;
                    double distanceMeters = timeDifferenceNanos * speedOfLight / 1e9;

                    long receivedSvTimeMillis = receivedSvTimeNanos / 1000000L; // 나노초를 밀리초로 변환
                    long getTimeMillis = getTimeNanos / 1000000L;
                    Date date = new Date(receivedSvTimeMillis); // 밀리초 값을 Date 객체로 변환
                    SimpleDateFormat sdf = new SimpleDateFormat("HH:mm:ss.SSS"); // 연월일시 형식 지정
                    String formattedDate = sdf.format(date); // 연월일시 형식으로 변환

                    String codeType = null;
                    if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.Q) {
                        codeType = measurement.getCodeType();
                    }




                    try {
                        Log.i("this", String.format("현재시간 : %s", DatetimeManager.getInstance().getSystemDateTime().get("datetime")) + " / "  + receivedSvTimeMillis + " / " + getTimeMillis + " / " + measurement.getTimeOffsetNanos() + " / " + constellationType + " / " + svid + " / " + codeType);
                    } catch (JSONException e) {
                        throw new RuntimeException(e);
                    }
                }
            }

            txt_z.setText(String.format("총 위성 수: %d\n사용된 위성 수: %d", satelliteCount, usedInFixCount));
            Log.i("this", String.format("총 위성 수: %d  사용된 위성 수: %d", satelliteCount, usedInFixCount));
        }
    }

    private void init() {
        locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
        sensorManager = (SensorManager) getSystemService(Context.SENSOR_SERVICE);
        measurementMap = new HashMap<>();

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
        btn_10.setOnClickListener((v) -> btn_10_action());

        btn_11 = findViewById(R.id.btn_11);
        btn_11.setOnClickListener((v) -> btn_11_action());

        btn_12 = findViewById(R.id.btn_12);
        btn_12.setOnClickListener((v) -> btn_12_action());

        btn_13 = findViewById(R.id.btn_13);
        btn_13.setOnClickListener((v) -> btn_13_action());

        btn_14 = findViewById(R.id.btn_14);
        btn_14.setOnClickListener((v) -> btn_14_action());

        btn_15 = findViewById(R.id.btn_15);
        btn_15.setOnClickListener((v) -> btn_15_action());

        btn_16 = findViewById(R.id.btn_16);
        btn_16.setOnClickListener((v) -> btn_16_action());

        btn_17 = findViewById(R.id.btn_17);
        btn_17.setOnClickListener((v) -> btn_17_action());

        btn_18 = findViewById(R.id.btn_18);
        btn_18.setOnClickListener((v) -> btn_18_action());

        btn_19 = findViewById(R.id.btn_19);
        btn_19.setOnClickListener((v) -> btn_19_action());

        btn_20 = findViewById(R.id.btn_20);
        btn_20.setOnClickListener((v) -> btn_20_action());

        initChart();

        txt_x = findViewById(R.id.txt_x);
        txt_y = findViewById(R.id.txt_y);
        txt_z = findViewById(R.id.txt_z);
    }

    private void setData(LineChart lineChart) {
        LineDataSet set1, set2, set3;

        xChart.setVisibility(View.VISIBLE);

        // create a dataset and give it a type
        set1 = new LineDataSet(valuesX, "x");
        set2 = new LineDataSet(valuesY, "y");
        set3 = new LineDataSet(valuesZ, "z");

        set1.setDrawIcons(false);
        set2.setDrawIcons(false);
        set3.setDrawIcons(false);

        // draw dashed line
        set1.enableDashedLine(10f, 5f, 0f);
        set2.enableDashedLine(10f, 5f, 0f);
        set3.enableDashedLine(10f, 5f, 0f);

        // black lines and points
        set1.setColor(getColor(R.color.black));
        set1.setCircleColor(getColor(R.color.black));
        set2.setColor(getColor(R.color.blue));
        set2.setCircleColor(getColor(R.color.blue));
        set3.setColor(getColor(R.color.red));
        set3.setCircleColor(getColor(R.color.red));

        // line thickness and point size
        set1.setLineWidth(1f);
        set1.setCircleRadius(3f);
        set2.setLineWidth(1f);
        set2.setCircleRadius(3f);
        set3.setLineWidth(1f);
        set3.setCircleRadius(3f);

        // draw points as solid circles
        set1.setDrawCircleHole(false);
        set2.setDrawCircleHole(false);
        set3.setDrawCircleHole(false);

        // customize legend entry
        set1.setFormLineWidth(1f);
        set1.setFormLineDashEffect(new DashPathEffect(new float[]{10f, 5f}, 0f));
        set1.setFormSize(15.f);
        set2.setFormLineWidth(1f);
        set2.setFormLineDashEffect(new DashPathEffect(new float[]{10f, 5f}, 0f));
        set2.setFormSize(15.f);
        set3.setFormLineWidth(1f);
        set3.setFormLineDashEffect(new DashPathEffect(new float[]{10f, 5f}, 0f));
        set3.setFormSize(15.f);

        // text size of values
        set1.setValueTextSize(9f);
        set2.setValueTextSize(9f);
        set3.setValueTextSize(9f);


        // draw selection line as dashed
        set1.enableDashedHighlightLine(10f, 5f, 0f);
        set2.enableDashedHighlightLine(10f, 5f, 0f);
        set3.enableDashedHighlightLine(10f, 5f, 0f);

        // set the filled area
        set1.setDrawFilled(false);
        set1.setFillFormatter(new IFillFormatter() {
            @Override
            public float getFillLinePosition(ILineDataSet dataSet, LineDataProvider dataProvider) {
                return lineChart.getAxisLeft().getAxisMinimum();
            }
        });

        set2.setDrawFilled(false);
        set2.setFillFormatter(new IFillFormatter() {
            @Override
            public float getFillLinePosition(ILineDataSet dataSet, LineDataProvider dataProvider) {
                return lineChart.getAxisLeft().getAxisMinimum();
            }
        });

        set3.setDrawFilled(false);
        set3.setFillFormatter(new IFillFormatter() {
            @Override
            public float getFillLinePosition(ILineDataSet dataSet, LineDataProvider dataProvider) {
                return lineChart.getAxisLeft().getAxisMinimum();
            }
        });

        ArrayList<ILineDataSet> dataSets = new ArrayList<>();
        dataSets.add(set1); // add the data sets
        dataSets.add(set2);
        dataSets.add(set3);

        // create a data object with the data sets
        LineData data = new LineData(dataSets);

        // set data
        lineChart.setData(data);

        // redraw
        lineChart.invalidate();
    }


    private void btn_1_action() {
        rotationSensor = sensorManager.getDefaultSensor(Sensor.TYPE_ROTATION_VECTOR);

        if (rotationSensor == null) {
            Toast.makeText(this, "이 기기에서는 회전 벡터 센서를 사용할 수 없습니다.", Toast.LENGTH_SHORT).show();
        }

        rotationListener = new SensorEventListener() {
            @Override
            public void onSensorChanged(SensorEvent event) {
                float x = event.values[0];
                float y = event.values[1];
                float z = event.values[2];

//                txt_z.setText("x: " + x + "\ny: " + y + "\nz: " + z);
                valuesX.add(new Entry(valuesX.size(), x));
                valuesY.add(new Entry(valuesY.size(), y));
                valuesZ.add(new Entry(valuesZ.size(), z));
                setData(xChart);
            }

            @Override
            public void onAccuracyChanged(Sensor sensor, int i) {

            }
        };

        sensorManager.registerListener(rotationListener, rotationSensor, SensorManager.SENSOR_DELAY_NORMAL);
    }

    private void btn_2_action() {
    }

    private void btn_3_action() {
        // 가속도 센서테스트
        accelSensor = sensorManager.getDefaultSensor(Sensor.TYPE_LINEAR_ACCELERATION);
        currentSpeed_x = currentSpeed_y = currentSpeed_z = -99f;
        lastTimestamp = -1l;
        speed = 0;

        max_noise = 0;
        min_noise = 0;


        Calendar cTuneTime = Calendar.getInstance();
        cTuneTime.set(Calendar.SECOND, cTuneTime.get(Calendar.SECOND) + 5);


        lastAcceleration = new float[3];
        currentVelocity = new float[3];
        position = new float[3];

        accelSensorEventListener = new SensorEventListener() {
            @Override
            public void onSensorChanged(SensorEvent event) {

                float x = event.values[0];
                float y = event.values[1];
                float z = event.values[2];

//                if (Calendar.getInstance().getTimeInMillis() < cTuneTime.getTimeInMillis()) {
//                    if (max_noise < x) max_noise = x;
//                    if (max_noise < y) max_noise = y;
//                    if (max_noise < z) max_noise = z;
//                    if (min_noise > x) min_noise = x;
//                    if (min_noise > y) min_noise = y;
//                    if (min_noise > z) min_noise = z;
//                    return ;
//                }
//
//                if(max_noise*2 > x && x > 0) x = 0;
//                if(max_noise*2 > y && y > 0) y = 0;
//                if(max_noise*2 > z && z > 0) z = 0;
//                if(min_noise*2 > x && x < 0) x = 0;
//                if(min_noise*2 > y && y < 0) y = 0;
//                if(min_noise*2 > z && z < 0) z = 0;

                valuesX.add(new Entry(valuesX.size(), x));
                valuesY.add(new Entry(valuesY.size(), y));
                valuesZ.add(new Entry(valuesZ.size(), z));
                setData(xChart);


//                txt_x.setText("x: " + x + "\ny: " + y + "\nz: " + z);


                if (lastTimestamp == -1l) {
                    lastTimestamp = event.timestamp;
                    lastX = x;
                    lastY = y;
                    lastZ = z;
                    return;
                }


            }

            @Override
            public void onAccuracyChanged(Sensor sensor, int accuracy) {
                Log.i("this", "onAccuracyChanged: " + accuracy);
            }
        };

        sensorManager.registerListener(accelSensorEventListener, accelSensor, SensorManager.SENSOR_DELAY_NORMAL);
    }

    public float kalmanFilter(float measurement) {
        float Q = 0.0001f; // process noise covariance
        float R = 0.1f; // measurement noise covariance
        float x = 0f; // estimated value
        float P = 1f; // estimation error covariance

        // prediction
        float x_pred = x;
        float P_pred = P + Q;

        // update
        float K = P_pred / (P_pred + R);
        x = x_pred + K * (measurement - x_pred);
        P = (1 - K) * P_pred;

        return x;
    }

    private void btn_4_action() {
        // 자이로 센서테스트
        gyroscopeSensor = sensorManager.getDefaultSensor(Sensor.TYPE_GYROSCOPE);
        if (gyroscopeSensor == null) {
            Toast.makeText(this, "이 기기에서는 자이로스코프 센서를 사용할 수 없습니다.", Toast.LENGTH_SHORT).show();
        }
        gyroscopeListener = new SensorEventListener() {
            @Override
            public void onSensorChanged(SensorEvent event) {
                float x = event.values[0];
                float y = event.values[1];
                float z = event.values[2];

                txt_x.setText("x: " + x + "\ny: " + y + "\nz: " + z);
            }

            @Override
            public void onAccuracyChanged(Sensor sensor, int accuracy) {

            }
        };
        sensorManager.registerListener(gyroscopeListener, gyroscopeSensor, SensorManager.SENSOR_DELAY_NORMAL);
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

                valuesX.add(new Entry(valuesX.size(), x));
                valuesY.add(new Entry(valuesY.size(), y));
                valuesZ.add(new Entry(valuesZ.size(), z));
                setData(xChart);

//                txt_z.setText("x: " + x + "\ny: " + y + "\nz: " + z);
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
        accelerometerSensor = sensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);
        if (accelerometerSensor != null) {

            accelerometerListener = new SensorEventListener() {
                @Override
                public void onSensorChanged(SensorEvent event) {
                    System.arraycopy(event.values, 0, accelerometerReading,
                            0, accelerometerReading.length);

                    updateOrientationAngles();
                }

                @Override
                public void onAccuracyChanged(Sensor sensor, int i) {

                }
            };

            sensorManager.registerListener(accelerometerListener, accelerometerSensor, SensorManager.SENSOR_DELAY_GAME);
        }
        magneticFieldSensor = sensorManager.getDefaultSensor(Sensor.TYPE_MAGNETIC_FIELD);
        if (magneticFieldSensor != null) {
            magneticFieldListener = new SensorEventListener() {
                @Override
                public void onSensorChanged(SensorEvent event) {
                    System.arraycopy(event.values, 0, magnetometerReading,
                            0, magnetometerReading.length);

                    updateOrientationAngles();
                }

                @Override
                public void onAccuracyChanged(Sensor sensor, int i) {

                }
            };
            sensorManager.registerListener(magneticFieldListener, magneticFieldSensor, SensorManager.SENSOR_DELAY_GAME);
        }

    }

    public void updateOrientationAngles() {
        // Update rotation matrix, which is needed to update orientation angles.
        SensorManager.getRotationMatrix(rotationMatrix, null,
                accelerometerReading, magnetometerReading);

        // "mRotationMatrix" now has up-to-date information.

        SensorManager.getOrientation(rotationMatrix, orientationAngles);

        StringBuilder sb = new StringBuilder();
        for(float val : rotationMatrix) {
            sb.append(val);
            sb.append(" ");
        }
        Log.i("this", "rotationMatrix: " + sb);

        float azimuthInRadians = orientationAngles[0];
        float azimuthInDegrees = (float) (Math.toDegrees(azimuthInRadians) + 360) % 360;

        float pitchInRadians = orientationAngles[1];
        float pitchInDegrees = (float) (Math.toDegrees(pitchInRadians) + 360) % 360;

        float rollInRadians = orientationAngles[2];
        float rollInDegrees = (float) (Math.toDegrees(rollInRadians) + 360) % 360;

        valuesX.add(new Entry(valuesX.size(), azimuthInDegrees));
        valuesY.add(new Entry(valuesY.size(), pitchInDegrees));
        valuesZ.add(new Entry(valuesZ.size(), rollInDegrees));
        setData(xChart);
        // "mOrientationAngles" now has up-to-date information.
    }

    public void updateInclinationAngles() {
        // Update rotation matrix, which is needed to update orientation angles.
        SensorManager.getRotationMatrix(rotationMatrix, null,
                accelerometerReading, magnetometerReading);

        // "mRotationMatrix" now has up-to-date information.

        SensorManager.getInclination(rotationMatrix);

        float azimuthInRadians = rotationMatrix[0];
        float azimuthInDegrees = (float) (Math.toDegrees(azimuthInRadians));

        float pitchInRadians = rotationMatrix[1];
        float pitchInDegrees = (float) (Math.toDegrees(pitchInRadians));

        float rollInRadians = rotationMatrix[2];
        float rollInDegrees = (float) (Math.toDegrees(rollInRadians));

        valuesX.add(new Entry(valuesX.size(), azimuthInDegrees));
        valuesY.add(new Entry(valuesY.size(), pitchInDegrees));
        valuesZ.add(new Entry(valuesZ.size(), rollInDegrees));
        setData(xChart);
        // "mOrientationAngles" now has up-to-date information.
    }

    private void btn_11_action() {
        proximitySensor = sensorManager.getDefaultSensor(Sensor.TYPE_PROXIMITY);
        proximityListener = new SensorEventListener() {
            @Override
            public void onSensorChanged(SensorEvent sensorEvent) {
                Log.i("this", String.valueOf(sensorEvent.values[0]));
                txt_x.setText(sensorEvent.values[0] + "");
                valuesX.add(new Entry(valuesX.size(), sensorEvent.values[0]));
                setData(xChart);
            }

            @Override
            public void onAccuracyChanged(Sensor sensor, int accuracy) {

            }
        };

        if (proximitySensor == null) {
            Toast.makeText(this, "proximity sensor not supported on this device.", Toast.LENGTH_LONG).show();
        } else {
            Toast.makeText(this, "proximity sensor supported on this device.", Toast.LENGTH_LONG).show();
        }

        sensorManager.registerListener(proximityListener, proximitySensor, SensorManager.SENSOR_DELAY_NORMAL);
    }

    private void btn_12_action() {
        lightSensor = sensorManager.getDefaultSensor(Sensor.TYPE_LIGHT);
        lightListener = new SensorEventListener() {
            @Override
            public void onSensorChanged(SensorEvent sensorEvent) {
                float lightLevel = sensorEvent.values[0];
                txt_x.setText("조도: " + lightLevel);
            }

            @Override
            public void onAccuracyChanged(Sensor sensor, int accuracy) {

            }
        };

        if (lightSensor == null) {
            Toast.makeText(this, "lightSensor sensor not supported on this device.", Toast.LENGTH_LONG).show();
        } else {
            Toast.makeText(this, "lightSensor sensor supported on this device.", Toast.LENGTH_LONG).show();
        }

        sensorManager.registerListener(lightListener, lightSensor, SensorManager.SENSOR_DELAY_NORMAL);
    }

    private void btn_13_action() {
        pressureSensor = sensorManager.getDefaultSensor(Sensor.TYPE_PRESSURE);
        pressureListener = new SensorEventListener() {
            @Override
            public void onSensorChanged(SensorEvent sensorEvent) {
                float pressureLevel = sensorEvent.values[0];
                txt_x.setText("압력: " + pressureLevel);
            }

            @Override
            public void onAccuracyChanged(Sensor sensor, int accuracy) {

            }
        };

        if (pressureSensor == null) {
            Toast.makeText(this, "pressure sensor not supported on this device.", Toast.LENGTH_LONG).show();
        } else {
            Toast.makeText(this, "pressure sensor supported on this device.", Toast.LENGTH_LONG).show();
        }

        sensorManager.registerListener(pressureListener, pressureSensor, SensorManager.SENSOR_DELAY_NORMAL);
    }

    private void btn_14_action() {
        humiditySensor = sensorManager.getDefaultSensor(Sensor.TYPE_RELATIVE_HUMIDITY);
        humidityListener = new SensorEventListener() {
            @Override
            public void onSensorChanged(SensorEvent sensorEvent) {
                float humidityLevel = sensorEvent.values[0];
                txt_x.setText("습도: " + humidityLevel);
            }

            @Override
            public void onAccuracyChanged(Sensor sensor, int accuracy) {

            }
        };

        if (humiditySensor == null) {
            Toast.makeText(this, "humidity sensor not supported on this device.", Toast.LENGTH_LONG).show();
        } else {
            Toast.makeText(this, "humidity sensor supported on this device.", Toast.LENGTH_LONG).show();
        }

        sensorManager.registerListener(humidityListener, humiditySensor, SensorManager.SENSOR_DELAY_NORMAL);
    }

    private void btn_15_action() {
        temperatureSensor = sensorManager.getDefaultSensor(Sensor.TYPE_AMBIENT_TEMPERATURE);
        temperatureListener = new SensorEventListener() {
            @Override
            public void onSensorChanged(SensorEvent sensorEvent) {
                float humidityLevel = sensorEvent.values[0];
                txt_x.setText("습도: " + humidityLevel);
            }

            @Override
            public void onAccuracyChanged(Sensor sensor, int accuracy) {

            }
        };

        if (temperatureSensor == null) {
            Toast.makeText(this, "temperature sensor not supported on this device.", Toast.LENGTH_LONG).show();
        } else {
            Toast.makeText(this, "temperature sensor supported on this device.", Toast.LENGTH_LONG).show();
        }

        sensorManager.registerListener(temperatureListener, temperatureSensor, SensorManager.SENSOR_DELAY_NORMAL);
    }

    private void btn_16_action() {
        gameRotationSensor = sensorManager.getDefaultSensor(Sensor.TYPE_GAME_ROTATION_VECTOR);
        gameRotationListener = new SensorEventListener() {
            @Override
            public void onSensorChanged(SensorEvent sensorEvent) {
                float x = sensorEvent.values[0];
                float y = sensorEvent.values[1];
                float z = sensorEvent.values[2];

                valuesX.add(new Entry(valuesX.size(), x));
                valuesY.add(new Entry(valuesY.size(), y));
                valuesZ.add(new Entry(valuesZ.size(), z));
                setData(xChart);
            }

            @Override
            public void onAccuracyChanged(Sensor sensor, int accuracy) {

            }
        };

        if (gameRotationSensor == null) {
            Toast.makeText(this, "gameRotationSensor not supported on this device.", Toast.LENGTH_LONG).show();
        } else {
            Toast.makeText(this, "gameRotationSensor supported on this device.", Toast.LENGTH_LONG).show();
        }

        sensorManager.registerListener(gameRotationListener, gameRotationSensor, SensorManager.SENSOR_DELAY_NORMAL);
    }

    private void btn_17_action() {

    }

    private void btn_18_action() {
        magneticFieldSensor = sensorManager.getDefaultSensor(Sensor.TYPE_MAGNETIC_FIELD);

        magneticFieldListener = new SensorEventListener() {
            @Override
            public void onSensorChanged(SensorEvent sensorEvent) {
                float x = sensorEvent.values[0];
                float y = sensorEvent.values[1];
                float z = sensorEvent.values[2];

                valuesX.add(new Entry(valuesX.size(), x));
                valuesY.add(new Entry(valuesY.size(), y));
                valuesZ.add(new Entry(valuesZ.size(), z));
                setData(xChart);
            }

            @Override
            public void onAccuracyChanged(Sensor sensor, int accuracy) {

            }
        };

        if (magneticFieldSensor == null) {
            Toast.makeText(this, "magneticFieldSensor not supported on this device.", Toast.LENGTH_LONG).show();
        } else {
            Toast.makeText(this, "magneticFieldSensor supported on this device.", Toast.LENGTH_LONG).show();
        }

        sensorManager.registerListener(magneticFieldListener, magneticFieldSensor, SensorManager.SENSOR_DELAY_NORMAL);
    }

    private void btn_19_action() {
        magneticUnCalibratedSensor = sensorManager.getDefaultSensor(Sensor.TYPE_MAGNETIC_FIELD_UNCALIBRATED);

        magneticUncalibratedListener = new SensorEventListener() {
            @Override
            public void onSensorChanged(SensorEvent sensorEvent) {
                float x = sensorEvent.values[0];
                float y = sensorEvent.values[1];
                float z = sensorEvent.values[2];

                valuesX.add(new Entry(valuesX.size(), x));
                valuesY.add(new Entry(valuesY.size(), y));
                valuesZ.add(new Entry(valuesZ.size(), z));
                setData(xChart);
            }

            @Override
            public void onAccuracyChanged(Sensor sensor, int accuracy) {

            }
        };

        if (magneticUnCalibratedSensor == null) {
            Toast.makeText(this, "magneticUnCalibratedSensor not supported on this device.", Toast.LENGTH_LONG).show();
        } else {
            Toast.makeText(this, "magneticUnCalibratedSensor supported on this device.", Toast.LENGTH_LONG).show();
        }

        sensorManager.registerListener(magneticUncalibratedListener, magneticUnCalibratedSensor, SensorManager.SENSOR_DELAY_NORMAL);
    }

    public void btn_20_action() {
        GnssMeasurementsEvent.Callback mGnssMeasurementsListener = null;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            mGnssMeasurementsListener = new GnssMeasurementsEvent.Callback() {
                @Override
                public void onGnssMeasurementsReceived(GnssMeasurementsEvent eventArgs) {
                    // 가장 최근에 수신한 원시 데이터 가져오기
                    List<GnssMeasurement> measurements = new ArrayList<>(eventArgs.getMeasurements());
                    gnssClock = eventArgs.getClock();

                    for (GnssMeasurement gnssMeasurement : measurements) {
                        try {

                        } catch (Exception e) {
                            Log.e("this", e.toString());
                        }

                        long receivedSvTimeNanos = gnssMeasurement.getReceivedSvTimeNanos(); // GNSS위성에서 내 안드로이드 기기로 신호를 전송할 때의 Time (위성시간)

                        long millis = receivedSvTimeNanos / 1000000L; // 나노초를 밀리초로 변환
                        Date date = new Date(millis); // 밀리초 값을 Date 객체로 변환
                        SimpleDateFormat sdf = new SimpleDateFormat("HH:mm:ss.SSS"); // 연월일시 형식 지정
                        String formattedDate = sdf.format(date); // 연월일시 형식으로 변환

                        int constellationType = gnssMeasurement.getConstellationType(); //
                        int svid = gnssMeasurement.getSvid();
                        String codeType = null;
                        gnssMeasurement.getAccumulatedDeltaRangeMeters();
                        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.Q) {
                            codeType = gnssMeasurement.getCodeType();
                        }
                        measurementMap.put(constellationType*100+svid, gnssMeasurement);
//                        Log.i("this", "onGnssMeasurementsReceived: " + formattedDate + " / " + constellationType + " / " + svid + " / " + codeType);

                    }


                    // 원시 데이터에서 필요한 정보 가져오기
//                    long timestamp = mLastMeasurement.getReceivedSatelliteTimeNanos();

//                    double signalToNoiseRatioInDb = mLastMeasurement.getCn0DbHz();
                    // ... 필요한 정보를 가져온 후 처리하는 코드 작성 ...
                }

                @Override
                public void onStatusChanged(int status) {
                    // 상태가 변경되면 호출됩니다.
                }
            };
        }

        // GNSS 원시 데이터 이벤트 등록
        LocationManager locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.
            Log.e("this", "checkSelfPermission, return;");
            return;
        }
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            locationManager.registerGnssMeasurementsCallback(mGnssMeasurementsListener);
            locationManager.registerGnssStatusCallback(new GnssStatusCallback(), null);
        }
    }

    public void initChart() {
        valuesX = new ArrayList<>();
        valuesY = new ArrayList<>();
        valuesZ = new ArrayList<>();

        xChart = findViewById(R.id.lineChart_x);
        xChart.setVisibility(View.GONE);
        // background color
        xChart.setBackgroundColor(Color.WHITE);

        // disable description text
        xChart.getDescription().setEnabled(false);

        // enable touch gestures
        xChart.setTouchEnabled(true);

        // set listeners
        xChart.setOnChartValueSelectedListener(new OnChartValueSelectedListener() {
            @Override
            public void onValueSelected(Entry e, Highlight h) {
            }

            @Override
            public void onNothingSelected() {
            }
        });
        xChart.setDrawGridBackground(false);
    }

    /**
     * It returns long array, [0]=hours, [1]=minutes, [2]=seconds
     * @param nanoseconds
     * @return
     */
    public long[] getHMSFromNanoseconds(long nanoseconds) {
        // 나노초를 초로 변환
        long totalSeconds = nanoseconds / 1_000_000_000;

        // 시간, 분, 초 계산
        long hours = totalSeconds / 3600;
        long minutes = (totalSeconds % 3600) / 60;
        long seconds = totalSeconds % 60;


        long[] result = new long[3];

        result[0] = hours;
        result[1] = minutes;
        result[2] = seconds;

        return result;
    }
}
