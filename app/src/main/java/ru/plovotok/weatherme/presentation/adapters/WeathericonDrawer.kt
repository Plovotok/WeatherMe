package ru.plovotok.weatherme.presentation.adapters

import android.content.Context
import android.text.Spannable
import android.text.SpannableString
import android.text.style.ImageSpan
import android.util.Log
import androidx.core.content.ContextCompat
import com.github.mikephil.charting.components.AxisBase
import com.github.mikephil.charting.formatter.ValueFormatter
import ru.plovotok.weatherme.R

class WeathericonDrawer(private val context : Context) : ValueFormatter() {
    override fun getFormattedValue(value: Float): String {
        return " "
    }

    override fun getAxisLabel(value: Float, axis: AxisBase?): String {

        if (value > -13f) {
            val drawable = ContextCompat.getDrawable(context, R.drawable.hourly_forecast)
            drawable?.setBounds(0, 0, drawable.intrinsicWidth, drawable.intrinsicHeight)
            val spannable = SpannableString(" ")
            val imageSpan = ImageSpan(drawable!!, ImageSpan.ALIGN_BOTTOM)
            spannable.setSpan(imageSpan, 0, 1, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE)
            Log.w("Custom", spannable.toString())
            return spannable.toString()
        }
        return super.getAxisLabel(value, axis)
    }
}