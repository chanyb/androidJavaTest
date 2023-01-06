package com.example.smartbox_dup.screen.function.gallary;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.widget.Button;

import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.example.smartbox_dup.R;
import com.example.smartbox_dup.screen.function.socket.TCPClient;

import java.util.List;

public class GallaryTest extends AppCompatActivity {
    Button btn_1, btn_2, btn_3;
    static int CODE_REQUEST_GALLARY=1;

    ActivityResultLauncher<String> mGetContent = registerForActivityResult(new ActivityResultContracts.GetContent(),
            new ActivityResultCallback<Uri>() {
                @Override
                public void onActivityResult(Uri uri) {
                    // Handle the returned Uri
                    Log.i("this", "onActivityResult - URI: "+ uri);
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
//            mGetMultiContent.launch("image/*");
            Intent tel = new Intent(Intent.ACTION_DIAL, Uri.parse("tel:15884343"));
            startActivity(tel);
        });
    }
}