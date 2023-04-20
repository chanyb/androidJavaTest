package com.example.smartbox_dup.screen.function.etc;

import android.content.ComponentName;
import android.content.pm.PackageManager;
import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.example.smartbox_dup.databinding.ActivityFunctionEtcBinding;

public class EtcActivity extends AppCompatActivity {

    private ActivityFunctionEtcBinding binding;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityFunctionEtcBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        init();
    }

    private void init() {
        binding.btn1.setOnClickListener((v) -> {
//            int status = getPackageManager().getComponentEnabledSetting(new ComponentName(this, EtcActivity.class));
//
//            if(status == PackageManager.COMPONENT_ENABLED_STATE_ENABLED) {
//                getPackageManager().setComponentEnabledSetting(new ComponentName(this, Intro.class), PackageManager.COMPONENT_ENABLED_STATE_DISABLED, PackageManager.DONT_KILL_APP);
//                getPackageManager().setComponentEnabledSetting(new ComponentName(this, EtcActivity.class), PackageManager.COMPONENT_ENABLED_STATE_ENABLED, PackageManager.DONT_KILL_APP);
//            }
//            else {
//                getPackageManager().setComponentEnabledSetting(new ComponentName(this, EtcActivity.class), PackageManager.COMPONENT_ENABLED_STATE_ENABLED, PackageManager.DONT_KILL_APP);
//                getPackageManager().setComponentEnabledSetting(new ComponentName(this, EtcActivity.class), PackageManager.COMPONENT_ENABLED_STATE_DISABLED, PackageManager.DONT_KILL_APP);
//            }

        });
    }
}
