package com.example.smartbox_dup.screen.function.camera;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.ContentValues;
import android.content.Context;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.ImageDecoder;
import android.graphics.Paint;
import android.graphics.SurfaceTexture;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.Looper;
import android.provider.MediaStore;
import android.util.Log;
import android.view.Surface;
import android.view.TextureView;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.VideoView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.camera.core.Camera;
import androidx.camera.core.CameraSelector;
import androidx.camera.core.ImageCapture;
import androidx.camera.core.ImageCaptureException;
import androidx.camera.core.Preview;
import androidx.camera.core.UseCaseGroup;
import androidx.camera.core.VideoCapture;
import androidx.camera.lifecycle.ProcessCameraProvider;
import androidx.camera.video.Quality;
import androidx.camera.view.PreviewView;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.lifecycle.ProcessLifecycleOwner;

import com.bumptech.glide.Glide;
import com.example.smartbox_dup.R;
import com.example.smartbox_dup.utils.DatetimeManager;
import com.example.smartbox_dup.utils.FutureTaskRunner;
import com.example.smartbox_dup.utils.PermissionManager;
import com.google.android.gms.common.images.Size;
import com.google.common.util.concurrent.ListenableFuture;

import org.json.JSONException;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Executors;

import chanyb.android.java.GlobalApplcation;

public class CameraTest extends AppCompatActivity {
    private ListenableFuture<ProcessCameraProvider> cameraProviderFuture;
    PreviewView previewView;
    ImageCapture imageCapture;
    Button btn_capture, btn_video_capture;
    ImageView imageView, imageView2;
    UseCaseGroup useCaseGroup;
    VideoCapture videoCapture;
    Handler mHandler;
    TextureView textureView;
    private MediaPlayer mediaPlayer;
    private Context mContext;
    Surface surface1;

    Camera camera;
//    ConstraintLayout lo_preview;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_function_camera);
        mHandler = new Handler(Looper.getMainLooper());
        mContext = this;
        mediaPlayer = new MediaPlayer();
        Log.i("this", getExternalFilesDir(null).getAbsolutePath());
        if (!PermissionManager.getInstance().cameraPermissionCheck()) {
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
                    if (PermissionManager.getInstance().cameraPermissionCheck()) return true;
                }
            });
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.RECORD_AUDIO},0);
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

        btn_video_capture = findViewById(R.id.btn_video_capture);
        btn_video_capture.setOnClickListener(view -> {
            stopCapture();
        });


        previewView = findViewById(R.id.previewView);
