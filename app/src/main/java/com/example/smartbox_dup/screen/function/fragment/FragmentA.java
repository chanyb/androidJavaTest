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

public class FragmentA extends Fragment {
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        Log.i("this", "FragmentA - onCreateView");
        return inflater.inflate(R.layout.fragment_a, container, false);
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        Log.i("this", "FragmentA - onCreate");
        super.onCreate(savedInstanceState);
    }

    @Override
    public void onStart() {
        Log.i("this", "FragmentA - onStart");
        super.onStart();
    }

    @Override
    public void onDestroy() {
        Log.i("this", "FragmentA - onDestroy");
        super.onDestroy();
    }

    @Override
    public void onDestroyView() {
        Log.i("this", "FragmentA - onDestroyView");
        super.onDestroyView();
    }

    @Override
    public void onStop() {
        Log.i("this", "FragmentA - onStop");
        super.onStop();
    }

    @Override
    public void onAttach(@NonNull Context context) {
        Log.i("this", "FragmentA - onAttach");
        super.onAttach(context);
    }
}
