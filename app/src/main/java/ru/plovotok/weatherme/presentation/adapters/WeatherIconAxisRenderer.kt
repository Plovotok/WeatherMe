package ru.plovotok.weatherme.presentation.adapters

import android.content.Context
import android.graphics.Bitmap
import android.graphics.Canvas
import com.github.mikephil.charting.components.XAxis
import com.github.mikephil.charting.renderer.XAxisRenderer
import com.github.mikephil.charting.utils.ViewPortHandler

class WeatherIconAxisRenderer(private val context : Context, private val weatherIcons : List<Bitmap>,
viewPortHandler: ViewPortHandler, xAxis : XAxis) : XAxisRenderer(viewPortHandler, xAxis, null) {

    override fun renderAxisLabels(c: Canvas?) {
        val labelCount = mXAxis.mEntryCount
        val labelWidth = mXAxis.mLabelWidth
        val labelHeight = mXAxis.mLabelHeight

        for (i in 0 until labelCount) {
            val x = mViewPortHandler.contentBottom() * i
            val y = mViewPortHandler.contentBottom()

            val icon = weatherIcons[i]
            val iconLeft = x - icon.width/2
            val iconTop = y
            c?.drawBitmap(icon, iconLeft, iconTop, null)
        }
    }
}