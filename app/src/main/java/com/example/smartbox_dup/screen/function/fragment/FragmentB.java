package com.example.smartbox_dup.screen.function.fragment;

import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.example.smartbox_dup.R;

public class FragmentB extends Fragment {
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        Log.i("this", "FragmentB - onCreateView");
        return inflater.inflate(R.layout.fragment_b, container, false);
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        Log.i("this", "FragmentB - onCreate");
        super.onCreate(savedInstanceState);
    }

    @Override
    public void onStart() {
        Log.i("this", "FragmentB - onStart");
        super.onStart();
    }

    @Override
    public void onDestroy() {
        Log.i("this", "FragmentB - onDestroy");
        super.onDestroy();
    }

    @Override
    public void onDestroyView() {
        Log.i("this", "FragmentB - onDestroyView");
        super.onDestroyView();
    }

    @Override
    public void onStop() {
        Log.i("this", "FragmentB - onStop");
        super.onStop();
    }

    @Override
    public void onAttach(@NonNull Context context) {
        Log.i("this", "FragmentB - onAttach");
        super.onAttach(context);
    }
}
