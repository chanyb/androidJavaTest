package com.example.smartbox_dup.screen.function.adbshell;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;


public class DrawingView extends View {
    private Paint paint;
    float mx,x,my,y;
    Bitmap bitmap;
    Paint bitmapPaint;
    Canvas mCanvas;
    public DrawingView(Context context) {
        super(context);
        init();
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        canvas.drawBitmap(bitmap, 0, 0, bitmapPaint);
    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);
        bitmap = Bitmap.createBitmap(w,h,Bitmap.Config.ARGB_8888);
        mCanvas = new Canvas(bitmap);
    }

    private void init() {
        bitmapPaint = new Paint(Paint.DITHER_FLAG);
        paint = new Paint();
        paint.setStrokeWidth(20f);
        paint.setStyle(Paint.Style.FILL);
        paint.setColor(Color.BLACK);


        Log.i("this", "init()");
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        Log.i("this", "onTouchEvent");

        if(event.getAction() == MotionEvent.ACTION_DOWN) {
            mx = event.getX();
            my = event.getY();
            return true;
        }

        if(event.getAction() == MotionEvent.ACTION_UP) {
            return true;
        }

        x = event.getX();
        y = event.getY();

        mCanvas.drawLine(mx, my, x, y, paint);
        mCanvas.drawCircle(x,y,paint.getStrokeWidth()/2, paint);
        mx = x;
        my = y;
        invalidate();
        return true;
    }

    public void getPhoneSize() {
    }

}
