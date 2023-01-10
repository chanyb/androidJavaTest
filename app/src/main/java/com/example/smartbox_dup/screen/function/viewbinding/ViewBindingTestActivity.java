package com.example.smartbox_dup.screen.function.viewbinding;

import android.graphics.Color;
import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.example.smartbox_dup.databinding.ActivityFunctionViewbindingBinding;

public class ViewBindingTestActivity extends AppCompatActivity {

    private ActivityFunctionViewbindingBinding binding;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityFunctionViewbindingBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        updateUI();
    }

    private void updateUI() {

        runOnUiThread(() -> {
            binding.txt1.setText("asdfasdfasdf");
            binding.txt1.setBackgroundColor(Color.GRAY);
        });
    }
}
