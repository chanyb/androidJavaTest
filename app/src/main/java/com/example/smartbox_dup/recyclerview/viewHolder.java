package com.example.smartbox_dup.recyclerview;

import android.content.Context;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.smartbox_dup.R;

public class viewHolder extends RecyclerView.ViewHolder{
    public TextView textView;
    public Button button;
    public viewHolder(Context context, @NonNull View itemView) {
        super(itemView);

        textView = itemView.findViewById(R.id.textView);
        button = itemView.findViewById(R.id.button);




    }
}
