package com.example.smartbox_dup.screen.function.gallary;

import android.Manifest;
import android.content.ContentResolver;
import android.content.ContentUris;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
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

import java.util.ArrayList;

public class PhotoFragment extends Fragment {

    private TextView btn_cancel;
    private Spinner spinner_category;
    private ArrayAdapter arrayAdapter;
    private RecyclerView recycler_photo_view;
    private PhotoAdapter photoAdapter;
    private ArrayList<Uri> photoList;
    private ArrayList<String> albumList;

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
        btn_cancel = getView().findViewById(R.id.btn_cancel);
        recycler_photo_view = getView().findViewById(R.id.recycler_photo_view);
        spinner_category = getView().findViewById(R.id.spinner_category);
        arrayAdapter = new ArrayAdapter(getContext(), com.airbnb.lottie.R.layout.support_simple_spinner_dropdown_item, new String[]{"최근 항목","폴더1","폴더2"}) {
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
        spinner_category.setTextAlignment(View.TEXT_ALIGNMENT_CENTER);
        spinner_category.setGravity(Gravity.CENTER);
        spinner_category.setForegroundGravity(Gravity.CENTER_HORIZONTAL);
        spinner_category.setAdapter(arrayAdapter);
        photoList = getEntireImage();
        albumList = new ArrayList<>();
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
                String album = cursor.getString(3);
                Log.i("this", date_added + " / " + title + " / " + _id + " / " + album);
                arr.add(ContentUris.withAppendedId(uri, _id));
            }
        } else {
            Log.i("this", "cursor is null");
        }

        photoAdapter = new PhotoAdapter(arr);
        recycler_photo_view.setAdapter(photoAdapter);
        return arr;
    }
}
