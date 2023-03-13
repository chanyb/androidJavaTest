package com.example.smartbox_dup.screen.function.gallary;

import android.Manifest;
import android.content.ContentResolver;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.ImageDecoder;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.widget.Button;
import android.widget.ImageView;

import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.core.app.ActivityCompat;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.resource.bitmap.RoundedCorners;
import com.bumptech.glide.request.RequestOptions;
import com.example.smartbox_dup.R;
import com.example.smartbox_dup.sqlite.Database;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class GallaryTest extends AppCompatActivity {
    private Button btn_1, btn_2, btn_3, btn_4;
    private ConstraintLayout lo_images;
    private ImageView img_photo;
    private PhotoFragment photoFragment;
    static int CODE_REQUEST_GALLARY=1;

    ActivityResultLauncher<String> mGetContent = registerForActivityResult(new ActivityResultContracts.GetContent(),
            new ActivityResultCallback<Uri>() {
                @Override
                public void onActivityResult(Uri uri) {
                    // Handle the returned Uri
                    Log.i("this", "onActivityResult - URI: "+ uri);
                    Bitmap bitmap = getBitmapFromUri(uri);
                    ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
                    bitmap.compress(Bitmap.CompressFormat.JPEG, 100, byteArrayOutputStream);
                    byte[] byteArr = byteArrayOutputStream.toByteArray();
                    ContentValues cv = new ContentValues();
                    cv.put("datetime", "1");
                    cv.put("image", byteArr);
                    Database.getHuntInfoTable().insert("hunt_info", cv);

                }
            });

    ActivityResultLauncher<String> mGetMultiContent = registerForActivityResult(new ActivityResultContracts.GetMultipleContents(){
        @NonNull
        @Override
        public Intent createIntent(@NonNull Context context, @NonNull String input) {
            Log.i("this", input);
            Intent intent = super.createIntent(context, input);
            intent.putExtra(Intent.EXTRA_ALLOW_MULTIPLE, true);
            intent.addCategory(Intent.CATEGORY_OPENABLE);
            intent.setAction(Intent.ACTION_GET_CONTENT);

            return Intent.createChooser(intent, "사진 선택");
        }
    }, new ActivityResultCallback<List<Uri>>() {
        @Override
        public void onActivityResult(List<Uri> result) {
            Log.i("this", "선택된 Content 수: " + result.size());
            for (Uri uri : result) {
                Log.i("this", "onActivityResult - URI: "+ uri);
            }
        }
    });


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_function_gallary);
        init();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode == CODE_REQUEST_GALLARY) {
            Uri uri = data.getData();
            Log.i("this", "URI: "+ uri);
        }
    }

    private void init() {
        btn_1 = findViewById(R.id.btn_1);
        btn_1.setOnClickListener((view) -> {
            Intent photoPickerIntent = new Intent(Intent.ACTION_PICK);
            photoPickerIntent.setType("image/*");
            startActivityForResult(photoPickerIntent, CODE_REQUEST_GALLARY);
        });

        btn_2 = findViewById(R.id.btn_2);
        btn_2.setOnClickListener((view) ->{
            mGetContent.launch("image/*");
        });

        btn_3 = findViewById(R.id.btn_3);
        btn_3.setOnClickListener((view) ->{
            getImageFromSqlite();
        });

        btn_4 = findViewById(R.id.btn_4);
        btn_4.setOnClickListener((view) -> btn_4_click_event());

        photoFragment = new PhotoFragment();
        img_photo = findViewById(R.id.img_photo);
    }

    private void btn_4_click_event() {
        // 전체 이미지 uri 가져오기
        Log.i("this", "btn_4_click_event");
        FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
        ft.replace(R.id.lo_images, photoFragment);
        ft.commit();
    }


    public Bitmap getBitmapFromUri(Uri uri) {
        Bitmap bitmap = null;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.P) {
            try {
                bitmap = ImageDecoder.decodeBitmap(ImageDecoder.createSource(getContentResolver(), uri));
            } catch (IOException e) {
                e.printStackTrace();
            }
        } else {
            try {
                bitmap = MediaStore.Images.Media.getBitmap(getContentResolver(), uri);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return bitmap;
    }

    public void getImageFromSqlite() {
        Cursor cursor = Database.getHuntInfoTable().selectCursor("hunt_info", new String[]{"datetime", "image"}, null, null, null, null, null, null);
        while(cursor.moveToNext()) {
            String datetime = cursor.getString(cursor.getColumnIndexOrThrow("datetime"));
            byte[] imageBytes = cursor.getBlob(cursor.getColumnIndexOrThrow("image"));
            Bitmap bitmap = getBitmapFromBlob(imageBytes);
            if(bitmap == null) {
                Log.i("this", "bitmap is null");
            } else {
                Glide.with(this).load(bitmap).into(img_photo);
            }

//            BitmapDrawable ob = new BitmapDrawable(getResources(), bitmap);
//            lo_images.setBackgroundDrawable(ob);
        }

    }

    public Bitmap getBitmapFromBlob(byte[] bytes) {
        return BitmapFactory.decodeByteArray(bytes, 0, bytes.length);
    }


    public File getFileFromUri(Uri uri) {
        File file = null;
        if (uri.getScheme().equals("content")) {
            ContentResolver contentResolver = getContentResolver();
            String mimeType = contentResolver.getType(uri);

            if (mimeType == null) {
                // MIME 타입을 알 수 없는 경우 처리
                Log.e("this", "mimeType error occur: " + mimeType);
            } else if (mimeType.equals("image/jpeg") || mimeType.equals("image/png")) {
                // 이미지 파일 처리
                Cursor cursor = contentResolver.query(uri, new String[]{MediaStore.Images.Media.DATA}, null, null, null);
                if (cursor != null) {
                    cursor.moveToFirst();
                    String filePath = cursor.getString(cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATA));
                    cursor.close();
                    file = new File(filePath);
                }
            } else if (mimeType.equals("video/mp4") || mimeType.equals("video/x-msvideo")) {
                // 비디오 파일 처리
                Cursor cursor = contentResolver.query(uri, new String[]{MediaStore.Video.Media.DATA}, null, null, null);
                if (cursor != null) {
                    cursor.moveToFirst();
                    String filePath = cursor.getString(cursor.getColumnIndexOrThrow(MediaStore.Video.Media.DATA));
                    cursor.close();
                    file = new File(filePath);
                }
            } else {
                // 기타 파일 처리
                Log.e("this", "mimeType error occur: " + mimeType);
            }
        }
        return file;
    }
}