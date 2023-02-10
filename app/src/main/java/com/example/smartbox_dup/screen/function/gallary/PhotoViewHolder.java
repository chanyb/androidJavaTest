package com.example.smartbox_dup.screen.function.gallary;

import android.content.Context;
import android.view.View;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.smartbox_dup.R;


public class PhotoViewHolder extends RecyclerView.ViewHolder {
    public ImageView img_photo, btn_remove;

    public PhotoViewHolder(Context context, @NonNull View itemView) {
        super(itemView);
        img_photo = itemView.findViewById(R.id.img_photo);
    }
}
