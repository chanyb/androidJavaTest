package com.example.smartbox_dup.screen.home;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.smartbox_dup.R;
import com.example.smartbox_dup.viewmodel.User;

import java.util.ArrayList;

public class NaverMapOveralyAdapter extends RecyclerView.Adapter<NaverMapOverlayViewHolder> {
    private ArrayList<User> arrayList = new ArrayList<>();

    @NonNull
    @Override
    public NaverMapOverlayViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        Context context = parent.getContext();

        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View view = inflater.inflate(R.layout.member, parent, false);

        NaverMapOverlayViewHolder naverMapOverlayViewHolder = new NaverMapOverlayViewHolder(context, view);
        return naverMapOverlayViewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull NaverMapOverlayViewHolder holder, int position) {
        // 바인딩 될 때마다 할 것
        User user = (User) arrayList.get(position);
        holder.item_member_name.setText(user.getUsername());
        holder.item_member_location.setText(user.getLocation());
        holder.item_member_locatedtime.setText(user.getLocatedtime());
    }

    @Override
    public int getItemCount() {
        return arrayList.size();
    }

    public void addItem(User user) {
        Log.i("this", "getTO");
        Log.i("this", user.getUsername());
        arrayList.add(user);
    }
}
