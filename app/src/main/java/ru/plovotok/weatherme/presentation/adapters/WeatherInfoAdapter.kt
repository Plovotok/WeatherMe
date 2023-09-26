package ru.plovotok.weatherme.presentation.adapters

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import ru.plovotok.weatherme.R
import ru.plovotok.weatherme.presentation.base.BaseAdapter
import ru.plovotok.weatherme.presentation.base.viewhelperclasses.WeatherInfo
import ru.plovotok.weatherme.presentation.base.viewhelperclasses.WeatherInfoType
import ru.plovotok.weatherme.databinding.WeatherInfoItemLayoutBinding

class WeatherInfoAdapter : BaseAdapter<WeatherInfoItemLayoutBinding, WeatherInfo>() {
    override fun createViewHolder(parent: ViewGroup): BaseViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.weather_info_item_layout, parent, false)
        return ViewHolder(view, WeatherInfoItemLayoutBinding.bind(view))
    }

    inner class ViewHolder(itemView : View, override val binding: WeatherInfoItemLayoutBinding) : BaseViewHolder(itemView) {
        @SuppressLint("SetTextI18n")
        override fun bind(item: WeatherInfo) {

            var valueDimension = ""

            binding.name.text = item.name

            with(binding.weatherInfoIcon) {
                when(item.type) {
                    WeatherInfoType.WIND_SPEED -> {
                        setImageResource(R.drawable.wind)
                        valueDimension = "m/s"
                        val formattedValue = String.format("%.2f", item.value)
                        binding.value.text = "$formattedValue $valueDimension"
                    }
                    WeatherInfoType.PRESSURE -> {
                        setImageResource(R.drawable.pressure)
                        valueDimension = "mm Hg"
                        binding.value.text = "${item.value.toInt()} $valueDimension"
                    }
                    WeatherInfoType.HUMIDITY -> {
                        setImageResource(R.drawable.rain_chance)
                        valueDimension = "%"
                        binding.value.text = "${item.value.toInt()} $valueDimension"
                    }
                    WeatherInfoType.UV_INDEX -> {
                        setImageResource(R.drawable.uv_index)
                        valueDimension = ""
                        binding.value.text = "${item.value.toInt()} $valueDimension"
                    }
                }
            }

//            binding.value.text = "${item.value.toInt()} $valueDimension"
        }

    }
}