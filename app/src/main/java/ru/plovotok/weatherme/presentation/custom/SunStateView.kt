package ru.plovotok.weatherme.presentation.custom

import android.content.Context
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.graphics.Path
import android.graphics.RadialGradient
import android.graphics.Shader
import android.util.AttributeSet
import android.util.Log
import android.view.View
import ru.plovotok.weatherme.R
import kotlin.math.PI
import kotlin.math.cos

/**
 * View to show current sun position at the sky
 */
class SunStateView : View {

    private var sunRiseTime : Long = (MILLIS_IN_DAY * 0.25).toLong()
    private var sunSetTime : Long = (MILLIS_IN_DAY * 0.75).toLong()

    private var currentTime : Long = (MILLIS_IN_DAY * 0.5).toLong()

    private var contentWidth = width
    private var contentHeight = height

    private var mPaddingLeft = 0
    private var mPaddingTop = 0
    private var mPaddingRight = 0
    private var mPaddingBottom = 0

    private var progress: Float = 50f
    private var sunRiseProgress : Double = 0.25
    private var sunSetProgress : Double = 0.75
    private var correctMiddleDayTime : Long = MILLIS_IN_DAY / 2
    private var correctMiddleNightTime : Long = 0
    private var delta : Long = 0

    private var lineColor : Int = Color.WHITE
    private var linePaint = Paint().apply {
        flags = Paint.ANTI_ALIAS_FLAG
        style = Paint.Style.STROKE
        strokeWidth = 3f
        color = lineColor
    }
    private var sunRadius = 40f
    private val sunPaint = Paint().apply {
        flags = Paint.ANTI_ALIAS_FLAG
        style = Paint.Style.FILL_AND_STROKE
        strokeWidth = 3f
//        color = Color.WHITE

    }

    private var path = Path()

    constructor(context: Context) : super(context) {
        init(null, 0)
    }

    constructor(context: Context, attrs: AttributeSet) : super(context, attrs) {
        init(attrs, 0)
    }

    constructor(context: Context, attrs: AttributeSet, defStyle: Int) : super(
        context,
        attrs,
        defStyle
    ) {
        init(attrs, defStyle)
    }

    private fun init(attrs: AttributeSet?, defStyle: Int) {
        // Load attributes
        val a = context.obtainStyledAttributes(
            attrs, R.styleable.SunStateView, defStyle, 0
        )

        lineColor = a.getColor(R.styleable.SunStateView_progressColor, Color.WHITE)

        a.recycle()
    }


    override fun onDraw(canvas: Canvas) {
        super.onDraw(canvas)

        path.reset()

        mPaddingLeft = paddingLeft
        mPaddingTop = paddingTop
        mPaddingRight = paddingRight
        mPaddingBottom = paddingBottom

        contentWidth = width - paddingLeft - paddingRight
        contentHeight = height - paddingTop - paddingBottom - (linePaint.strokeWidth).toInt() - sunRadius.toInt() * 2

        linePaint.color = lineColor

        path.apply {
            moveTo(paddingLeft.toFloat(), cosine(0f))

            for (i in 0..contentWidth step 1) {
                val x = i.toFloat()
                val y = cosine(i.toFloat())
                lineTo(x, y)
            }
        }

        canvas.drawPath(path, linePaint)

        val horizontalLineHeight = defineHorizontalLinePercentage() * contentHeight +
                paddingTop + (linePaint.strokeWidth/2).toInt() + sunRadius


        canvas.drawLine(
            paddingLeft.toFloat(),
            horizontalLineHeight,
            (paddingLeft + contentWidth).toFloat(),
            horizontalLineHeight,
            linePaint)

        val cx = contentWidth * progress/100

        val cor = if (currentTime in sunRiseTime*0.993.toLong()..sunRiseTime) {
//            -sunRadius
            0f
        } else if (currentTime in sunSetTime..sunSetTime*1.007.toLong()) {
//            sunRadius
            0f
        } else {
            0f
        }
        drawSunByCoordinates(cx + cor, cy = cosine(cx + cor - paddingLeft), canvas)
    }

