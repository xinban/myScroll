package com.thinkerx.myscroll;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.util.AttributeSet;
import android.view.SurfaceHolder;
import android.view.SurfaceHolder.Callback;
import android.view.SurfaceView;

/**
 * Created by banXin on 2017/11/24.
 * ＊功能：
 */

public class MSurfaceView extends SurfaceView implements Callback {

    SurfaceHolder surfaceHolder;
    ViewThread    viewThread;
    int ani_process = 0;
    public boolean direction = true;
    int width, height;
    private Bitmap bmp_right;
    private Bitmap bmp_middle;

    private int imgHeight;//
    private int scrollBarWidth;
    int    scrollWidth = 900;//卷轴要绘制的宽度
    Canvas canvas      = null;

    public MSurfaceView(Context context) {
        super(context);
        init();
    }

    public MSurfaceView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public MSurfaceView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        init();
    }

    public void surfaceChanged(SurfaceHolder surfaceHolder, int format, int width, int height) {

    }

    public void surfaceCreated(SurfaceHolder surfaceHolder) {
        width = this.getWidth();
        height = this.getHeight();
        viewThread = new ViewThread();
        viewThread.flag = true;
        viewThread.start();
    }

    private void init() {
        surfaceHolder = this.getHolder();
        surfaceHolder.addCallback(this);
        /**
         * 将150dp转化为px
         *
         */
        imgHeight = ScreenUtil.dp2px(150, getContext());
        scrollWidth = ScreenUtil.getScreenWidth(getContext());
        scrollBarWidth = (int) (scrollWidth * 0.089);
        loadRes();
    }

    public void surfaceDestroyed(SurfaceHolder surfaceHolder) {
        viewThread.flag = false;
    }

    public void loadRes() {
        Bitmap localBitmap;
        Bitmap bitmap = BitmapFactory.decodeResource(getResources(), R.drawable.scroll);
        localBitmap = Bitmap.createScaledBitmap(bitmap, scrollWidth, imgHeight, true);
        bmp_middle = Bitmap.createBitmap(localBitmap, 0, 0, scrollWidth, imgHeight);
        bmp_right = Bitmap.createBitmap(localBitmap, (scrollWidth - scrollBarWidth), 0,
                scrollBarWidth, imgHeight);
    }

    class ViewThread extends Thread {
        public boolean flag;

        public void run() {
            while (flag) {
                try {
                    if (direction) {
                        if (ani_process > scrollWidth - scrollBarWidth) {
                            direction = false;
                        }
                        ani_process += 3;
                    } else {
                        viewThread.flag = false; // 销毁线程
                    }
                    canvas = surfaceHolder.lockCanvas(null); // 锁定画布 并获取canvas
                    myDraw();
                    surfaceHolder.unlockCanvasAndPost(canvas);
                } catch (Exception e) {
                    e.printStackTrace();
                }
                try {
                    Thread.sleep(10);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    private void myDraw() {
        canvas.drawBitmap(bmp_right, ani_process - 6, 0, null);
        if (canvas.clipRect(0, 0, ani_process, scrollWidth))
            canvas.drawBitmap(bmp_middle, 0, 0, null);
    }
}
