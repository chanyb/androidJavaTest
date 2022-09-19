package com.example.smartbox_dup.utils;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.view.View;

import androidx.core.app.ActivityOptionsCompat;

public class ActivitySwitchManager {
    private static ActivitySwitchManager instance = new ActivitySwitchManager();

    public static ActivitySwitchManager getInstance() {
        return instance;
    }

    public void changeActivity(Context context, Class after, boolean finish) {
        Intent intent = new Intent(context, after);
        context.startActivity(intent);
        if(finish) ((Activity) context).finish();
    }

    public void changeActivity(Context context, Intent intent, boolean finish) {
        context.startActivity(intent);
        if(finish) ((Activity) context).finish();
    }

    public void changeActivity(Context context, Class after, Activity activity, View view, String _sharedElementName) {
        Intent intent = new Intent(context, after);

        ActivityOptionsCompat activityOptionsCompat = ActivityOptionsCompat.makeSceneTransitionAnimation(activity, view, _sharedElementName);
        context.startActivity(intent, activityOptionsCompat.toBundle());

    }
}
