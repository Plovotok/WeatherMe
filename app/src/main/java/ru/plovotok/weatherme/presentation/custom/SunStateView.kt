package ru.plovotok.weatherme.presentation.custom

import android.animation.Animator
import android.animation.ObjectAnimator
import android.content.Context
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.LinearGradient
import android.graphics.Paint
import android.graphics.Path
import android.graphics.RadialGradient
import android.graphics.RectF
import android.graphics.Shader
import android.util.AttributeSet
import android.util.Log
import android.view.View
import android.view.animation.AccelerateDecelerateInterpolator
import ru.plovotok.weatherme.R
import kotlin.math.PI
import kotlin.math.abs
import kotlin.math.acos
import kotlin.math.cos
import kotlin.math.sin

/**
 * View to show current sun position at the sky
 * <p>
 * @author Plovotok
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

    private var horizontalLineHeight : Float = 0f

    private var progress: Float = 0f
        set(value) {
            field = value
            invalidate()
        }

    /**
     * returns progress of the day
     * <p>
     * @return progress in percents
     */
    fun getProgress() = progress

    private var sunRiseProgress : Double = 0.25
    private var sunSetProgress : Double = 0.75
    private var correctMiddleDayTime : Long = MILLIS_IN_DAY / 2
    private var correctMiddleNightTime : Long = 0
    private var delta : Long = 0

    private var lineColor : Int = Color.WHITE
    private var linePaint = Paint().apply {
        flags = Paint.ANTI_ALIAS_FLAG
        style = Paint.Style.STROKE
        strokeWidth = 4f
        isAntiAlias = true
        color = lineColor
    }
    private var sunRadius = 40f
    private val sunPaint = Paint().apply {
        flags = Paint.ANTI_ALIAS_FLAG
        style = Paint.Style.FILL_AND_STROKE
        strokeWidth = 3f

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
        progress = a.getFloat(R.styleable.SunStateView_progress, 0f)

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

        path.apply {
            moveTo(paddingLeft.toFloat(), countHeightByCosValue(0f))

            for (i in 0..contentWidth step 1) {
                val x = i.toFloat()
                val y = countHeightByCosValue(i.toFloat())
                lineTo(x, y)
            }
        }

        setupLinearGradient()
        canvas.drawPath(path, linePaint)

        horizontalLineHeight = defineHorizontalLinePercentage() * contentHeight +
                paddingTop + (linePaint.strokeWidth/2).toInt() + sunRadius

        setupRadialGradient()

        linePaint.strokeWidth = 3f

        canvas.drawLine(
            paddingLeft.toFloat(),
            horizontalLineHeight,
            (paddingLeft + contentWidth).toFloat(),
            horizontalLineHeight ,
            linePaint)

        linePaint.strokeWidth = 4f

        val cx = contentWidth * progress/100
        val cor = defineCorrDeltaX()
        drawSunByCoordinates(cx + cor, cy = countHeightByCosValue(cx + cor - paddingLeft), canvas)
    }

    private fun defineCorrDeltaX() : Float {

        val value = sunRiseProgress
        val tangAlpha = -sin(value * 2 * PI) * (2 * PI)

        return if (currentTime > MILLIS_IN_DAY / 2) {
            -((sunRadius / 2) / tangAlpha).toFloat()
        } else {
            ((sunRadius / 2) / tangAlpha).toFloat()
        }
    }

    private fun setupLinearGradient() {
        linePaint.shader = LinearGradient(
            0f, (paddingTop + contentHeight).toFloat(), 0f, 0f,
            Color.TRANSPARENT, lineColor, Shader.TileMode.CLAMP)
    }

    private fun setupRadialGradient() {
        linePaint.shader = RadialGradient(
            width.toFloat() / 2, (paddingTop + contentHeight).toFloat(),
            width.toFloat() / 2.2f,
            Color.WHITE, Color.TRANSPARENT, Shader.TileMode.CLAMP )
    }

    private fun drawSunByCoordinates(cx : Float, cy : Float, canvas : Canvas) {
        sunPaint.color = Color.WHITE
        sunPaint.shader = RadialGradient(cx, cy, sunRadius, Color.WHITE, Color.TRANSPARENT, Shader.TileMode.CLAMP)

//        Draw Ray circle
        canvas.drawCircle(cx, cy, sunRadius, sunPaint)
        sunPaint.style = Paint.Style.STROKE
        sunPaint.strokeWidth = 10f
        sunPaint.shader = RadialGradient(cx, cy, sunRadius * 2f, Color.WHITE, Color.TRANSPARENT, Shader.TileMode.CLAMP)

//        Draw Glare
//        canvas.drawCircle(cx, cy, sunRadius * 2, sunPaint)

        if (currentTime in sunRiseTime..sunSetTime) {
            sunPaint.strokeWidth = 3f
            sunPaint.style = Paint.Style.FILL_AND_STROKE
            sunPaint.shader = null
        } else {
            sunPaint.strokeWidth = 3f
            sunPaint.style = Paint.Style.FILL_AND_STROKE
            sunPaint.shader = null
        }
//        Draw Sun
        canvas.drawCircle(cx, cy, sunRadius / 3, sunPaint)

        sunPaint.color = Color.BLACK
        val degreeDis = defineVerticalDegreeDis(cy, horizontalLineHeight)
//        Draw shadow
        canvas.drawArc(
            RectF(cx - sunRadius/3, cy - sunRadius/3, cx + sunRadius/3, cy + sunRadius/3),
            -180f - 90f + degreeDis, - 2 * degreeDis, false, sunPaint
        )

    }

    private fun defineVerticalDegreeDis(cy: Float, lineY : Float) : Float {
        if (abs(cy - lineY - linePaint.strokeWidth/2) <= (sunRadius/3)) {
            if ((cy - lineY) < 0 ) {
                val deltaH = lineY - cy //+ linePaint.strokeWidth/2
                val angle = acos(deltaH / (sunRadius/3))
                return Math.toDegrees(angle.toDouble()).toFloat()
            } else {
                val deltaH = cy - lineY //+ linePaint.strokeWidth/2
                val angle = acos(deltaH / (sunRadius/3))
                return 180f - Math.toDegrees(angle.toDouble()).toFloat()
            }
        } else {
            return if (cy > lineY) {
                360f
            } else {
                0f
            }
        }
    }

    private fun countHeightByCosValue(value : Float) : Float {
        return (cos(value * 2 * PI / width) * contentHeight / 2 + height / 2).toFloat()
    }

    private fun defineHorizontalLinePercentage() : Float {

        val percentage: Float = if (MILLIS_IN_DAY - sunRiseTime >= 0) {
            (sunSetTime - sunRiseTime).toFloat()/ MILLIS_IN_DAY
        } else {
            0.5f
        }
        Log.w(TAG, "horizontal percentage = $percentage")

        return percentage

    }

    /**
     * Set the current time of the day.
     * <p>
     * @param timeInMillis current time of the day in millis
     */
    fun setCurrentTime(timeInMillis : Long) {
        currentTime = timeInMillis
        var tempProgress = 0f

//        if (currentTime in correctMiddleNightTime until MILLIS_IN_DAY) {
//            tempProgress = (currentTime.toFloat() / MILLIS_IN_DAY * 100)
//        } else if (currentTime in 0 until correctMiddleNightTime) {
//            val innerProgress = (MILLIS_IN_DAY - correctMiddleNightTime).toFloat() * 100 / MILLIS_IN_DAY
//            tempProgress = innerProgress + currentTime.toFloat() * 100 / MILLIS_IN_DAY
//        }

        if (currentTime in delta until MILLIS_IN_DAY) {
            tempProgress = (currentTime - delta).toFloat() / MILLIS_IN_DAY * 100
        } else if (currentTime in 0 until delta) {
            val innerProgress = (MILLIS_IN_DAY - delta) / MILLIS_IN_DAY * 100
            tempProgress = innerProgress + currentTime.toFloat() / MILLIS_IN_DAY * 100
        }

        val animation = ObjectAnimator.ofFloat(this, "progress", 0f, tempProgress)
        animation.interpolator = AccelerateDecelerateInterpolator()
        animation.startDelay = 2_000L
        animation.duration = 2_000L
        animation.addListener(object : Animator.AnimatorListener {
            override fun onAnimationStart(animation: Animator) {
                Log.i(TAG, "start animation")
                progress = 0f
            }

            override fun onAnimationEnd(animation: Animator) {
                Log.i(TAG, "end animation")
                progress = tempProgress
            }

            override fun onAnimationCancel(animation: Animator) {
                Log.i(TAG, "cancel animation")
            }

            override fun onAnimationRepeat(animation: Animator) {
                Log.i(TAG, "repeat animation")
            }
        })
        animation.start()
//        invalidate()
    }

    /**
     * Set the sunrise time and sunset time.
     * <p>
     * @param sunRiseTimeMillis sunrise time in millis
     * @param sunSetTimeMillis sunset time in millis
     */
    fun setSunRiseAndSunSetTime(sunRiseTimeMillis : Long, sunSetTimeMillis : Long) {

        sunRiseTime = sunRiseTimeMillis
        sunSetTime = sunSetTimeMillis

        correctMiddleDayTime = (sunSetTimeMillis + sunRiseTimeMillis) / 2
        delta = correctMiddleDayTime - MILLIS_IN_DAY / 2
        correctMiddleNightTime = delta

        Log.i(TAG, "correctmiddleDayTime : $correctMiddleDayTime\n correctmiddleNightTime : $correctMiddleNightTime")

        sunRiseProgress = (sunRiseTime - delta).toDouble() / MILLIS_IN_DAY
        Log.i(TAG, "sunRiseTime : $sunRiseTime")
        sunSetProgress = (sunSetTime + delta).toDouble() / MILLIS_IN_DAY
        Log.i(TAG, "sunSetTime : $sunSetTime")

        invalidate()
    }

    companion object {
        const val MILLIS_IN_DAY = 60 * 60 * 24 * 1000L
        const val SECONDS_PER_DAY = 60 * 60 * 24
        private const val TAG ="SunStateView"
    }

}