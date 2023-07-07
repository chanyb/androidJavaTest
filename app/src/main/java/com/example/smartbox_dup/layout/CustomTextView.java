package com.example.smartbox_dup.layout;

import android.content.Context;
import android.content.res.TypedArray;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.constraintlayout.widget.ConstraintLayout;

import com.example.smartbox_dup.R;

public class CustomTextView extends ConstraintLayout {

    private ImageView img_background;

    public CustomTextView(@NonNull Context context) {
        super(context);
        init();
    }
    public CustomTextView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
        getAttrs(attrs);
    }

    public CustomTextView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs);
        init();
        getAttrs(attrs, defStyle);
    }

    private void init() {
        String infService = Context.LAYOUT_INFLATER_SERVICE;
        LayoutInflater li = (LayoutInflater) getContext().getSystemService(infService);
        View v = li.inflate(R.layout.custom_layout, this, false);
        addView(v);


    }

    private void getAttrs(AttributeSet attrs) {
        TypedArray typedArray = getContext().obtainStyledAttributes(attrs, R.styleable.BcTextView);

        setTypeArray(typedArray);
    }


    private void getAttrs(AttributeSet attrs, int defStyle) {
        TypedArray typedArray = getContext().obtainStyledAttributes(attrs, R.styleable.BcTextView, defStyle, 0);
        setTypeArray(typedArray);
    }

    private void setTypeArray(TypedArray typedArray) {

        int textColor = typedArray.getColor(R.styleable.BcTextView_textColor, 0);

        String text_string = typedArray.getString(R.styleable.BcTextView_text);

        typedArray.recycle();
    }

    public void setText(String text) {
    }

    public void setDefaultText(String text) {
    }

    public void rotate() {

    }
}
