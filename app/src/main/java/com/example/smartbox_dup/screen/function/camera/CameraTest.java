package com.example.smartbox_dup.screen.function.camera;

import android.Manifest;
import android.content.pm.PackageManager;
import android.hardware.Camera;
import android.hardware.camera2.CameraAccessException;
import android.hardware.camera2.CameraDevice;
import android.hardware.camera2.CameraManager;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.widget.Button;
import android.widget.FrameLayout;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import com.example.smartbox_dup.R;
import com.example.smartbox_dup.utils.PermissionManager;

import chanyb.android.java.GlobalApplcation;
import chanyb.android.java.ToastManager;

public class CameraTest extends AppCompatActivity {
    Button btn_1;
    Camera mCamera;
    CameraPreview mPreview;
    FrameLayout preview;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_function_camera);
        init();
    }

    private void init() {
        preview = findViewById(R.id.preview1);
        btn_1 = findViewById(R.id.btn_1);
        btn_1.setOnClickListener((view) -> {
            mCamera = getCameraInstance();
            mPreview = new CameraPreview(this, mCamera);

            preview.addView(mPreview);
        });


    }

    /** Check if this device has a camera */
    private boolean checkCameraHardware() {
        // this device has a camera
        // no camera on this device
        return GlobalApplcation.getContext().getPackageManager().hasSystemFeature(PackageManager.FEATURE_CAMERA_ANY);
    }

    private Camera getCameraInstance() {
        Camera c = null;
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED) {
            Log.i("this", "permission denied");
            PermissionManager.getInstance().cameraPermissionCheck();
            return null;
        }

        try {
            c = Camera.open(); // attempt to get a Camera instance
        }
        catch (Exception e){
            // Camera is not available (in use or does not exist)
            Log.i("this", "error", e);
        }

        return c;
    }
}
/** QR코드 인식 동작 순서
 * 카메라를 연 후 프리뷰를 가동한다.
 * 카메라로부터 지속적으로 영상을 받아들인다. [2]
 * 영상에서 밝기값만 추출[3]하여 이를 기반으로 이진화를 수행한다.[4]
 * Detector 클래스를 통해 QR코드 영역을 찾아냈다.
 * 찾아낸 영역을 Decoder 클래스를 통해 해석한다.
 * 결과 값과 결과 영상을 리턴
 * 결과 값을 분석하여 URL일 경우 탭하면 인터넷으로 연결되도록 한다.
 * 화면에 결과 영상과 결과 값을 출력한다.
 */