//        cameraProviderFuture = ProcessCameraProvider.getInstance(GlobalApplcation.getContext());
//        cameraProviderFuture.addListener(() -> {
//            try {
//                ProcessCameraProvider cameraProvider = cameraProviderFuture.get();
//                bindPreview(cameraProvider);
//
//            } catch (ExecutionException | InterruptedException e) {
//                // No errors need to be handled for this Future.
//                // This should never be reached.
//            }
//        }, ContextCompat.getMainExecutor(GlobalApplcation.getContext()));

        btn_capture = findViewById(R.id.btn_capture);
        btn_capture.setOnClickListener((view) -> {
            capture();
        });

        textureView = findViewById(R.id.videoView22);




    }

    @SuppressLint("RestrictedApi")
    private VideoCapture videoCaptureTest(ListenableFuture<ProcessCameraProvider> cameraFuture) {
        ProcessCameraProvider camera = null;
        try {
            camera = cameraFuture.get();
        } catch (ExecutionException e) {
            e.printStackTrace();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        int rotation = getWindowManager().getDefaultDisplay().getRotation();
        Size resolution = new Size(1920, 1080);

        videoCapture = new VideoCapture.Builder()
                .setTargetRotation(Surface.ROTATION_0)
                .build();

        return videoCapture;
    }

    // A helper function to translate Quality to a string
    public String qualityToString(Quality quality) {
        if (Quality.UHD == quality) {
            return "UHD";
        } else if (Quality.FHD == quality) {
            return "FHD";
        } else if (Quality.HD == quality) {
            return "HD";
        } else if (Quality.SD == quality) {
            return "SD";
        }
        throw new IllegalArgumentException();
    }

    private void bindPreview(@NonNull ProcessCameraProvider cameraProvider) {
        Preview preview = new Preview.Builder().build();

        CameraSelector cameraSelector = new CameraSelector.Builder()
                .requireLensFacing(CameraSelector.LENS_FACING_BACK)
                .build();

        preview.setSurfaceProvider(previewView.getSurfaceProvider());
        imageCapture = new ImageCapture.Builder()
                .setTargetRotation(Surface.ROTATION_0)
                .build();

        useCaseGroup = new UseCaseGroup.Builder().addUseCase(preview).addUseCase(imageCapture).build();

        try{
            cameraProvider.unbindAll();
            camera = cameraProvider.bindToLifecycle(ProcessLifecycleOwner.get(), cameraSelector, preview, videoCaptureTest(cameraProviderFuture));
        } catch (Exception e) {
            Log.i("this", "error", e);
        }
    }

    private void stopCapture() {
        Log.i("this", "stopCapture() s");
        surface1 = new Surface(textureView.getSurfaceTexture());
        mediaPlayer.setSurface(null);
        mediaPlayer.setSurface(surface1);
//        TextureView.SurfaceTextureListener surfaceTextureListener = new TextureView.SurfaceTextureListener() {
//            @Override
//            public void onSurfaceTextureAvailable(SurfaceTexture surfaceTexture, int width, int height) {
//                // SurfaceTexture가 생성된 경우
//                surface1 = new Surface(surfaceTexture);
//                mediaPlayer.setSurface(surface1);
////                mediaPlayer.prepare();
////                mediaPlayer.start();
////                mediaPlayer.prepareAsync();
////                mediaPlayer.setOnPreparedListener(new MediaPlayer.OnPreparedListener() {
////                    @Override
////                    public void onPrepared(MediaPlayer mediaPlayer) {
////                        Log.i("this", "onPrepared");
////                        mediaPlayer.start();
////                    }
////                });
//            }
//
//            @Override
//            public void onSurfaceTextureSizeChanged(SurfaceTexture surfaceTexture, int width, int height) {
//                // SurfaceTexture의 크기가 변경된 경우
//            }
//
//            @Override
//            public boolean onSurfaceTextureDestroyed(SurfaceTexture surfaceTexture) {
//                // SurfaceTexture가 파괴된 경우
//                if (mediaPlayer != null) {
//                    mediaPlayer.stop();
//                    mediaPlayer.release();
//                    mediaPlayer = null;
//                }
//                return true;
//            }
//
//            @Override
//            public void onSurfaceTextureUpdated(SurfaceTexture surfaceTexture) {
//                // SurfaceTexture가 업데이트된 경우
//            }
//        };

        try{
            mediaPlayer.reset();
            mediaPlayer.setDataSource(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DCIM)+"/video.mp4");
            mediaPlayer.prepareAsync();
            mediaPlayer.setOnPreparedListener((mp) -> mp.start());
        } catch (Exception e) {
            Log.i("this", "error", e);
        }

        Log.i("this", "stopCapture() e");
//        startCapture();
//        mHandler.postDelayed(() -> {
//            videoCapture.stopRecording();
//        }, 10000);
//
//        mHandler.postDelayed(() -> {
//            Log.i("this", "12000 s");
//            final MediaPlayer[] mediaPlayer = {new MediaPlayer()};
//
//            // textureView에 mediaPlayer 연결
//            TextureView.SurfaceTextureListener surfaceTextureListener = new TextureView.SurfaceTextureListener() {
//                @Override
//                public void onSurfaceTextureAvailable(@NonNull SurfaceTexture surfaceTexture, int i, int i1) {
//                    Log.i("this", "onSurfaceTextureAvailable");
//                    Surface surface1 = new Surface(surfaceTexture);
//                    try {
//                        mediaPlayer[0] = new MediaPlayer();
//                        mediaPlayer[0].setDataSource(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DCIM)+"/video.mp4");
//                        mediaPlayer[0].setSurface(surface1);
//                        mediaPlayer[0].prepare();
//                        mediaPlayer[0].start();
//                    } catch (IOException e) {
//                        e.printStackTrace();
//                    }
//                }
//
//                @Override
//                public void onSurfaceTextureSizeChanged(@NonNull SurfaceTexture surfaceTexture, int i, int i1) {
//
//                }
//
//                @Override
//                public boolean onSurfaceTextureDestroyed(@NonNull SurfaceTexture surfaceTexture) {
//                    Log.i("this", "onSurfaceTextureDestroyed");
//                    if (mediaPlayer[0] != null) {
//                        mediaPlayer[0].release();
//                        mediaPlayer[0] = null;
//                    }
//                    return true;
//                }
//
//                @Override
//                public void onSurfaceTextureUpdated(@NonNull SurfaceTexture surfaceTexture) {
//
//                }
//            };
//
//            textureView.setSurfaceTextureListener(surfaceTextureListener);
//            Log.i("this", "12000 e");
//        }, 12000);
    }

    @SuppressLint("RestrictedApi")
    private void startCapture() {
        File file = new File(getExternalFilesDir(null), "video.mp4");
        VideoCapture.OutputFileOptions outputFile = new VideoCapture.OutputFileOptions.Builder(file).build();
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.RECORD_AUDIO) != PackageManager.PERMISSION_GRANTED) {
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.
            Log.e("this", "recordAudio permission deniedError");
        }
        videoCapture.startRecording(outputFile, Executors.newSingleThreadExecutor(), new VideoCapture.OnVideoSavedCallback() {
            @Override
            public void onVideoSaved(@NonNull VideoCapture.OutputFileResults outputFileResults) {
                Log.i("this", "onVideoSaved");
                File dcimDir = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DCIM);
                File destFile = new File(dcimDir, "video.mp4");

                try (InputStream is = new FileInputStream(file);
                     OutputStream os = new FileOutputStream(destFile)) {
                    byte[] buffer = new byte[1024];
                    int bytesRead;
                    while ((bytesRead = is.read(buffer)) != -1) {
                        os.write(buffer, 0, bytesRead);
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onError(int videoCaptureError, @NonNull String message, @Nullable Throwable cause) {
                Log.e("this", "onError: " + message, cause);
            }
        });
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
            cs.drawText("대전광역시 서구 관저북로 80 원앙마을아파트", 20f, height * 3 + 15f, tPaint); //주소정보
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


    public static byte[] getVideoFile(String fileName) {

        String sz = fileName + ".mp4";
        File file = new File(sz);
        int fileSize = (int) file.length();
        byte[] outBuffer = new byte[ fileSize];
        byte[] readBuffer = new byte[1024];

        if (file.exists()) {
            try {
                FileInputStream fileInputStream = new FileInputStream(file);
                int count = 0;
                int nReadSize =0 ;
                while ((count = fileInputStream.read(readBuffer)) > 0){
                    System.arraycopy(readBuffer , 0 , outBuffer, nReadSize, count);
                    nReadSize += count;
                }
                fileInputStream.close();
                return outBuffer;
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return null;
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
