package com.example.chenmin.alldemo.reddot;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.RectF;
import android.support.annotation.Nullable;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.util.DisplayMetrics;
import android.view.View;


/**
 * <pre>
 *     author : mathewchen
 *     e-mail : 136214454@qq.com
 *     time   : 2017/10/19
 *     desc   : 小红点
 *     version: 1.0
 * </pre>
 *
 * @author mathewchen
 */

public class RedPointView extends View {

    /**
     * 一些默认参数
     */
    private static final int DEFAULT_COLOR = 0xffe25360;
    private static final int DEFAULT_MAX_WORD_LENGTH = 2;
    private static final int DEFAULT_TEXT_COLOR = Color.WHITE;

    private String mShowText = "";

    /**
     * 圆圈颜色
     */
    private int mColor = DEFAULT_COLOR;

    /**
     * 字体大小
     */
    private float mTextSize = 0;

    /**
     * 文字颜色
     */
    private int mTextColor = DEFAULT_TEXT_COLOR;

    /**
     * 超过多少显示圆角矩形
     */
    private int mShowTextLength = DEFAULT_MAX_WORD_LENGTH;

    private Paint mPaint;

    private RectF mRectF;

    public RedPointView(Context context) {
        super(context);
    }

    public RedPointView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
    }

    public RedPointView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);

        if (mPaint == null) {
            mPaint = new Paint();
            mPaint.setAntiAlias(true);
        }
        mPaint.setColor(mColor);
        float radius;
        float cx;
        float cy;
        //如果文字为空，就显示红点，显示在控件左侧
        if (TextUtils.isEmpty(mShowText)) {
            cy = canvas.getHeight() >> 1;
            cx = canvas.getWidth() >> 1;
            //取最短边作为红点的直径
            radius = cx > cy ? cy : cx;
            //如果不想左侧则改变前两个参数
            canvas.drawCircle(radius, cy, radius, mPaint);
        } else {

            if (mShowText.length() <= mShowTextLength) {
                cy = canvas.getHeight() >> 1;
                cx = canvas.getWidth() >> 1;
                radius = cx > cy ? cy : cx;
                canvas.drawCircle(radius, cy, radius, mPaint);

                mPaint.setColor(getTextColor());
                mPaint.setTextSize(getTextSize());
                mPaint.setTextAlign(Paint.Align.CENTER);
                Paint.FontMetricsInt fontMetrics = mPaint.getFontMetricsInt();
                float baseLine = cy + ((fontMetrics.bottom - fontMetrics.top) >> 1) - fontMetrics.bottom;
                //drawText中的Y是baseLine
                canvas.drawText(mShowText, radius, baseLine, mPaint);
            } else {
                //画个圆角矩形，不想画成半圆修改此处
                cy = canvas.getHeight() >> 1;
                cx = cy;
                //如果宽高改变了则改变红点绘制的矩形
                if (mRectF == null || mRectF.right != canvas.getWidth() || mRectF.bottom != canvas.getHeight()) {
                    mRectF = new RectF(0, 0, canvas.getWidth(), canvas.getHeight());
                }
                canvas.drawRoundRect(mRectF, cx, cy, mPaint);


                mPaint.setColor(getTextColor());
                mPaint.setTextSize(getTextSize());
                mPaint.setTextAlign(Paint.Align.CENTER);
                Paint.FontMetricsInt fontMetrics = mPaint.getFontMetricsInt();
                //不懂就搜一下baseLine算法
                float baseLine = cy + ((fontMetrics.bottom - fontMetrics.top) >> 1) - fontMetrics.bottom;

                canvas.drawText(mShowText, canvas.getWidth() >> 1, baseLine, mPaint);
            }
        }

    }

    /**
     * @param showText 显示的文字
     */
    public void show(String showText) {
        this.mShowText = showText;
        setVisibility(VISIBLE);
        invalidate();
    }

    /**
     * 消失
     */
    public void dismiss() {
        setVisibility(INVISIBLE);
    }


    /**
     * @return 圆圈颜色
     */
    public int getColor() {
        return mColor;
    }

    /**
     * @param color 圆圈颜色
     */
    public void setColor(int color) {
        this.mColor = color;
        invalidate();
    }

    private int getTextColor() {
        return mTextColor;
    }

    public void setTextColor(int textColor) {
        this.mTextColor = textColor;
        invalidate();
    }

    public void setTextSize(float size) {
        mTextSize = size;
    }

    public void setTextMaxLength(int textMaxLength) {
        this.mShowTextLength = textMaxLength;
    }

    /**
     * 根据屏幕分辨率确定字体大小
     *
     * @return 大小
     */
    public float getTextSize() {
        if (mTextSize > 0) {
            return mTextSize;
        }
        DisplayMetrics dm = getContext().getResources().getDisplayMetrics();
        int mScreenWidth = dm.widthPixels;
        int mScreenHeight = dm.heightPixels;

        //以分辨率为1080*1920准，计算宽高比值
        float ratioWidth = (float) mScreenWidth / 1080;
        float ratioHeight = (float) mScreenHeight / 1920;
        float ratioMetrics = Math.min(ratioWidth, ratioHeight);
        mTextSize = Math.round(35 * ratioMetrics);
        return mTextSize;
    }
}
