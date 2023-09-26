package ru.plovotok.weatherme.presentation.adapters

import android.app.Application
import android.content.res.Resources
import android.graphics.BlendMode
import android.graphics.Color
import android.graphics.drawable.GradientDrawable
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.content.res.AppCompatResources
import com.github.mikephil.charting.data.Entry
import com.github.mikephil.charting.data.LineData
import com.github.mikephil.charting.data.LineDataSet
import com.github.mikephil.charting.formatter.ValueFormatter
import com.github.mikephil.charting.model.GradientColor
import ru.plovotok.weatherme.R
import ru.plovotok.weatherme.databinding.HourInfoItemLayoutBinding
import ru.plovotok.weatherme.presentation.base.BaseAdapter
import ru.plovotok.weatherme.presentation.base.defineWeatherIconID
import ru.plovotok.weatherme.presentation.base.viewhelperclasses.HourForecast

class HourlyForecastAdapter : BaseAdapter<HourInfoItemLayoutBinding, HourForecast>() {

    override fun createViewHolder(parent: ViewGroup): BaseViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.hour_info_item_layout, parent, false)
        return ViewHolder(view, HourInfoItemLayoutBinding.bind(view))
    }

    inner class ViewHolder(itemView: View, override val binding: HourInfoItemLayoutBinding) : BaseViewHolder(itemView) {
        override fun bind(item: HourForecast) {
            binding.avgTemp.text = "${item.avgTemp}°"
            if (adapterPosition == 0) {
                binding.time.text = "Now"
            } else {
                binding.time.text = item.time.split(" ").last()
            }

            val imageResource = defineWeatherIconID(iconCode = item.code, isDay = item.isDay).iconResource
            binding.weatherIcon.setImageResource(imageResource)

            with(binding.temperatureView) {
                setMinValue(getItems().sortedBy { it.avgTemp }[0].avgTemp.toFloat() - 1f)
                setMaxValue(getItems().sortedBy { it.avgTemp }[getItems().size - 1].avgTemp.toFloat() + 1f)
            }


            //如果是第一个
            if (adapterPosition == 0) {
                binding.temperatureView.setDrawLeftLine(false)
            } else {
                binding.temperatureView.setDrawLeftLine(true)
                binding.temperatureView.setLastValue(getItems()[adapterPosition-1].avgTemp.toFloat())
            }

            //如果是最后一个

            //如果是最后一个
            if (adapterPosition == getItems().size - 1) {
                binding.temperatureView.setDrawRightLine(false)
            } else {
                binding.temperatureView.setDrawRightLine(true)
                binding.temperatureView.setNextValue(getItems()[adapterPosition + 1].avgTemp.toFloat())
            }

            binding.temperatureView.setCurrentValue(item.avgTemp.toFloat())

            val items = getItems()
            val entries = mutableListOf<Entry>()
            entries.add(Entry(-1f, items[0].avgTemp.toFloat()))
            for (i in items.indices) {
                entries.add(Entry(i.toFloat(), items[i].avgTemp.toInt().toFloat()))
            }
            entries.add(Entry(entries.size.toFloat(), items[itemCount-1].avgTemp.toFloat()))

            val dataset = LineDataSet(entries, "График первый")
            dataset.color = Color.GREEN
//            dataset.setGradientColor(Color.RED, Color.BLUE)
            dataset.gradientColors = listOf(GradientColor(Color.RED, Color.BLUE))
            dataset.lineWidth = 3f
            dataset.valueTextColor = Color.WHITE
            dataset.fillAlpha = 20
//            dataset.fillColor = itemView.context.getDrawable(R.drawable.header_background)
            dataset.fillDrawable = AppCompatResources.getDrawable(itemView.context, R.drawable.chart_gradient)
            dataset.setDrawFilled(true)
            dataset.setDrawValues(true)

            dataset.setDrawCircles(true)
            dataset.setCircleColor(Color.GREEN)
            dataset.circleRadius = 4f
            dataset.circleHoleColor = Color.WHITE
            dataset.circleHoleRadius = 2f
            dataset.setDrawCircleHole(true)

            dataset.setDrawVerticalHighlightIndicator(true)
            dataset.setDrawIcons(true)
            dataset.mode = LineDataSet.Mode.CUBIC_BEZIER
            dataset.cubicIntensity = 0.2f
            dataset.valueTextSize = 20f
            dataset.valueFormatter = object : ValueFormatter() {
                override fun getFormattedValue(value: Float): String {
                    return "${value.toInt()}°"
                }
            }
            val data = LineData(dataset)
            binding.lineChart.data = data
            binding.lineChart.pointerIcon
            binding.lineChart.legend.isEnabled = false
            binding.lineChart.setBackgroundColor(Color.TRANSPARENT)
            binding.lineChart.description.isEnabled = false
            binding.lineChart.setTouchEnabled(false)
            binding.lineChart.isDragEnabled = true
            binding.lineChart.xAxis.isEnabled = false
            binding.lineChart.axisLeft.isEnabled = false
            binding.lineChart.axisRight.isEnabled = false
            binding.lineChart.axisLeft.axisMinimum = getItems().sortedBy { it.avgTemp }[0].avgTemp.toFloat() - 10f
            binding.lineChart.axisLeft.axisMaximum = getItems().sortedBy { it.avgTemp }[getItems().size - 1].avgTemp.toFloat() + 2f

//            binding.lineChart.xAxis.axisMinimum = (adapterPosition).toFloat()
//            binding.lineChart.xAxis.axisMaximum = (adapterPosition + 1).toFloat()

            if (adapterPosition == 0) {
                binding.lineChart.xAxis.axisMinimum = ((adapterPosition - 1).toFloat() + (adapterPosition).toFloat())/2f
                binding.lineChart.xAxis.axisMaximum = ((adapterPosition).toFloat() + (adapterPosition + 1).toFloat())/2f
            } else if (adapterPosition == getItems().size - 1) {
                binding.lineChart.xAxis.axisMinimum = ((adapterPosition - 1).toFloat() + (adapterPosition).toFloat())/2f
                binding.lineChart.xAxis.axisMaximum = ((adapterPosition).toFloat() + (adapterPosition + 1).toFloat())/2f
            } else {
                binding.lineChart.xAxis.axisMinimum = ((adapterPosition - 1).toFloat() + (adapterPosition).toFloat())/2f
                binding.lineChart.xAxis.axisMaximum = ((adapterPosition).toFloat() + (adapterPosition + 1).toFloat())/2f
            }


            binding.lineChart.invalidate()
        }



    }
}