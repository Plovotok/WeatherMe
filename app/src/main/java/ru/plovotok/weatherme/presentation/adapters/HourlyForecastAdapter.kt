package ru.plovotok.weatherme.presentation.adapters

import android.graphics.Color
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
import ru.plovotok.weatherme.presentation.base.defineWeatherByCondition
import ru.plovotok.weatherme.presentation.base.viewhelperclasses.HourForecast

class HourlyForecastAdapter : BaseAdapter<HourInfoItemLayoutBinding, HourForecast>() {

//    private var items = listOf<HourForecast>()
    private var entriesList = mutableListOf<Entry>()
    private var dataset : LineDataSet? = null
    private var data : LineData? = null
    private val compensTemp = 100f

    override fun createViewHolder(parent: ViewGroup): BaseViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.hour_info_item_layout, parent, false)
        return ViewHolder(view, HourInfoItemLayoutBinding.bind(view))
    }

    inner class ViewHolder(itemView: View, override val binding: HourInfoItemLayoutBinding) : BaseViewHolder(itemView) {
        override fun bind(item: HourForecast) {
//            binding.avgTemp.text = "${item.avgTemp}°"
            if (adapterPosition == 0) {
                makeEntries()
                binding.time.text = "Now"
            } else {
                binding.time.text = item.time.split(" ").last()
            }
            val imageResource = defineWeatherByCondition(iconCode = item.code, isDay = item.isDay).iconResource
            binding.weatherIcon.setImageResource(imageResource)

            customizeDataSet()
            drawClippedGraph()

        }

        private fun drawClippedGraph() {
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
            binding.lineChart.axisLeft.axisMinimum = getItems().sortedBy { it.avgTemp }[0].avgTemp.toFloat() - 10f + compensTemp
            binding.lineChart.axisLeft.axisMaximum = getItems().sortedBy { it.avgTemp }[getItems().size - 1].avgTemp.toFloat() + 2f + compensTemp


            binding.lineChart.xAxis.axisMinimum = ((adapterPosition - 1).toFloat() + (adapterPosition).toFloat())/2f
            binding.lineChart.xAxis.axisMaximum = ((adapterPosition).toFloat() + (adapterPosition + 1).toFloat())/2f

            binding.lineChart.invalidate()
        }

        private fun customizeDataSet() {
            dataset?.color = Color.GREEN
            dataset?.gradientColors = listOf(GradientColor(Color.RED, Color.BLUE))
            dataset?.lineWidth = 3f
            dataset?.valueTextColor = Color.WHITE
            dataset?.fillAlpha = 20
            dataset?.fillDrawable = AppCompatResources.getDrawable(itemView.context, R.drawable.chart_gradient)
            dataset?.setDrawFilled(true)
            dataset?.setDrawValues(true)

            dataset?.setDrawCircles(true)
            dataset?.setCircleColor(Color.GREEN)
            dataset?.circleRadius = 4f
            dataset?.circleHoleColor = Color.WHITE
            dataset?.circleHoleRadius = 2f
            dataset?.setDrawCircleHole(true)

            dataset?.setDrawVerticalHighlightIndicator(true)
            dataset?.setDrawIcons(true)
            dataset?.mode = LineDataSet.Mode.CUBIC_BEZIER
            dataset?.cubicIntensity = 0.2f
            dataset?.valueTextSize = 20f
            dataset?.valueFormatter = object : ValueFormatter() {
                override fun getFormattedValue(value: Float): String {
                    return "${value.toInt() - compensTemp.toInt()}°"
                }
            }
        }

        private fun makeEntries() {
            val items = getItems()
            val entries = mutableListOf<Entry>()
            entries.add(Entry(-1f, items[0].avgTemp.toFloat() + compensTemp))
            for (i in items.indices) {
                entries.add(Entry(i.toFloat(), items[i].avgTemp.toInt().toFloat() + compensTemp))
            }
            entries.add(Entry(entries.size.toFloat(), items[itemCount-1].avgTemp.toFloat() + compensTemp))
            entriesList = entries
            dataset = LineDataSet(entriesList, "График первый")
            data = LineData(dataset)
        }

    }
}