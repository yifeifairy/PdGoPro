package com.emt.pdgo.next.ui.view;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;

import androidx.annotation.Nullable;

public class Breathe extends View {
    private static final String TAG = "OkView";
    private static final int DEFAULT_DELAY = 15; // 默认每帧播放速度
    private static final int DEFAULT_RADIUS = 20;  // 默认半径
    private static final int DEFAULT_STROKE_WIDTH = 3;  // 默认外圈画笔宽度

    private float mInnerCx;
    private float mInnerCy;
    private float mOuterCx;
    private float mOuterCy;
    private int mWidth;
    private int mInitOuterRadius;
    private Paint mInnerPaint;
    private Paint mOuterPaint;

    private int mInnerAlpha = 0;
    private int mOuterAlpha = 0;
    private float mOuterRadius;
    private float mInnerRadius;
    private boolean isPlay = false;

    private float mOuterStrokeWidth = DEFAULT_STROKE_WIDTH;   // 外圈画笔大小

    private int mInitInnerRadius = DEFAULT_RADIUS;  // 默认的半径大小
    private int mDelay = DEFAULT_DELAY;


    public Breathe(Context context) {
        this(context, null);
    }


    public Breathe(Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public Breathe(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);

    }
    /**
     * 测量view
     * @param widthMeasureSpec .
     * @param heightMeasureSpec .
     */
    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        // 获取大小
        int widthSize = MeasureSpec.getSize(widthMeasureSpec); // 获取实际宽
        int heightSize = MeasureSpec.getSize(heightMeasureSpec); // 获取实际高
        // 减去padding
        int widthTargetSize = widthSize - getPaddingLeft() - getPaddingRight();
        int heightTargetSize = heightSize - getPaddingTop() - getPaddingBottom();
        setMeasuredDimension(widthTargetSize, heightTargetSize);

        // 因为需要放大处理，所以控件实际大小为设置大小的1/2倍
        mWidth = widthTargetSize;
        mInitInnerRadius = mWidth / 4;  // 初始圆的半径大小
        mInitOuterRadius = (int) ((mWidth / 2 + (mOuterStrokeWidth / 2)) / 2);  // 初始外圆的半径大小, 1.5是画笔宽度的一半
        mInnerRadius = mInitInnerRadius;  // 内圆搬进大小
        mOuterRadius = mInitOuterRadius;  // 外圆半径大小
        mInnerCx = (float) mWidth / 2;  // 内圆心
        mInnerCy = (float) mWidth / 2;// 内圆心

        mOuterCx = (float) mWidth / 2;  // 外圆心
        mOuterCy = (float) mWidth / 2;// 外圆心
        // 初始化画笔
        initPaints();
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        canvas.save();
        mInnerPaint.setAlpha(mInnerAlpha);  // 画笔透明度
        // 四个参数：X轴圆心，Y轴圆心，圆的半径， 画笔
        canvas.drawCircle(mInnerCx, mInnerCy, mInnerRadius, mInnerPaint);
        canvas.restore();

