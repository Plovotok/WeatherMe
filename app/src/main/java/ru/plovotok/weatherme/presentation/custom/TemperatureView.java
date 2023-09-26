package ru.plovotok.weatherme.presentation.custom;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.Rect;
import android.graphics.RectF;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;

import ru.plovotok.weatherme.R;


public class TemperatureView extends View {
    private static final String TAG = "TemperatureView";

    private float minValue;
    private float maxValue;
    private float currentValue;
    private float lastValue;
    private float nextValue;
    private Paint mPaint;
    private int viewHeight;
    private int viewWidth;
    private float pointX;
    private float pointY;
    private boolean isDrawLeftLine;
    private boolean isDrawRightLine;
    private int pointTopY = (int) (40 * Util.getDensity(getContext()));
    private int pointBottomY = (int) (100 * Util.getDensity(getContext()));
    private float mMiddleValue;

    public TemperatureView(Context context) {
        super(context);
    }

    public TemperatureView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public TemperatureView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }


    //设置最小值
    public void setMinValue(float minValue){
        this.minValue = minValue;
    }

    //设置最大值
    public void setMaxValue(float maxValue){
        this.maxValue = maxValue;
    }

    //设置目前的值
    public void setCurrentValue(float currentValue){
        this.currentValue = currentValue;
    }

    //设置是否画左边线段(只有第一个View是false)
    public void setDrawLeftLine(boolean isDrawLeftLine){
        this.isDrawLeftLine = isDrawLeftLine;
    }

    //设置是否画右边线段(只有最后一个View是false)
    public void setDrawRightLine(boolean isDrawRightLine){
        this.isDrawRightLine = isDrawRightLine;
    }

    //设置之前温度点的值
    public void setLastValue(float lastValue){
        this.lastValue = lastValue;
    }

    //设置下一个温度点的值
    public void setNextValue(float nextValue){
        this.nextValue = nextValue;
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        //给一个初始长、宽
        int mDefaultWidth = 200;
        int mDefaultHeight = (int) (100 * Util.getDensity(getContext()));
        setMeasuredDimension(resolveSize(mDefaultWidth,widthMeasureSpec),resolveSize(mDefaultHeight,heightMeasureSpec));
        viewHeight = getMeasuredHeight();
        viewWidth = getMeasuredWidth();
        pointX = viewWidth / 2;
        Log.d(TAG, "onMeasure: " + viewWidth);
    }

    @Override
    public void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        mMiddleValue = (pointTopY + pointBottomY) / 2;
        pointY = mMiddleValue + ((pointBottomY-pointTopY) * 1f / (maxValue - minValue) * ((maxValue + minValue) / 2 - currentValue));

        Log.d(TAG, "onDraw: " + pointY);
        mPaint = new Paint();
        drawGraph(canvas);
        drawValue(canvas);
        drawPoint(canvas);
    }

    //绘制数值
    private void drawValue(Canvas canvas){
        mPaint.setTextSize(50);
        setTextColor();
        mPaint.setStrokeWidth(0);
        mPaint.setStyle(Paint.Style.FILL);
        mPaint.setTextAlign(Paint.Align.CENTER);
        canvas.drawText(currentValue +"°" ,pointX , pointY - 20, mPaint);


//        Bitmap icon =  BitmapFactory.decodeResource(getResources(), R.drawable.blowing_snow);
//        canvas.drawBitmap(icon, pointX, pointY - 30, mPaint);

//        Bitmap bitmap = BitmapFactory.decodeResource(getResources(), R.drawable.blowing_snow);
//
//        // Create a Rect object with the size of the view
//        RectF viewRect = new RectF(0, 0, getWidth(), getHeight());
//
//        // Create a RectF object with the size of the bitmap
////        RectF bitmapRect = new RectF(0, 0, bitmap.getWidth(), bitmap.getHeight());
//        RectF bitmapRect = new RectF(0, 0, bitmap.getWidth(), bitmap.getHeight());
//
//        // Create a Matrix object and set its values to scale the bitmap to fit inside the view
//        Matrix matrix = new Matrix();
//        matrix.setRectToRect(viewRect, bitmapRect, Matrix.ScaleToFit.CENTER);
//        matrix.setScale(60f / bitmap.getWidth(), 60f / bitmap.getHeight());
//
//        // Apply the matrix to the RectF object
//        matrix.mapRect(bitmapRect);
//
//        // Draw the bitmap on the canvas
//        canvas.drawBitmap(bitmap, null, bitmapRect, null);
    }

    //设置字体颜色
    public void setTextColor(){
//        if(currentValue <= 10 && currentValue >= 0){
//            mPaint.setColor(Color.BLUE);
//        }else if(currentValue <= 20 && currentValue > 10){
//            mPaint.setColor(Color.GREEN);
//        }else if(currentValue <= 30 && currentValue > 20){
//            mPaint.setColor(0xFFFF8000);
//        }else if(currentValue <= 40 && currentValue > 30){
//            mPaint.setColor(Color.RED);
//        }

        mPaint.setColor(Color.WHITE);
    }

    //绘制温度点
    public void drawPoint(Canvas canvas){
//        mPaint.setColor(Color.BLUE);
//        mPaint.setStrokeWidth(2);
//        mPaint.setStyle(Paint.Style.STROKE);
//        canvas.drawCircle(pointX, pointY, 10, mPaint);
        mPaint.setColor(Color.WHITE);
        mPaint.setStyle(Paint.Style.FILL);
        canvas.drawCircle(pointX, pointY, 6, mPaint);
    }

    //绘制线段（线段组成折线）
    public void drawGraph(Canvas canvas){
//        mPaint.setColor(0xFF24C3F1);
        mPaint.setColor(Color.GREEN);
        mPaint.setStyle(Paint.Style.FILL);
        mPaint.setStrokeWidth(8);
        mPaint.setAntiAlias(true);    //设置抗锯齿

        //判断是否画左线段（第一个View不用，其他全要）
        if(isDrawLeftLine){
            float middleValue = currentValue - (currentValue - lastValue) / 2f;

            float middleY = mMiddleValue + (int) ((pointBottomY-pointTopY) * 1f / (maxValue - minValue) * ((maxValue + minValue) / 2 - middleValue));
            canvas.drawLine(0, middleY, pointX, pointY, mPaint);
        }

        //判断是否画右线段（最后View不用，其他全要）
        if(isDrawRightLine){
            float middleValue = currentValue - (currentValue - nextValue) / 2f;
            float middleY = mMiddleValue + (int) ((pointBottomY-pointTopY) * 1f / (maxValue - minValue) * ((maxValue + minValue) / 2 - middleValue));
            canvas.drawLine(pointX, pointY, viewWidth, middleY, mPaint);
        }

//
//        Path path = new Path();
//
//
//
//        if(isDrawLeftLine){
//            float middleValue = currentValue - (currentValue - lastValue) / 2f;
//
//            float middleY = mMiddleValue + (int) ((pointBottomY-pointTopY) * 1f / (maxValue - minValue) * ((maxValue + minValue) / 2 - middleValue));
//
//            path.moveTo(pointX, pointY);
//            path.cubicTo(0, pointY, );
//        }
//
//        //判断是否画右线段（最后View不用，其他全要）
//        if(isDrawRightLine){
//            float middleValue = currentValue - (currentValue - nextValue) / 2f;
//            float middleY = mMiddleValue + (int) ((pointBottomY-pointTopY) * 1f / (maxValue - minValue) * ((maxValue + minValue) / 2 - middleValue));
//            canvas.drawLine(pointX, pointY, viewWidth, middleY, mPaint);
//        }
    }
}
