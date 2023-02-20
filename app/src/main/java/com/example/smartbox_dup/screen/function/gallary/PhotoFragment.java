package com.example.smartbox_dup.screen.function.gallary;

import android.Manifest;
import android.content.ContentResolver;
import android.content.ContentUris;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.provider.MediaStore;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.app.ActivityCompat;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.smartbox_dup.R;
import com.example.smartbox_dup.utils.FutureTaskRunner;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class PhotoFragment extends Fragment {

    private TextView btn_cancel;
    private Spinner spinner_category;
    private ArrayAdapter arrayAdapter;
    private RecyclerView recycler_photo_view;
    private PhotoAdapter photoAdapter;
    private ArrayList<Uri> photoList;
    private Set<String> categorySet;
    private Handler mHandler;
    private static final String recentContents = "최근항목";

    public static final String[] IMAGE_PROJECTION = new String[] {
            MediaStore.Images.ImageColumns._ID,
            MediaStore.Images.ImageColumns.TITLE,
            MediaStore.Images.ImageColumns.DATE_ADDED,
            MediaStore.Images.ImageColumns.BUCKET_DISPLAY_NAME
    };

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_images, container, false);
    }

    @Override
    public void onStart() {
        super.onStart();
        init();
    }


    public void init() {
        mHandler = new Handler(Looper.getMainLooper());
        btn_cancel = getView().findViewById(R.id.btn_cancel);
        recycler_photo_view = getView().findViewById(R.id.recycler_photo_view);
        spinner_category = getView().findViewById(R.id.spinner_category);
        categorySet = new HashSet<>();
        FutureTaskRunner<Boolean> taskRunner = new FutureTaskRunner<>();
        taskRunner.nextTask(() -> {
            Object obj = getImageByCategory(recentContents);
            while(obj == null) {
                Thread.sleep(500);
            }
            return true;
        });
        taskRunner.nextTask(() -> {
            spinnerAdapterSetting();
            return true;
        });
        taskRunner.start();


    }

    private ArrayList<Uri> getEntireImage() {
        if(ActivityCompat.checkSelfPermission(getActivity(), Manifest.permission.READ_EXTERNAL_STORAGE) == PackageManager.PERMISSION_DENIED) {
            ActivityCompat.requestPermissions(getActivity(), new String[]{Manifest.permission.READ_EXTERNAL_STORAGE}, 0);
            return null;
        }
        ArrayList<Uri> arr = new ArrayList<>();
        ContentResolver cr = getActivity().getContentResolver();
        Uri uri = MediaStore.Images.Media.EXTERNAL_CONTENT_URI;
        Cursor cursor = null;
        cursor = cr.query(uri, IMAGE_PROJECTION, null, null, "date_added desc");

        Log.i("this", "getEntireImage()");
        if (cursor != null) {
            if(cursor.getCount() == 0) {
                Log.i("this", "cursor count 0");
            }
            while (cursor.moveToNext()) {
                long _id = cursor.getLong(0);
                String title = cursor.getString(1);
                String date_added = cursor.getString(2);
                String category = cursor.getString(3);
                arr.add(ContentUris.withAppendedId(uri, _id));
                categorySet.add(category);
            }
        } else {
            Log.i("this", "cursor is null");
        }

        photoAdapter = new PhotoAdapter(arr);
        recycler_photo_view.setAdapter(photoAdapter);

        return arr;
    }

    private void spinnerAdapterSetting() {
        String[] categories = new String[this.categorySet.size()+1];
        categories[0] = recentContents;
        for(int idx=0; idx<this.categorySet.size(); idx++) {
            categories[idx+1] = (String) categorySet.toArray()[idx];
        }
        arrayAdapter = new ArrayAdapter(getContext(), com.airbnb.lottie.R.layout.support_simple_spinner_dropdown_item, categories) {
            @NonNull
            @Override
            public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
                View v = super.getView(position, convertView, parent);
                ((TextView)v).setGravity(Gravity.CENTER);
                return v;
            }

            @Override
            public View getDropDownView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
                View v = super.getDropDownView(position, convertView, parent);
                ((TextView)v).setGravity(Gravity.CENTER);
                return v;
            }
        };

        spinner_category.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                Log.i("this", "onItemSelected - position: " + position);
                getImageByCategory((String) categorySet.toArray()[position]);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        mHandler.post(() -> {
            spinner_category.setAdapter(arrayAdapter);
        });
    }

    private ArrayList<Uri> getImageByCategory(String category) {
        if(ActivityCompat.checkSelfPermission(getActivity(), Manifest.permission.READ_EXTERNAL_STORAGE) == PackageManager.PERMISSION_DENIED) {
            ActivityCompat.requestPermissions(getActivity(), new String[]{Manifest.permission.READ_EXTERNAL_STORAGE}, 0);
            return null;
        }
        ArrayList<Uri> arr = new ArrayList<>();
        ContentResolver cr = getActivity().getContentResolver();
        Uri uri = MediaStore.Images.Media.EXTERNAL_CONTENT_URI;

        Cursor cursor = null;
        if(category.equals(recentContents)) {
            cursor = cr.query(uri, IMAGE_PROJECTION, null, null, "date_added desc");
        } else {
            cursor = cr.query(uri, IMAGE_PROJECTION, MediaStore.Images.ImageColumns.BUCKET_DISPLAY_NAME+"=?", new String[]{category}, "date_added desc");
        }
        while(cursor.moveToNext()) {
            long _id = cursor.getLong(0);
            String title = cursor.getString(1);
            String date_added = cursor.getString(2);
            String cat = cursor.getString(3);
            Log.i("this", cat);
            arr.add(ContentUris.withAppendedId(uri, _id));
            categorySet.add(cat);
        }

        mHandler.post(() -> {
            photoAdapter = new PhotoAdapter(arr);
            recycler_photo_view.setAdapter(photoAdapter);
        });
        return arr;
    }
}
