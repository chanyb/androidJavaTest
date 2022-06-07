package com.example.smartbox_dup;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.smartbox_dup.R;
import com.example.smartbox_dup.viewHolder;

import java.util.ArrayList;

public class adapter extends RecyclerView.Adapter<viewHolder>{

    private ArrayList<String> arrayList;

    public adapter() {
        arrayList = new ArrayList<>();
    }

    @NonNull
    @Override
    public viewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        // 뷰홀더를 건네줘야 함.
        Context context = parent.getContext();
        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View view = inflater.inflate(R.layout.item, parent, false);

        viewHolder vHolder = new viewHolder(context, view);

        return vHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull viewHolder holder, int position) {
        // 바인딩 될 때마다 뭐할래?
        String text = arrayList.get(position);
        holder.textView.setText(text);
    }


    @Override
    public int getItemCount() {
        return arrayList.size();
    }

    public void setArrayData(String _str) {
        arrayList.add(_str);
    }
}