        canvas.save();
        mOuterPaint.setAlpha(mOuterAlpha);
        canvas.drawCircle(mOuterCx, mOuterCy, mOuterRadius, mOuterPaint);
        canvas.restore();
    }


    private void initPaints() {
        // 内圆画笔
        mInnerPaint = new Paint();
        mInnerPaint.setColor(Color.RED);  // 画笔颜色
        mInnerPaint.setStyle(Paint.Style.FILL);  // 内部填充
        mInnerPaint.setAntiAlias(true);

        // 外圈画笔
        mOuterPaint = new Paint();
        mOuterPaint.setColor(Color.RED);
        mOuterPaint.setStrokeWidth(mOuterStrokeWidth);  // 画笔宽度
        mOuterPaint.setStyle(Paint.Style.STROKE);  // 描边：在描边状态下，圆的实际半径为 设置的半径 + 画笔宽度的一半,有兴趣可以自行实验
        mOuterPaint.setAntiAlias(true);

    }

    public void show() {
        Log.d(TAG, "show: ..............................");
        isPlay = true;
        // 显示并播放动画
        post(mRunnable);
    }

    private final Runnable mRunnable = new Runnable() {
        @Override
        public void run() {
            if (isPlay && Breathe.this.getAlpha() != 0) {
                play();
                invalidate(); // 调用此方法最终会调用到onDraw()方法
                postDelayed(mRunnable, mDelay);
            } else {
                removeCallbacks(this);
            }
        }
    };

    private int currentFrame = 1;

    private void play() {
        switch (currentFrame) {
            case 1:
                // 第一帧
                boolean isInnerShow = innerCircleShow();
                boolean isOuterShow = outerCircleShow();
                if (isInnerShow && isOuterShow) {
                    currentFrame++;
                }
                break;
            case 2:
                // 第二帧
                boolean isInnerHide = innerCircleHide();
                boolean isOuterHide = outerCircleHide();
                boolean isOuterEnlarge = outerCircleEnlarge();
                if (isInnerHide && isOuterHide && isOuterEnlarge) {
                    mOuterRadius = mInitOuterRadius; // 恢复原本大小
                    resumeInit();
                    currentFrame = 1;
                }
                break;
        }
    }

    /**
     * 内圈圆显示  每帧透明度加15.3，15帧之后透明度到达90%,返回true
     */
    private boolean innerCircleShow() {
        mInnerAlpha += 15.3;
        if (mInnerAlpha > 255 * 0.9f) {
            mInnerAlpha = (int) (255 * 0.9f);
            return true;
        }
        return false;
    }

    /**
     * 内圈圆隐藏   每帧透明度减少15.3，15帧之后透明度到达90%,返回true
     */
    private boolean innerCircleHide() {
        mInnerAlpha -= 15.3;
        if (mInnerAlpha < 0) {
            mInnerAlpha = 0;
            return true;
        }
        return false;
    }

    /**
     * 外圈圆显示  每帧透明度增加17，15帧之后透明度到达100%,返回true
     */
    private boolean outerCircleShow() {
        mOuterAlpha += 17;
        if (mOuterAlpha > 255) {
            mOuterAlpha = 255;
            return true;
        }
        return false;
    }

    /**
     * 外圈圆隐藏   每帧透明度减少17，15帧之后透明度到达90%,返回true
     */
    private boolean outerCircleHide() {
        mOuterAlpha -= 17;
        if (mOuterAlpha < 0) {
            mOuterAlpha = 0;
            return true;
        }
        return false;
    }

    /**
     * 外圈放大  每次放大15分之一， 5帧之后达到原本大小的130%，返回true
     */
    private boolean outerCircleEnlarge() {
        if (mOuterRadius <= (mInitOuterRadius * 1.5)) {
            mOuterRadius += mInitOuterRadius / 30f;
            return false;
        } else {
            return true;
        }
    }

    public void hide() {
        Log.d(TAG, "hide: .......................");
        // 隐藏并停止动画,恢复默认值
        isPlay = false;
        currentFrame = 1;
        removeCallbacks(mRunnable);
        resumeInit();

    }

    /**
     * 恢复初始状态
     */
    private void resumeInit() {
        mInnerAlpha = 0;
        mOuterRadius = mInitInnerRadius;
        mInnerRadius = mInitOuterRadius;
        mOuterAlpha = 0;
        invalidate();
    }

    /**
     * 设置外圈画笔大小
     *
     * @param outerStrokeWidth 。
     */
    public void setOuterStrokeWidth(float outerStrokeWidth) {
        mOuterStrokeWidth = outerStrokeWidth;
        mInitOuterRadius = (int) ((mWidth / 2 + (mOuterStrokeWidth / 2)) / 2);  // 初始外圆的半径大小, 1.5是画笔宽度的一半
    }

    /**
     * 设置时长
     *
     * @param delay .
     */
    public void setDelay(int delay) {
        mDelay = delay;
    }
}