    private fun drawSunByCoordinates(cx : Float, cy : Float, canvas : Canvas) {
        sunPaint.shader = RadialGradient(cx, cy, sunRadius, Color.WHITE, Color.TRANSPARENT, Shader.TileMode.CLAMP)

        canvas.drawCircle(cx, cy, sunRadius, sunPaint)
        sunPaint.style = Paint.Style.STROKE
        sunPaint.strokeWidth = 10f
        sunPaint.shader = RadialGradient(cx, cy, sunRadius * 2f, Color.WHITE, Color.TRANSPARENT, Shader.TileMode.CLAMP)
        canvas.drawCircle(cx, cy, sunRadius * 2, sunPaint)

        if (currentTime in sunRiseTime..sunSetTime) {
            sunPaint.strokeWidth = 3f
            sunPaint.style = Paint.Style.FILL_AND_STROKE
            sunPaint.color = Color.WHITE
            sunPaint.shader = Shader()
            canvas.drawCircle(cx, cy, sunRadius / 3, sunPaint)
        } else {
            sunPaint.strokeWidth = 3f
            sunPaint.style = Paint.Style.FILL_AND_STROKE
            sunPaint.color = Color.BLACK
            sunPaint.shader = Shader()
            canvas.drawCircle(cx, cy, sunRadius / 3, sunPaint)
        }


    }

    private fun cosine(value : Float) : Float {
        return (cos(value * 2 * PI / width) * contentHeight / 2 + height / 2).toFloat()
    }

    private fun defineHorizontalLinePercentage() : Float {

        val percent: Float = if (MILLIS_IN_DAY - sunRiseTime >= 0) {
            (sunSetTime - sunRiseTime).toFloat()/ MILLIS_IN_DAY
        } else {
            0.5f
        }

        Log.i("SunState", "percents : ${percent}")
        return percent
    }

    fun setCurrentTime(timeInMillis : Long) {
        currentTime = timeInMillis

        if (currentTime in correctMiddleNightTime until MILLIS_IN_DAY) {

            progress = (currentTime.toFloat() / MILLIS_IN_DAY * 100)
            Log.e("SunState", "currentTime : $currentTime, field 1, progress : $progress")
        } else if (currentTime in 0 until correctMiddleNightTime) {

            val innerProgress = (MILLIS_IN_DAY - correctMiddleNightTime).toFloat() * 100 / MILLIS_IN_DAY
            progress = innerProgress + currentTime.toFloat() * 100 / MILLIS_IN_DAY
            Log.e("SunState", "currentTime : $currentTime, field 2, progress : $progress")
        }

        invalidate()
    }

    fun setSunRiseAndSetTime(sunRiseTimeMillis : Long, sunSetTimeMillis : Long) {
        sunRiseTime = sunRiseTimeMillis
        sunSetTime = sunSetTimeMillis

        sunRiseProgress = sunRiseTime.toDouble() / MILLIS_IN_DAY
        Log.i("SunState", "sunRiseTime : $sunRiseTime")
        sunSetProgress = sunSetTime.toDouble() / MILLIS_IN_DAY
        Log.i("SunState", "sunSetTime : $sunSetTime")

        correctMiddleDayTime = (sunSetTime - sunRiseTime) / 2 + sunRiseTime
        delta = correctMiddleDayTime - MILLIS_IN_DAY / 2
        correctMiddleNightTime = delta

        Log.i("SunState", "correctmiddleDayTime : $correctMiddleDayTime\n correctmiddleNightTime : $correctMiddleNightTime")

        invalidate()
    }

    fun setSunRiseTime(timeInMillis : Long) {
        sunRiseTime = timeInMillis

        invalidate()
    }

    fun setSunSetTime(timeInMillis : Long) {
        sunSetTime = timeInMillis

        invalidate()
    }

    companion object {
        const val MILLIS_IN_DAY = 60 * 60 * 24 * 1000L
        const val SECONDS_PER_DAY = 60 * 60 * 24
    }

}