package com.example.smartbox_dup.screen.signup;

import android.os.Bundle;
import android.widget.ImageView;

import androidx.appcompat.app.AppCompatActivity;

import com.bumptech.glide.Glide;
import com.example.smartbox_dup.R;

public class SignUpActivity3 extends AppCompatActivity {
    ImageView iv_gif;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sigup3);

        iv_gif = findViewById(R.id.iv_gif);
        Glide.with(this).load(R.drawable.pang_bg).into(iv_gif);
    }
}