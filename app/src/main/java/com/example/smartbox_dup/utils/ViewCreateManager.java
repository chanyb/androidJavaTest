package com.example.smartbox_dup.utils;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import org.json.JSONException;
import org.json.JSONObject;



public class ViewCreateManager {
    private static ViewCreateManager instance = new ViewCreateManager();
    private ViewCreateManager () {
        savedViews = new JSONObject();
    }
    public enum ViewType {Button, TextView}
    public enum LayoutType {ConstraintLayout}
    private JSONObject savedViews;

    public static ViewCreateManager getInstance() {
        return instance;
    }


    public String createView(Context context, ViewType viewType, ViewGroup layout, ViewGroup.LayoutParams layoutParams) {
        View view;

        switch (viewType) {
            case Button:
                view = new Button(context);
                break;
            case TextView:
                view = new TextView(context);
                ((TextView) view).setText("new TextView");
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
