package com.jacob.surface;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.util.AttributeSet;
import android.util.Log;
import android.view.SurfaceHolder;
import android.view.SurfaceView;

/**
 * Created by jacob-wj on 2015/3/8.
 */
public class SimpleSurfaceView extends SurfaceView implements SurfaceHolder.Callback, Runnable {
    public static final String TAG = "SimpleSurfaceView";
    private Thread thread;
    private SurfaceHolder surfaceHolder;
    private Canvas canvas;
    private boolean isRunning = false;
    private int count = 0;

    public SimpleSurfaceView(Context context) {
        this(context, null);
    }

    public SimpleSurfaceView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public SimpleSurfaceView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        thread = new Thread(this);
        surfaceHolder = getHolder();
        surfaceHolder.addCallback(this);
        setKeepScreenOn(true);
    }


    @Override
    public void surfaceCreated(SurfaceHolder holder) {
        Log.e(TAG, "surfaceCreated");
        thread.start();
        isRunning = true;
    }

    @Override
    public void surfaceChanged(SurfaceHolder holder, int format, int width, int height) {
        Log.e(TAG, "surfaceChanged");
    }

    @Override
    public void surfaceDestroyed(SurfaceHolder holder) {
        isRunning = false;
        Log.e(TAG, "surfaceDestroyed");
    }

    @Override
    public void run() {
        Log.e(TAG, "run");
        while (isRunning) {
            draw();
        }
    }

    private void draw() {
        Log.e(TAG, "draw");
        try {
            canvas = surfaceHolder.lockCanvas();
            if (canvas != null) {
                canvas.drawColor(Color.GRAY);
                Paint paint = new Paint();
                paint.setAntiAlias(true);
                paint.setColor(Color.BLACK);
                paint.setTextSize(45);
                String text = "计时器：" + (count++);
                canvas.drawText(text, 100, 300, paint);
                Thread.sleep(1000);
            }
        } catch (Exception e) {
            isRunning = false;
        } finally {
            surfaceHolder.unlockCanvasAndPost(canvas);
        }
    }

}
