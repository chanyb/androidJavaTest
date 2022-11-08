package com.example.smartbox_dup.screen.function.fragment;

import android.os.Bundle;
import android.util.Log;
import android.widget.Button;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import com.example.smartbox_dup.R;

public class FragmentActivity extends AppCompatActivity {
    FragmentManager fragmentManager;
    Fragment fragmentA, fragmentB;
    Button btn_1, btn_2, btn_3;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_function_fragment);
        init();
        Log.i("this", "FragmentActivity-onCreate");
    }

    @Override
    protected void onResume() {
        super.onResume();
        Log.i("this", "FragmentActivity-onResume");
    }

    @Override
    protected void onStop() {
        Log.i("this", "FragmentActivity-onStop");
        super.onStop();
    }

    @Override
    protected void onRestart() {
        Log.i("this", "FragmentActivity-onRestart");
        super.onRestart();
    }

    private void init() {
        fragmentManager = getSupportFragmentManager();
        fragmentA = new FragmentA();
        fragmentB = new FragmentB();
        btn_1 = findViewById(R.id.btn_1);
        btn_1.setOnClickListener((view) -> {
            FragmentTransaction transaction = fragmentManager.beginTransaction();
            transaction.add(R.id.lo_fragment,fragmentA);
            transaction.commit();
        });

        btn_2 = findViewById(R.id.btn_2);
        btn_2.setOnClickListener((view) -> {
            FragmentTransaction transaction = fragmentManager.beginTransaction();
            transaction.replace(R.id.lo_fragment,fragmentB);
            transaction.commit();
        });
    }
}
