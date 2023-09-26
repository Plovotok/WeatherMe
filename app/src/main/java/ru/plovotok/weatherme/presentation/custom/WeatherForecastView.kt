package ru.plovotok.weatherme.presentation.custom

import android.content.Context
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.util.AttributeSet
import android.util.Log
import android.view.View

class WeatherForecastView(context : Context) : View(context) {


    private val TAG = "TemperatureView"

    private var minValue = 0
    private var maxValue = 0
    private var currentValue = 0
    private var lastValue = 0
    private var nextValue = 0
    private var mPaint: Paint? = null
    private var viewHeight = 0
    private var viewWidth = 0
    private var pointX = 0
    private var pointY = 0
    private var isDrawLeftLine = false
    private var isDrawRightLine = false
    private val pointTopY = (40 * Util.getDensity(getContext())).toInt()
    private val pointBottomY = (200 * Util.getDensity(getContext())).toInt()
    private var mMiddleValue = 0


    //设置最小值
    fun setMinValue(minValue: Int) {
        this.minValue = minValue
    }

    //设置最大值
    fun setMaxValue(maxValue: Int) {
        this.maxValue = maxValue
    }

    //设置目前的值
    fun setCurrentValue(currentValue: Int) {
        this.currentValue = currentValue
    }

    //设置是否画左边线段(只有第一个View是false)
    fun setDrawLeftLine(isDrawLeftLine: Boolean) {
        this.isDrawLeftLine = isDrawLeftLine
    }

    //设置是否画右边线段(只有最后一个View是false)
    fun setDrawRightLine(isDrawRightLine: Boolean) {
        this.isDrawRightLine = isDrawRightLine
    }

    //设置之前温度点的值
    fun setLastValue(lastValue: Int) {
        this.lastValue = lastValue
    }

    //设置下一个温度点的值
    fun setNextValue(nextValue: Int) {
        this.nextValue = nextValue
    }

    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec)
        //给一个初始长、宽
        val mDefaultWidth = 200
        val mDefaultHeight = (220 * Util.getDensity(
            context
        )).toInt()
        setMeasuredDimension(
            resolveSize(mDefaultWidth, widthMeasureSpec),
            resolveSize(mDefaultHeight, heightMeasureSpec)
        )
        viewHeight = measuredHeight
        viewWidth = measuredWidth
        pointX = viewWidth / 2
        Log.d(TAG, "onMeasure: $viewWidth")
    }

    override fun onDraw(canvas: Canvas) {
        super.onDraw(canvas)
        mMiddleValue = (pointTopY + pointBottomY) / 2
        pointY =
            mMiddleValue + ((pointBottomY - pointTopY) * 1f / (maxValue - minValue) * ((maxValue + minValue) / 2 - currentValue)).toInt()
        Log.d(TAG, "onDraw: $pointY")
        mPaint = Paint()
        drawGraph(canvas)
        drawValue(canvas)
        drawPoint(canvas)
    }

    //绘制数值
    private fun drawValue(canvas: Canvas) {
        mPaint!!.textSize = 40f
        setTextColor()
        mPaint!!.strokeWidth = 0f
        mPaint!!.style = Paint.Style.FILL
        mPaint!!.textAlign = Paint.Align.CENTER
        canvas.drawText(
            "$currentValue°", pointX.toFloat(), (pointY - 20).toFloat(),
            mPaint!!
        )
    }

    //设置字体颜色
    fun setTextColor() {
        if (currentValue <= 10 && currentValue >= 0) {
            mPaint!!.color = Color.BLUE
        } else if (currentValue <= 20 && currentValue > 10) {
            mPaint!!.color = Color.GREEN
        } else if (currentValue <= 30 && currentValue > 20) {
            mPaint!!.color = -0x8000
        } else if (currentValue <= 40 && currentValue > 30) {
            mPaint!!.color = Color.RED
        }
    }

    //绘制温度点
    fun drawPoint(canvas: Canvas) {
        mPaint!!.color = Color.BLUE
        mPaint!!.strokeWidth = 2f
        mPaint!!.style = Paint.Style.STROKE
        canvas.drawCircle(pointX.toFloat(), pointY.toFloat(), 10f, mPaint!!)
        mPaint!!.color = Color.WHITE
        mPaint!!.style = Paint.Style.FILL
        canvas.drawCircle(pointX.toFloat(), pointY.toFloat(), 5f, mPaint!!)
    }

    //绘制线段（线段组成折线）
    fun drawGraph(canvas: Canvas) {
        mPaint!!.color = -0xdb3c0f
        mPaint!!.style = Paint.Style.FILL
        mPaint!!.strokeWidth = 3f
        mPaint!!.isAntiAlias = true //设置抗锯齿

        //判断是否画左线段（第一个View不用，其他全要）
        if (isDrawLeftLine) {
            val middleValue = currentValue - (currentValue - lastValue) / 2f
            val middleY =
                (mMiddleValue + ((pointBottomY - pointTopY) * 1f / (maxValue - minValue) * ((maxValue + minValue) / 2 - middleValue)).toInt()).toFloat()
            canvas.drawLine(0f, middleY, pointX.toFloat(), pointY.toFloat(), mPaint!!)
        }

        //判断是否画右线段（最后View不用，其他全要）
        if (isDrawRightLine) {
            val middleValue = currentValue - (currentValue - nextValue) / 2f
            val middleY =
                (mMiddleValue + ((pointBottomY - pointTopY) * 1f / (maxValue - minValue) * ((maxValue + minValue) / 2 - middleValue)).toInt()).toFloat()
            canvas.drawLine(
                pointX.toFloat(), pointY.toFloat(), viewWidth.toFloat(), middleY,
                mPaint!!
            )
        }
    }
}