package com.vlabs.progresswidget;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.RectF;
import android.support.annotation.NonNull;
import android.util.AttributeSet;
import android.view.View;

public class ProgressWidget extends View {
    private static final float DEFAULT_WIDTH = 10f;
    private static final int DEFAULT_COLOR = 0x00ff00;
    public static final float START_ANGLE = 270.f;
    public static final float FULL_CIRCLE_SWEEP = 360.f;

    private Paint mPaint;
    private RectF mDrawRect = new RectF();
    private float mProgress;

    public ProgressWidget(Context context, AttributeSet attrs) {
        super(context, attrs);

        TypedArray a = context.getTheme().obtainStyledAttributes(
                attrs,
                R.styleable.ProgressWidget,
                0, 0);

        mPaint = new Paint();
        mPaint.setTextAlign(Paint.Align.CENTER);
        mPaint.setAntiAlias(true);
        mPaint.setDither(true);
        mPaint.setStyle(Paint.Style.STROKE);
        mPaint.setStrokeJoin(Paint.Join.ROUND);
        mPaint.setStrokeCap(Paint.Cap.ROUND);

        try {
            final float strokeWidth = a.getDimension(R.styleable.ProgressWidget_stroke_width, DEFAULT_WIDTH);
            mPaint.setStrokeWidth(strokeWidth);

            final int strokeColor = a.getColor(R.styleable.ProgressWidget_stroke_color, DEFAULT_COLOR);
            mPaint.setColor(strokeColor);
        } finally {
            a.recycle();
        }

    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);

        float paintWidth = mPaint.getStrokeWidth() / 2;
        mDrawRect.set(getLeft() + paintWidth, getTop() + paintWidth, getRight() - paintWidth, getBottom() - paintWidth);

        canvas.drawArc(
                mDrawRect,
                startAngle(),
                sweepAngle(),
                useCenter(),
                paint());
    }

    @NonNull
    private Paint paint() {
        return mPaint;
    }

    private boolean useCenter() {
        return false;
    }

    private float sweepAngle() {
        return FULL_CIRCLE_SWEEP * mProgress / 100;
    }

    private float startAngle() {
        return START_ANGLE;
    }

    public float progress() {
        return mProgress;
    }

    public void setProgress(float progress) {
        mProgress = progress;
        invalidate();
    }

}