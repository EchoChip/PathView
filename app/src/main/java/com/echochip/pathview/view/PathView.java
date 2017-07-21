package com.echochip.pathview.view;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.RectF;
import android.util.AttributeSet;
import android.util.Log;
import android.view.SurfaceHolder;
import android.view.SurfaceView;


public class PathView extends SurfaceView implements SurfaceHolder.Callback, Runnable {
    private static final String TAG = "PathView";
    Boolean isRunning;
    Paint paint;
    SurfaceHolder holder;
    int startAngle = 0;
    int sweepAngle = 240;
    int line1 = 3, line2 = 3;
    boolean flag;
    int RectWidth = 100;
    int RectHeight = 100;
    State state;

    public PathView(Context context) {
        super(context);
    }

    public PathView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public PathView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    public void setState(State state) {
        this.state = state;
        init();
    }

    public enum State {
        Process,
        Fail,
        Success
    }

    public void init() {

        holder = getHolder();
        holder.addCallback(this);

        paint = new Paint();
        paint.setAntiAlias(true);
        paint.setStyle(Paint.Style.STROKE);
        paint.setStrokeWidth(5);
        paint.setStrokeCap(Paint.Cap.ROUND);
        paint.setStrokeJoin(Paint.Join.ROUND);

        line2 = line1 = 3;

        switch (state) {
            case Process:
                paint.setColor(Color.GRAY);
                break;
            case Fail:
                paint.setColor(Color.RED);
                break;
            case Success:
                paint.setColor(Color.GREEN);
                break;
        }
        isRunning = true;
        new Thread(this).start();
    }

    @Override
    public void surfaceChanged(SurfaceHolder holder, int format, int width, int height) {
        Log.i(TAG, "surfaceChanged: ");
    }

    @Override
    public void surfaceCreated(SurfaceHolder holder) {
        Log.i(TAG, "surfaceCreated: ");
    }

    @Override
    public void surfaceDestroyed(SurfaceHolder holder) {
        Log.i(TAG, "surfaceDestroyed: ");
        isRunning = false;
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);

        Log.i(TAG, "width: " + getWidth());
        Log.i(TAG, "height: " + getHeight());
    }

    @Override
    public void run() {
        Canvas c;
        while (isRunning) {

            synchronized (holder) {

                c = holder.lockCanvas();
                if (c != null) {
                    doDraw(c);
                }

                try {
                    Thread.sleep(30);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                } finally {
                    if (c != null) {
                        holder.unlockCanvasAndPost(c);
                    }
                }
            }

        }
    }

    private void doDraw(Canvas canvas) {

        canvas.drawColor(Color.WHITE);

        int x = (getWidth() - RectWidth) / 2;
        int y = (getHeight() - RectHeight) / 2;

        RectF rectArc = new RectF(x, y, getWidth() - x, getHeight() - y);

        switch (state) {
            case Process:

                if (sweepAngle > 300) {
                    flag = true;
                } else if (sweepAngle < 30) {
                    flag = false;
                }

                if (flag) {
                    startAngle += 20;
                    sweepAngle -= 10;
                } else {
                    startAngle += 5;
                    sweepAngle += 5;
                }
                canvas.drawArc(rectArc, startAngle, sweepAngle, false, paint);
                break;
            case Fail:
                if (sweepAngle < 360) {
                    sweepAngle += 15;
                } else {
                    sweepAngle = 360;
                }
                canvas.drawArc(rectArc, startAngle, sweepAngle, false, paint);

                if (sweepAngle == 360) {
                    doDrawFail(canvas);
                }

                break;
            case Success:
                if (sweepAngle < 360) {
                    sweepAngle += 15;
                } else {
                    sweepAngle = 360;
                }
                canvas.drawArc(rectArc, startAngle, sweepAngle, false, paint);

                if (sweepAngle == 360) {
                    doDrawSuccess(canvas);
                }

                break;
        }

    }

    private void doDrawFail(Canvas canvas) {

        int a = (getWidth() - RectWidth / 3) / 2;
        int b = (getHeight() - RectHeight / 3) / 2;
        int c = (getWidth() + RectHeight / 3) / 2;
        int d = (getHeight() + RectHeight / 3) / 2;


        Path path = new Path();
        path.moveTo(a, b);
        if (a + line1 < c || b + line1 < d) {
            path.lineTo(a + line1, b + line1);
            line1 += 3;
        } else {
            path.lineTo(c, d);
            path.moveTo(c, b);
            if (a + line2 < c || b + line2 < d) {
                path.lineTo(c - line2, b + line2);
                line2 += 3;
            } else {
                path.lineTo(a, d);
            }
        }

        canvas.drawPath(path, paint);
    }

    private void doDrawSuccess(Canvas canvas) {

        int a = (getWidth() - RectWidth / 2) / 2;
        int b = getHeight() / 2;
        int c = (getWidth() - RectWidth / 6) / 2;
        int d = (getHeight() + RectHeight / 3) / 2;
        int e = (getWidth() + RectWidth / 2) / 2;
        int f = (getHeight() - RectHeight / 3) / 2;

        Path path = new Path();
        path.moveTo(a, b);
        if (a + line1 < c || b + line1 < d) {
            path.lineTo(a + line1, b + line1);
            line1 += 3;
        } else {
            path.lineTo(c, d);
            if (c + line2 < e || f + line2 < d) {
                path.lineTo(c + line2, d - line2);
                line2 += 3;
            } else {
                path.lineTo(e, f);
            }
        }

        canvas.drawPath(path, paint);
    }

}
