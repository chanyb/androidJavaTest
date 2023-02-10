package com.example.smartbox_dup.screen.function.gallary;

import android.app.Activity;
import android.content.Context;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.resource.bitmap.RoundedCorners;
import com.bumptech.glide.request.RequestOptions;
import com.example.smartbox_dup.R;

import java.util.ArrayList;


public class PhotoAdapter extends RecyclerView.Adapter<PhotoViewHolder> {

    private ArrayList<Uri> photoList;
    private Context mContext;

    public PhotoAdapter(ArrayList<Uri> _photoList) {
        photoList = _photoList;
    }

    @NonNull
    @Override
    public PhotoViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        // 뷰홀더를 건네줘야 함.
        mContext = parent.getContext();
        LayoutInflater inflater = (LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View view = inflater.inflate(R.layout.item_photo, parent, false);
        PhotoViewHolder vHolder = new PhotoViewHolder(mContext, view);
        return vHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull PhotoViewHolder holder, int position) {
        // 바인딩 될 때마다 뭐할래?
        Uri uri = photoList.get(position);
        ((Activity)mContext).runOnUiThread(() -> Glide.with(mContext).load(uri).into(holder.img_photo));
    }

    @Override
    public int getItemCount() {
        return photoList.size();
    }

}
