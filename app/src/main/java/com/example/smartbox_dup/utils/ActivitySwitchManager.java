package com.example.smartbox_dup.utils;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.view.View;

import androidx.core.app.ActivityOptionsCompat;

public class ActivitySwitchManager {
    private static ActivitySwitchManager instance = new ActivitySwitchManager();

    private ActivitySwitchManager () {}

    public static ActivitySwitchManager getInstance() {
        return instance;
    }

    public void changeActivity(Context context, Class after) {
        Intent intent = new Intent(context, after);
        context.startActivity(intent);
    }

    public void changeActivity(Context context, Class after, Intent _intent) {
        context.startActivity(_intent);
    }

    public void changeActivity(Context context, Class after, Activity activity, View view, String _sharedElementName) {
        Intent intent = new Intent(context, after);

        ActivityOptionsCompat activityOptionsCompat = ActivityOptionsCompat.makeSceneTransitionAnimation(activity, view, _sharedElementName);
        context.startActivity(intent, activityOptionsCompat.toBundle());

    }
}
