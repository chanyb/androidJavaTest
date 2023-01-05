package com.example.smartbox_dup.screen.function.adbshell;

import android.content.Context;
import android.graphics.Color;
import android.graphics.Paint;
import android.view.MotionEvent;
import android.view.View;

public class DrawingView extends View {
    public static final int MODE_PEN = 1;
    public static final int MODE_ERASE = 0;
    private int penSize;
    private int eraseSize;

    private Paint paint;
    private int penColor;

    public DrawingView(Context context) {
        super(context);
        init();
    }

    private void init() {
        paint = new Paint();
        penSize = 3;
        eraseSize = 30;
        penColor = Color.BLACK;
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        int action = event.getAction();
        int state = action == MotionEvent.ACTION_DOWN ? 1:2;
        return true;
    }
}
