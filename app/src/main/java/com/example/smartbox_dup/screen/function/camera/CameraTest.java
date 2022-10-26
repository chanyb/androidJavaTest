package com.example.smartbox_dup.screen.function.camera;

import android.content.ContentValues;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.Matrix;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.util.Log;
import android.view.Surface;
import android.widget.Button;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.camera.core.Camera;
import androidx.camera.core.CameraSelector;
import androidx.camera.core.ImageCapture;
import androidx.camera.core.ImageCaptureException;
import androidx.camera.core.Preview;
import androidx.camera.lifecycle.ProcessCameraProvider;
import androidx.camera.view.PreviewView;
import androidx.core.content.ContextCompat;
import androidx.lifecycle.ProcessLifecycleOwner;

import com.bumptech.glide.Glide;
import com.example.smartbox_dup.R;
import com.example.smartbox_dup.utils.DatetimeManager;
import com.example.smartbox_dup.utils.FutureTaskRunner;
import com.example.smartbox_dup.utils.PermissionManager;
import com.google.common.util.concurrent.ListenableFuture;

import org.json.JSONException;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

import chanyb.android.java.GlobalApplcation;

public class CameraTest extends AppCompatActivity {
    private ListenableFuture<ProcessCameraProvider> cameraProviderFuture;
    PreviewView previewView;
    ImageCapture imageCapture;
    Button btn_capture, btn_update;
    ImageView imageView, imageView2;
//    ConstraintLayout lo_preview;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_function_camera);
        if(!PermissionManager.getInstance().cameraPermissionCheck()) {
            FutureTaskRunner<Boolean> futureTaskRunner = new FutureTaskRunner<>();
//            futureTaskRunner.nextTask(() -> {
//                if(!PermissionManager.getInstance().requestWriteExternalStoragePermission()) {
//                    // 이미 거절하여 요청 실패
//                }
//                while (true) {
//                    if(PermissionManager.getInstance().writeExternalStoragePermissionCheck()) {
//                        Log.i("this", "permission granted: " + PermissionManager.getInstance().writeExternalStoragePermissionCheck());
//                        break;
//                    }
//                }
//                return true;
//            });
            futureTaskRunner.nextTask(() -> {
                PermissionManager.getInstance().requestCameraPermission();
                while (true) {
                    if(PermissionManager.getInstance().cameraPermissionCheck()) return true;
                }
            });
            futureTaskRunner.setCallback((res) -> {
                runOnUiThread(() -> init());
                return true;
            });
            futureTaskRunner.start();
        } else {
            init();
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
    }

    private void init() {
        imageView = findViewById(R.id.imageView);
        imageView2 = findViewById(R.id.imageView2);
        imageCapture = new ImageCapture.Builder()
                .setTargetRotation(Surface.ROTATION_0)
                .build();

        btn_update = findViewById(R.id.btn_update);
        btn_update.setOnClickListener(view -> {
            updateImage();
        });


        previewView = findViewById(R.id.previewView);
        cameraProviderFuture = ProcessCameraProvider.getInstance(GlobalApplcation.getContext());
        cameraProviderFuture.addListener(() -> {
            try {
                ProcessCameraProvider cameraProvider = cameraProviderFuture.get();
                bindPreview(cameraProvider);
            } catch (ExecutionException | InterruptedException e) {
                // No errors need to be handled for this Future.
                // This should never be reached.
            }
        }, ContextCompat.getMainExecutor(GlobalApplcation.getContext()));

        btn_capture = findViewById(R.id.btn_capture);
        btn_capture.setOnClickListener((view) -> {
            capture();
        });
    }

    private void bindPreview(@NonNull ProcessCameraProvider cameraProvider) {
        Preview preview = new Preview.Builder().build();

        CameraSelector cameraSelector = new CameraSelector.Builder()
                .requireLensFacing(CameraSelector.LENS_FACING_BACK)
                .build();

        preview.setSurfaceProvider(previewView.getSurfaceProvider());

        try{
            cameraProvider.unbindAll();
            Camera camera = cameraProvider.bindToLifecycle(ProcessLifecycleOwner.get(), cameraSelector, preview, imageCapture);
        } catch (Exception e) {
            Log.i("this", "error", e);
        }
    }

    private void capture() {
        String filename = null;
        try {
            filename = DatetimeManager.getInstance().getSystemDateTime().getString("datetime");
        } catch (JSONException e) {
            e.printStackTrace();
        }

        ContentValues contentValues = new ContentValues();
        contentValues.put(MediaStore.MediaColumns.DISPLAY_NAME, filename);
        contentValues.put(MediaStore.MediaColumns.MIME_TYPE, "image/jpeg");
        if(Build.VERSION.SDK_INT > Build.VERSION_CODES.P) {
            contentValues.put(MediaStore.Images.Media.RELATIVE_PATH, "Pictures/CameraX-Image");
        }

        imageCapture.setFlashMode(ImageCapture.FLASH_MODE_AUTO);

        ImageCapture.OutputFileOptions outputFileOptions =
                new ImageCapture.OutputFileOptions.Builder(GlobalApplcation.getContext().getContentResolver(), MediaStore.Images.Media.EXTERNAL_CONTENT_URI, contentValues).build();
        imageCapture.takePicture(outputFileOptions,
                Executors.newSingleThreadExecutor(),
                new ImageCapture.OnImageSavedCallback() {
                    @Override
                    public void onImageSaved(@NonNull ImageCapture.OutputFileResults outputFileResults) {
                        Log.i("this", "onImageSaved");
                        runOnUiThread(()->Glide.with(GlobalApplcation.currentActivity).load(outputFileResults.getSavedUri()).into(imageView));
                    }

                    @Override
                    public void onError(@NonNull ImageCaptureException exception) {

                    }
                }

        );
    }

    private void updateImage() {
        BitmapDrawable drawable = (BitmapDrawable) imageView.getDrawable();
        Bitmap bitmap = drawable.getBitmap();
        byte[] bytes = getImageByte(bitmap);

        Bitmap copiedBitmap = byteArrayToBitmap(bytes);
        imageView2.setImageBitmap(copiedBitmap);
    }

    public byte[] getImageByte(Bitmap bitmap) {
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.JPEG, 100, out);

        return out.toByteArray();
    }

    public Bitmap byteArrayToBitmap( byte[] byteArray ) {
        Bitmap bitmap = BitmapFactory.decodeByteArray( byteArray, 0, byteArray.length ) ;
        return bitmap;
    }
}




/**
 * QR코드 인식 동작 순서
 * 카메라를 연 후 프리뷰를 가동한다.
 * 카메라로부터 지속적으로 영상을 받아들인다. [2]
 * 영상에서 밝기값만 추출[3]하여 이를 기반으로 이진화를 수행한다.[4]
 * Detector 클래스를 통해 QR코드 영역을 찾아냈다.
 * 찾아낸 영역을 Decoder 클래스를 통해 해석한다.
 * 결과 값과 결과 영상을 리턴
 * 결과 값을 분석하여 URL일 경우 탭하면 인터넷으로 연결되도록 한다.
 * 화면에 결과 영상과 결과 값을 출력한다.
 */
