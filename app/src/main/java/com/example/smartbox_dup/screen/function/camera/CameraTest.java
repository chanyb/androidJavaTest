package com.example.smartbox_dup.screen.function.camera;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.ImageDecoder;
import android.graphics.Matrix;
import android.graphics.Paint;
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
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
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
//            updateImage();
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
                        updateImage(outputFileResults.getSavedUri());
                    }

                    @Override
                    public void onError(@NonNull ImageCaptureException exception) {

                    }
                }

        );
    }

    private void updateImage(Uri uri) {
        Bitmap bitmap = getBitmapFromUri(uri);
        Bitmap infoBitmap = addInfoImage(GlobalApplcation.getContext(), bitmap, 33.123412, 127.15124231);
        runOnUiThread(() -> imageView2.setImageBitmap(infoBitmap));
    }


    public static void saveBitmapToGallery(Bitmap bitmap, String fileName, String fileDesc) {
        MediaStore.Images.Media.insertImage(GlobalApplcation.getContext().getContentResolver(), bitmap, fileName, fileDesc);
    }

    public static Bitmap getBitmapFromUri(Uri uri) {
        Bitmap bitmap = null;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.P) {
            try {
                bitmap = ImageDecoder.decodeBitmap(ImageDecoder.createSource(GlobalApplcation.getContext().getContentResolver(), uri));
            } catch (IOException e) {
                e.printStackTrace();
            }
        } else {
            try {
                bitmap = MediaStore.Images.Media.getBitmap(GlobalApplcation.getContext().getContentResolver(), uri);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return bitmap;
    }

    public static Bitmap addInfoImage(Context context, Bitmap original, double mLon, double mLat){
//        Bitmap dest = Bitmap.createBitmap(toEdit.getWidth(), toEdit.getHeight(), Bitmap.Config.ARGB_8888);
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        String dateTime = sdf.format(Calendar.getInstance().getTime()); // reading local time in the system

        Bitmap bAddedInfo = original.copy(Bitmap.Config.ARGB_8888, true);
        Canvas cs = new Canvas(bAddedInfo);
        Paint tPaint = new Paint();
        tPaint.setTextSize(100);
        tPaint.setColor(Color.WHITE);
        tPaint.setStyle(Paint.Style.FILL);
        float height = tPaint.measureText("yY");
//        cs.drawBitmap(toEdit, 0f, 0f, null); //원본비트맵
        cs.drawText(dateTime, 20f, height + 15f, tPaint); //날짜시간
        if (mLat != 0.0 && mLon != 0.0) {
            String sX = String.format("%.6f", mLon);
            String sY = String.format("%.6f", mLat);
            cs.drawText(sY + ", " + sX, 20f, height * 2 + 15f, tPaint); //위도 + 경도 - 34.9506986, 127.4872429
            cs.drawText("대전광역시 서구 관저북로 80 원앙마을아파트 213동 1201호", 20f, height * 3 + 15f, tPaint); //주소정보
        }

        saveBitmapToGallery(bAddedInfo, "test", "");

        return bAddedInfo;
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
