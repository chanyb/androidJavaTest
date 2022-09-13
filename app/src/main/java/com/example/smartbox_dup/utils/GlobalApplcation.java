package com.example.smartbox_dup.utils;

import android.app.Activity;
import android.app.Application;
import android.os.Bundle;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

public class GlobalApplcation extends Application {
    private static volatile GlobalApplcation instance;
    private static String TAG = "activity";
    public static GlobalApplcation getContext() {
        return instance;
    }
    private enum State {
        None, Foreground, Background
    }
    private State state;
    private int running;

    public interface Listener {
        void onBecameForeground();
        void onBecameBackground();
    }

    private Listener stateListener;

    @Override
    public void onCreate() {
        super.onCreate();
        Log.i("this", "GlobalApplication - onCreate");
        registerActivityLifecycleCallbacks(activityLifecycleCallbacks);
        instance = this;
        state = State.None;
    }

    ActivityLifecycleCallbacks activityLifecycleCallbacks = new ActivityLifecycleCallbacks() {
        @Override
        public void onActivityCreated(@NonNull Activity activity, @Nullable Bundle bundle) {

        }

        @Override
        public void onActivityStarted(@NonNull Activity activity) {
            Log.i(TAG, "onActivityStarted: " + activity);
            if (++running == 1 && !activity.isChangingConfigurations()) {
                state = State.Foreground;
                if (stateListener != null) stateListener.onBecameForeground();
            }
        }

        @Override
        public void onActivityResumed(@NonNull Activity activity) {
            Log.i(TAG, "onActivityResumed: " + activity);
        }

        @Override
        public void onActivityPaused(@NonNull Activity activity) {
            Log.i(TAG, "onActivityPaused: " + activity);
        }

        @Override
        public void onActivityStopped(@NonNull Activity activity) {
            Log.i(TAG, "onActivityStopped: " + activity);
            if (--running == 0 && !activity.isChangingConfigurations()) {
                state = State.Background;
                if (stateListener != null) stateListener.onBecameBackground();
            }
        }

        @Override
        public void onActivitySaveInstanceState(@NonNull Activity activity, @NonNull Bundle bundle) {
            Log.i(TAG, "onActivitySaveInstanceState: " + activity);
        }

        @Override
        public void onActivityDestroyed(@NonNull Activity activity) {
            Log.i(TAG, "onActivityDestroyed: " + activity);
        }
    };

    public boolean isEqual(Activity a, Class b) {
        return (a.getClass().getName().equals(b.getName()));
    }

    public boolean isBackground() {
        return state == State.Background;
    }

    public boolean isForeground() {
        return state == State.Foreground;
    }

    public void addListener(Listener listener) {
        this.stateListener = listener;
    }

    public void removeListsner() {
        stateListener = null;
    }

}
