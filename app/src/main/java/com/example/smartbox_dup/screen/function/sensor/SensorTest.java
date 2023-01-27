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
import android.net.Uri;
import android.os.Bundle;
import android.provider.CalendarContract;
import android.util.Log;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import com.example.smartbox_dup.R;

import java.util.Calendar;

import chanyb.android.java.GlobalApplcation;

public class SensorTest extends AppCompatActivity {
    private static final int RADIAN_TO_DEGREE = -57;
    private SensorManager sensorManager;
    private Sensor sensor;
    private SensorEventListener sensorEventListener;
    private Button btn_1, btn_2;
    private TextView txt_x, txt_y, txt_z;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        setContentView(R.layout.activity_function_sensor);
        init();
        super.onCreate(savedInstanceState);
    }

    private void init() {
        btn_1 = findViewById(R.id.btn_1);
        btn_1.setOnClickListener((view) -> {
            btn_1_action();
        });

        btn_2 = findViewById(R.id.btn_2);
        btn_2.setOnClickListener((view) -> {
            btn_2_action();
        });

        txt_x = findViewById(R.id.txt_x);
        txt_y = findViewById(R.id.txt_y);
        txt_z = findViewById(R.id.txt_z);
    }



    private void btn_1_action() {
        sensorManager = (SensorManager) getSystemService(Context.SENSOR_SERVICE);
        sensor = sensorManager.getDefaultSensor(Sensor.TYPE_ROTATION_VECTOR);
        if(sensor == null) {
            Log.i("this", "센서가 존재하지 않습니다.");
        }

        sensorEventListener = new SensorEventListener() {
            @Override
            public void onSensorChanged(SensorEvent event) {
//                float xSin = event.values[0];
//                float ySin = event.values[1];
//                float zSin = event.values[2];
//                float cos = event.values[3];
//                float headingAcc = event.values[4];
//
//                double theta = cos*Math.acos(cos);
//                double sinTheta = Math.sin(theta/2);
//                double x = xSin/sinTheta;
//                double y = ySin/sinTheta;
//                double z = zSin/sinTheta;

                checkOrientation(event.values);

            }

            @Override
            public void onAccuracyChanged(Sensor sensor, int accuracy) {

            }
        };

        sensorManager.registerListener(sensorEventListener, sensor, 10000);

        Log.i("this", "end");
    }

    private void btn_2_action() {
        sensorManager.unregisterListener(sensorEventListener);
    }

    /**
     * 지정한 센서 각도를 판단하여 적절한 회전 모드를 결정한다.
     *
     * @param angle 센서 각도값 또는 360도 기준의 각도값
     * @return 회전 모드
     *                 정방향 세로모드일 경우 "portrait"
     *                 역방향 세로모드일 경우 "portrait_reverse"
     *                 정방향 가로모드일 경우 "landscape"
     *                 역방향 가로모드일 경우 "landscape_reverse"
     */
    private String determineOrientationModeByAngle(float angle) {
        float absAngle = calcAbsoluteAngle(angle);

        // PORTRAIT
        if ((0 <= absAngle && absAngle < 45) || (315 <= absAngle && absAngle < 360)) {
//            this.setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
            return "portrait";
        }
        // LANDSCAPE
        else if (45 <= absAngle && absAngle < 135) {
//            this.setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);
            return "landscape";
        }
        // PORTRAIT (REVERSE)
        else if (135 <= absAngle && absAngle < 225) {
            return "portrait_reverse";
        }
        // LANDSCAPE (REVERSE)
        else { // 225 <= absAngle && absAngle < 315
            return "landscape_reverse";
        }
    }

    /**
     * 지정한 각도를 0 ~ 360도 기준으로 환산하여 계산한다.
     *
     * 각도값이 0을 포함하여 양수일 경우 동일한 각도가 반환되고,
     * 각도값이 음수일 경우 360도를 기준으로 해당 각도값의 절대값을 뺀 각도가 반환된다.
     *
     * @param angle 센서에 의한 원본 각도값
     * @return 0 ~ 360도 기준의 각도값
     */
    private float calcAbsoluteAngle(float angle) {
        if (angle >= 0) {
            return angle;
        }
        else {
            return (360 - Math.abs(angle));
        }
    }


    private void checkOrientation(float[] vectors) {
        float[] rotationMatrix = new float[9];
        SensorManager.getRotationMatrixFromVector(rotationMatrix, vectors);

        int axisX = SensorManager.AXIS_X;
        int axisZ = SensorManager.AXIS_Z;

        float[] adjustedRotationMatrix = new float[9];
        SensorManager.remapCoordinateSystem(rotationMatrix, axisX, axisZ, adjustedRotationMatrix);

        float[] orientation = new float[3];
        SensorManager.getOrientation(adjustedRotationMatrix, orientation);

        float azimuch = orientation[0] * RADIAN_TO_DEGREE;
        float pitch = orientation[1] * RADIAN_TO_DEGREE;
        float roll = orientation[2] * RADIAN_TO_DEGREE;

        txt_y.setText("roll: " + roll);
        txt_x.setText("pitch: " + pitch);
        txt_z.setText("calculateOrientation: " + calculateOrientation(roll, pitch));
    }

    private String calculateOrientation(float roll, float pitch) {
        if (-120 < roll && roll < -60) {
            return "LANDSCAPE REVERSE";
        } else if (60 < roll && roll < 120) {
            return "LANDSCAPE";
        } else if ((-30 < roll && roll < 30)) {
            return "PORTRAIT";
        } else if ((150 < roll && roll < 180) || (-180 < roll && roll < -150)) {
            return "PORTRAIT REVERSE";
        } else {
            return "UNKNOWN";
        }
    }
}
