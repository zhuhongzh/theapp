package com.example.administrator.lifeapp;

import android.content.Context;
import android.util.AttributeSet;
import android.view.View;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.util.AttributeSet;
import android.view.View;

import java.util.Calendar;

public class MyView extends View {
    //外圆画笔
    private Paint mCirclepaint;
    //数字画笔
    private Paint numberPaint;
    //时针画笔
    private Paint hourPaint;
    //分针画笔
    private Paint minutePaint;
    //秒针画笔
    private Paint secondPaint;
    //圆心坐标
    private float x, y;
    // 外圆半径
    private int radius;

    public MyView(Context context) {
        super(context);
        initPaint();
    }

    public MyView(Context context, AttributeSet attrs) {
        super(context, attrs);
        initPaint();
    }

    public MyView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initPaint();
    }

    private void initPaint() {
        mCirclepaint = new Paint();
        mCirclepaint.setColor(Color.BLACK);
        mCirclepaint.setAntiAlias(true);
        mCirclepaint.setStrokeWidth(8);
        mCirclepaint.setStyle(Paint.Style.STROKE);

        numberPaint = new Paint();
        numberPaint.setColor(Color.BLACK);
        numberPaint.setAntiAlias(true);
        numberPaint.setTextSize(35);
        numberPaint.setStyle(Paint.Style.STROKE);
        numberPaint.setTextAlign(Paint.Align.CENTER);

        hourPaint = new Paint();
        hourPaint.setColor(Color.BLACK);
        hourPaint.setAntiAlias(true);
        hourPaint.setStrokeWidth(15);
        hourPaint.setStyle(Paint.Style.FILL);

        minutePaint = new Paint();
        minutePaint.setColor(Color.RED);
        minutePaint.setAntiAlias(true);
        minutePaint.setStrokeWidth(10);
        minutePaint.setStyle(Paint.Style.FILL);

        secondPaint = new Paint();
        secondPaint.setColor(Color.BLACK);
        secondPaint.setAntiAlias(true);
        secondPaint.setStrokeWidth(5);
        secondPaint.setStyle(Paint.Style.FILL);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        x = getMeasuredWidth() / 2;
        y = getMeasuredHeight() / 2;
        radius = (int) (x - 5);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        //绘制外圆
        canvas.drawCircle(x, y, radius, mCirclepaint);
        //绘制圆心
        canvas.drawCircle(x, y, 15,secondPaint);
        //绘制刻度
        drawLines(canvas);
        //绘制数字
        drawNumbers(canvas);
        //绘制指针
        try {
            initCurrentTime(canvas);
        }catch (Exception e){
            e.printStackTrace();
        }
        postInvalidateDelayed(1000);
    }

    private void drawLines(Canvas canvas) {
        for (int i = 0; i < 60; i++) {
            if (i % 5 == 0) {
                mCirclepaint.setStrokeWidth(3);
                canvas.drawLine(x, y - radius, x, y - radius + 40, mCirclepaint);
            } else {
                mCirclepaint.setStrokeWidth(1);
                canvas.drawLine(x, y - radius, x, y - radius + 30, mCirclepaint);
            }
            canvas.rotate(6, x, y);
        }
    }

    private void drawNumbers(Canvas canvas) {
        // 获取文字高度用于设置文本垂直居中
        float textSize = (numberPaint.getFontMetrics().bottom - numberPaint.getFontMetrics().top);
        // 数字离圆心的距离,40为刻度的长度,20文字大小
        int distance = radius - 40 - 20;
        // 数字的坐标(a,b)
        float a, b;
        for (int i = 0; i < 12; i++) {
            a = (float) (distance * Math.sin(i * 30 * Math.PI / 180) + x);
            b = (float) (y - distance * Math.cos(i * 30 *Math.PI / 180));
            if(i == 0){
                canvas.drawText("12",a,b + textSize / 3,numberPaint);
            }else {
                canvas.drawText(String.valueOf(i),a,b + textSize / 3,numberPaint);
            }
        }
    }
    private void initCurrentTime(Canvas canvas) {
        Calendar calendar = Calendar.getInstance();
        int hour = calendar.get(Calendar.HOUR_OF_DAY);
        int minute = calendar.get(Calendar.MINUTE);
        int second = calendar.get(Calendar.SECOND);
        //时针走过的角度
        int hourAngle = hour * 30 + minute / 2;
        //分针走过的角度
        int minuteAngle = minute * 6 + second / 10;
        //秒针走过的角度
        int secondAngle = second * 6;
        //绘制时钟,以12整点为0°参照点
        canvas.rotate(hourAngle, x, y);
        canvas.drawLine(x, y, x, y - radius + 120, hourPaint);
        canvas.save();
        canvas.restore();
        //这里画好了时钟，我们需要再将画布转回来,继续以12整点为0°参照点
        canvas.rotate(-hourAngle, x, y);

        //绘制分钟
        canvas.rotate(minuteAngle, x, y);
        canvas.drawLine(x, y, x, y - radius + 60, minutePaint);
        canvas.save();
        canvas.restore();
        //这里同上
        canvas.rotate(-minuteAngle, x, y);

        //绘制秒钟
        canvas.rotate(secondAngle, x, y);
        canvas.drawLine(x, y, x, y - radius + 20, secondPaint);
    }
}

