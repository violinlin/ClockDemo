package com.example.administrator.clockdemo;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.graphics.RectF;
import android.text.format.Time;
import android.util.AttributeSet;
import android.view.View;

/**
 * @author wanghuilin
 * @time 2016/6/23  10:15
 */
public class ClockView extends View {
    private Paint mPaint;//画笔工具
    private RectF mBounds;//绘制钟表的边界
    private float width;//钟表视图的宽
    private float height;//钟表视图的高
    private float radius;//表盘半径
    private float smallLength;//表盘指示短长度
    private float largeLength;//表盘指示长长度
    private int mBorderColor;//表盘颜色
    private int clockBackColor;//表背景颜色
    private int padding;//表盘和视图间的间隔
    private int senWidth;//秒针的宽度
    private int minWidth;//分针的宽度
    private int hourWidth;//时针的宽度
    private int textSize = 30;//文本字体大小
    private boolean isDrawRecr;//是否绘制边界

    public ClockView(Context context) {
        this(context, null);
    }

    public ClockView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context, attrs);

    }

    /**
     * 初始化自定义视图
     *
     * @param context
     * @param attars
     */
    private void init(Context context, AttributeSet attars) {
        mPaint = new Paint();
        mPaint.setAntiAlias(true);
        mPaint.setColor(mBorderColor);
        time = new Time();
        //获取自定义视图的属性
        TypedArray array = context.obtainStyledAttributes(attars, R.styleable.clock_view);
        //获取自定义的属性值，并为其设置默认值
        mBorderColor = array.getColor
                (R.styleable.clock_view_clock_border_color, Color.parseColor("#ffffff"));
        senWidth = (int) array.getDimension(R.styleable.clock_view_clock_border_width, 6);
        minWidth = (int) (senWidth * 1.5);
        hourWidth = (int) (minWidth * 1.5);
        padding = (int) array.getDimension(R.styleable.clock_view_clock_border_padding, 10);
        isDrawRecr = array.getBoolean(R.styleable.clock_view_clock_drawRect, false);
        clockBackColor = array.getColor(R.styleable.clock_view_clock_back_color, Color.parseColor("#281f1f"));
        array.recycle();//回收TypedArray ，一遍后面的资源使用

    }

    /**
     * 屏幕旋转是重新计算钟表尺寸
     *
     * @param w
     * @param h
     * @param oldw
     * @param oldh
     */
    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);
        mBounds = new RectF(0, 0, w, h);
        width = w;
        height = h;
        if (width < height) {
            radius = width / 2 - padding;
        } else {
            radius = height / 2 - padding;
        }
        smallLength = 10;
        largeLength = 20;
    }


    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        canvas.drawColor(clockBackColor);
        time.setToNow();
        mPaint.setStyle(Paint.Style.STROKE);
        mPaint.setStrokeWidth(senWidth);
        mPaint.setColor(mBorderColor);
        canvas.drawCircle(mBounds.centerX(), mBounds.centerY(), radius, mPaint);//绘制表盘
        //是否绘制边界矩形
        if (isDrawRecr) {
            canvas.drawRect(mBounds, mPaint);
        }
        float start_x, start_y;
        float end_x, end_y;


        //绘制表盘刻度
        for (int i = 0; i < 60; ++i) {
            start_x = radius * (float) Math.cos(Math.PI / 180 * i * 6);
            start_y = radius * (float) Math.sin(Math.PI / 180 * i * 6);
            if (i % 5 == 0) {
                end_x = start_x - largeLength * (float) Math.cos(Math.PI / 180 * i * 6);
                end_y = start_y - largeLength * (float) Math.sin(Math.PI / 180 * i * 6);
                drawTextView(canvas, i);
            } else {
                end_x = start_x - smallLength * (float) Math.cos(Math.PI / 180 * i * 6);
                end_y = start_y - smallLength * (float) Math.sin(Math.PI / 180 * i * 6);
            }
            start_x += mBounds.centerX();
            end_x += mBounds.centerX();
            start_y += mBounds.centerY();
            end_y += mBounds.centerY();
            mPaint.setStrokeWidth(senWidth);
            canvas.drawLine(start_x, start_y, end_x, end_y, mPaint);

        }
        mPaint.setStrokeWidth(senWidth);
        mPaint.setStyle(Paint.Style.FILL);
        canvas.drawCircle(mBounds.centerX(), mBounds.centerY(), 10, mPaint);//绘制表盘中心点
        runTime(canvas);
        postInvalidateDelayed(1000);//每隔1s重新绘制，刷新界面
    }

    /**
     * 绘制表盘刻度文本
     *
     * @param canvas
     */
    private void drawTextView(Canvas canvas, int i) {
        String textTime;
        float textTranslate = textSize / 2;
        float text_x;
        float text_y;
        //钟表左右边刻度
        if (Math.sin(Math.PI / 180 * i * 6) > 0) {

            text_x = (float) (mBounds.centerX() + (radius * 0.9 - textTranslate) * (float) Math.sin(Math.PI / 180 * i * 6));
        } else if (Math.sin(Math.PI / 180 * i * 6) < 0) {
            text_x = (float) (mBounds.centerX() + (radius * 0.9 + textTranslate) * (float) Math.sin(Math.PI / 180 * i * 6));

        } else {
            text_x = (float) (mBounds.centerX() - textTranslate);

        }

        //钟表上下刻度
        if (Math.cos(Math.PI / 180 * 6 * i) > 0) {
            text_y = (float) (mBounds.centerY() + -(radius * 0.9 - textTranslate) * (float) Math.cos(Math.PI / 180 * i * 6));

        } else if (Math.cos(Math.PI / 180 * 6 * i) < 0) {
            text_y = (float) (mBounds.centerY() + -(radius * 0.9 + textTranslate) * (float) Math.cos(Math.PI / 180 * i * 6));

        } else {
            text_y = (float) (mBounds.centerY() - textTranslate);

        }
        mPaint.setTextSize(textSize);
        mPaint.setStrokeWidth(2);
        textTime = i == 0 ? "12" : i / 5 + "";
        canvas.drawText(textTime, text_x, text_y, mPaint);
    }

    /**
     * 绘制时间指针
     *
     * @param canvas
     */
    private void runTime(Canvas canvas) {
        drawHourLine(canvas);
        drawMinline(canvas);
        drawSecLine(canvas);
    }

    /**
     * 绘制秒针
     *
     * @param canvas
     */
    private int second = 0;

    private void drawSecLine(Canvas canvas) {
        second = getCurrentSec();
        int angle = second * 6;
        float endX = (float) (mBounds.centerX() + (radius / 1.2) * (float) Math.sin(Math.PI / 180 * angle));
        float endY = (float) (mBounds.centerY() + -(radius / 1.2) * (float) Math.cos(Math.PI / 180 * angle));
        float startX= (mBounds.centerX() - 50 * (float) Math.sin(Math.PI / 180 * angle));
        float startY=  (mBounds.centerY() + 50 * (float) Math.cos(Math.PI / 180 * angle));
        mPaint.setStrokeWidth(senWidth);
        canvas.drawLine(startX,startY, endX, endY, mPaint);
    }

    /**
     * 绘制分针
     *
     * @param canvas
     */
    private int minutes;

    private void drawMinline(Canvas canvas) {
        minutes = getCurrentMin();
        float endX = (float) (mBounds.centerX() + (radius / 1.5) * (float) Math.sin(Math.PI / 180 * minutes * 6));
        float endY = (float) (mBounds.centerY() + -(radius / 1.5) * (float) Math.cos(Math.PI / 180 * minutes * 6));
        mPaint.setStrokeWidth(minWidth);
        canvas.drawLine(mBounds.centerX(), mBounds.centerY(), endX, endY, mPaint);
    }

    private int hour;

    private void drawHourLine(Canvas canvas) {
        hour = getCurrentHour() % 12;
        float endX = mBounds.centerX() + radius / 2 * (float) Math.sin(Math.PI / 180 * hour * 30);
        float endY = mBounds.centerY() + -radius / 2 * (float) Math.cos(Math.PI / 180 * hour * 30);
        mPaint.setStrokeWidth(hourWidth);
        canvas.drawLine(mBounds.centerX(), mBounds.centerY(), endX, endY, mPaint);
    }

    /**
     * 获取当前的时间s
     *
     * @return
     */
    private Time time;

    private int getCurrentSec() {
        return time.second;
    }

    /**
     * 获取当前的时间m
     *
     * @return
     */
    private int getCurrentMin() {
        return time.minute;
    }

    private int getCurrentHour() {
        return time.hour;
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
    }

    @Override
    protected void onLayout(boolean changed, int left, int top, int right, int bottom) {
        super.onLayout(changed, left, top, right, bottom);
    }
}
