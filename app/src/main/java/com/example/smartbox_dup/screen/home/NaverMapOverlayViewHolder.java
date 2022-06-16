package com.example.smartbox_dup.screen.home;

import android.content.Context;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.smartbox_dup.R;

public class NaverMapOverlayViewHolder extends RecyclerView.ViewHolder {
    public ImageView item_member_icon;
    public TextView item_member_name;
    public TextView item_member_location;
    public TextView item_member_locatedtime;

    public NaverMapOverlayViewHolder(Context context, @NonNull View itemView) {
        super(itemView);

        item_member_name = itemView.findViewById(R.id.item_member_name);
        item_member_location = itemView.findViewById(R.id.item_member_location);
        item_member_locatedtime = itemView.findViewById(R.id.item_member_locatedtime);
        item_member_icon = itemView.findViewById(R.id.item_member_icon);
    }
}
