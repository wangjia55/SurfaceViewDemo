
package com.jacob.surface;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.Rect;
import android.graphics.RectF;
import android.util.AttributeSet;
import android.util.Log;
import android.view.SurfaceHolder;
import android.view.SurfaceView;

/**
 * Created by jacob-wj on 2015/3/8.
 */
public class LuckyPanView extends SurfaceView implements SurfaceHolder.Callback, Runnable {
    public static final String TAG = "LuckyPanView";
    private SurfaceHolder mSurfaceHolder;
    private Canvas mCanvas;

    /**
     * 绘制的线程
     */
    private Thread mThread;

    private boolean isRunning;

    private String[] mStrs = new String[]{"单反相机", "IPAD", "恭喜发财", "IPHONE",
            "妹子一只", "恭喜发财"};
    /**
     * 每个盘块的颜色
     */
    private int[] mColors = new int[]{0xFFFFC300, 0xFFF17E01, 0xFFFFC300,
            0xFFF17E01, 0xFFFFC300, 0xFFF17E01};
    /**
     * 与文字对应的图片
     */
    private int[] mImgs = new int[]{R.drawable.danfan, R.drawable.ipad,
            R.drawable.f040, R.drawable.iphone, R.drawable.meizi,
            R.drawable.f040};


    private int width;

    private int center;

    private Bitmap mBitmapBg;

    private Paint mTextPaint;
    private Paint mArcPaint;
    private float startAngle;

    private int speed = 10;


    /**
     * 与文字对应图片的bitmap数组
     */
    private Bitmap[] mImgsBitmap;
    /**
     * 盘块的个数
     */
    private int mItemCount = 6;

    /**
     * 绘制盘块的范围
     */

    private int padding = 15;
    private RectF mRange = new RectF();
    private boolean isShouldEnd = false;

    public LuckyPanView(Context context) {
        this(context, null);
    }

    public LuckyPanView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public LuckyPanView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        mSurfaceHolder = getHolder();
        mSurfaceHolder.addCallback(this);

        setFocusable(true);
        setFocusableInTouchMode(true);
        setKeepScreenOn(true);

        mBitmapBg = BitmapFactory.decodeResource(getResources(), R.drawable.bg2);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        //将布局大小设置成正方形
        width = Math.min(getMeasuredWidth(), getMeasuredHeight());
        center = width / 2;
        setMeasuredDimension(width, width);
    }

    @Override
    public void surfaceCreated(SurfaceHolder holder) {
        Log.e(TAG, "surfaceCreated");
        mTextPaint = new Paint();
        mTextPaint.setAntiAlias(true);
        mTextPaint.setDither(true);
        mTextPaint.setTextSize(35);
        mTextPaint.setColor(Color.BLACK);


        mArcPaint = new Paint();
        mArcPaint.setAntiAlias(true);
        mArcPaint.setDither(true);
        mArcPaint.setStrokeWidth(10);
        mArcPaint.setColor(Color.GREEN);
        mImgsBitmap = new Bitmap[mItemCount];
        for (int i = 0; i < mImgs.length; i++) {
            mImgsBitmap[i] = BitmapFactory.decodeResource(getResources(), mImgs[i]);
        }

        mRange = new RectF(padding * 2, padding * 2, width - padding * 2, width - padding * 2);
        isRunning = true;
        mThread = new Thread(this);
        mThread.start();
    }

    @Override
    public void surfaceChanged(SurfaceHolder holder, int format, int width, int height) {
        Log.e(TAG, "surfaceChanged");
    }

    @Override
    public void surfaceDestroyed(SurfaceHolder holder) {
        Log.e(TAG, "surfaceDestroyed");
        isRunning = false;
    }

    @Override
    public void run() {
        while (isRunning) {
            draw();
        }
//        draw();
    }

    private void draw() {
        try {
            mCanvas = mSurfaceHolder.lockCanvas();
            if (mCanvas != null) {
                drawBg();
                drawArc();
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (mCanvas != null) {
                mSurfaceHolder.unlockCanvasAndPost(mCanvas);
            }
        }
    }

    //绘制弧形的背景
    private void drawArc() {
        float per = 360 / mItemCount;
        for (int i = 0; i < mColors.length; i++) {
            float tmpAngle = startAngle;
            mArcPaint.setColor(mColors[i]);
            mCanvas.drawArc(mRange, tmpAngle, per, true, mArcPaint);
            //绘制文字，使用drawtextOnPath
            drawText(tmpAngle, per, i);
            // 确定绘制图片的位置
            drawIcon(tmpAngle, i);
            // 如果mSpeed不等于0，则相当于在滚动
            startAngle = startAngle + per;
        }
        startAngle += speed;
    }

    private void drawText(float tmpAngle, float swipe, int i) {
        String string = mStrs[i];
        float textWidth = mTextPaint.measureText(string);
        // 利用水平偏移让文字居中
        float hOffset = (float) ((width / 2 - textWidth) / 2);// 水平偏移
        Path path = new Path();
        path.addArc(mRange, tmpAngle, swipe);
        mCanvas.drawTextOnPath(mStrs[i], path, hOffset, width / 12, mTextPaint);
    }

    private void drawIcon(float per, int i) {
        int imgWidth = width / 8;
        float angle = (float) ((30 + per) * (Math.PI / 180));

        int x = (int) (center + width / 2 / 2 * Math.cos(angle));
        int y = (int) (center + width / 2 / 2 * Math.sin(angle));
        Rect rect = new Rect(x - imgWidth / 2, y - imgWidth / 2, x + imgWidth
                / 2, y + imgWidth / 2);
        mCanvas.drawBitmap(mImgsBitmap[i], null, rect, null);

    }

    //绘制圆盘的背景
    private void drawBg() {
        mCanvas.drawColor(0xFFFFFFFF);
        mCanvas.drawBitmap(mBitmapBg, padding, padding, null);
    }

    public void stopRotate() {
        startAngle = 0;
        isShouldEnd = true;
    }


    public void startRotate() {

    }
}
