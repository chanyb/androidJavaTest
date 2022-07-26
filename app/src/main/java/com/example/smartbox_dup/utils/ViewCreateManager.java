package com.example.smartbox_dup.utils;

import android.content.Context;
import android.media.Image;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.constraintlayout.widget.ConstraintLayout;

import com.example.smartbox_dup.R;

import org.json.JSONException;
import org.json.JSONObject;



public class ViewCreateManager {
    private static ViewCreateManager instance = new ViewCreateManager();
    private ViewCreateManager () {
        savedViews = new JSONObject();
    }
    public enum ViewType {BUTTON, TEXTVIEW, IMAGEVIEW}
    public enum LayoutType {CONSTRAINTLAYOUT}
    private JSONObject savedViews;
    private View view;
    ViewGroup.LayoutParams layoutParams;

    public static ViewCreateManager getInstance() {
        return instance;
    }


    /*
     * (1/2) createView - LayoutType을 선택하면, 기본으로 layoutParams을 주고 View를 생성하는 함수
     */
    public String createView(Context context, ViewType viewType, ViewGroup layout, LayoutType layoutType) {

        switch (layoutType) {
            case CONSTRAINTLAYOUT:
                layoutParams = new ConstraintLayout.LayoutParams(0,0);
                ((ConstraintLayout.LayoutParams) layoutParams).matchConstraintPercentWidth = 0.9f;
                ((ConstraintLayout.LayoutParams) layoutParams).matchConstraintPercentHeight = 0.1f;
                ((ConstraintLayout.LayoutParams) layoutParams).topToTop = layout.getId();
                ((ConstraintLayout.LayoutParams) layoutParams).bottomToBottom = layout.getId();
                ((ConstraintLayout.LayoutParams) layoutParams).startToStart = layout.getId();
                ((ConstraintLayout.LayoutParams) layoutParams).endToEnd = layout.getId();
                break;
            default:
                throw new IllegalStateException("Unexpected value: " + layoutType);
        }

        switch (viewType) {
            case BUTTON:
                view = new Button(context);
                ((Button) view).setText("new Button");
                break;
            case TEXTVIEW:
                view = new TextView(context);
                ((TextView) view).setText("new TextView");
                break;
            case IMAGEVIEW:
                view = new ImageView(context);
                ((ImageView) view).setBackgroundResource(R.drawable.example);
                break;
            default:
                throw new IllegalStateException("Unexpected value: " + viewType);
        }

        view.setLayoutParams(layoutParams);
        layout.addView(view);

        String id = null;
        try {
            id = String.valueOf(DatetimeManager.getInstance().getUTCDateTime().get("timestamp"));
        } catch (JSONException e) {
            e.printStackTrace();
        }
        saveView(id, view);
        return id;
    }

    /*
     * (2/2) createView - 사전에 정의한 LayoutParams을 인자로 받아 View에 적용해주는 함수
     */
    public String createView(Context context, ViewType viewType, ViewGroup layout, ViewGroup.LayoutParams layoutParams) {


        switch (viewType) {
            case BUTTON:
                view = new Button(context);
                ((Button) view).setText("new Button");
                break;
            case TEXTVIEW:
                view = new TextView(context);
                ((TextView) view).setText("new TextView");
                break;
            case IMAGEVIEW:
                view = new ImageView(context);
                ((ImageView) view).setBackgroundResource(R.drawable.example);

            default:
                throw new IllegalStateException("Unexpected value: " + viewType);
        }

        view.setLayoutParams(layoutParams);
        layout.addView(view);

        String id = null;
        try {
            id = String.valueOf(DatetimeManager.getInstance().getUTCDateTime().get("timestamp"));
        } catch (JSONException e) {
            e.printStackTrace();
        }
        saveView(id, view);
        return id;
    }

    private void saveView(String id,View view) {
        try {
            savedViews.put(id, view);
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    public View getViewById(String id) {
        try {
            return (View) savedViews.get(id);
        } catch (JSONException e) {
            e.printStackTrace();
        }

        return null;
    }
}
