package ru.plovotok.weatherme.presentation.adapters

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import ru.plovotok.weatherme.R
import ru.plovotok.weatherme.databinding.WeatherInfoItemLayoutBinding
import ru.plovotok.weatherme.presentation.base.BaseAdapter
import ru.plovotok.weatherme.presentation.base.viewhelperclasses.AstroInfo
import ru.plovotok.weatherme.presentation.base.viewhelperclasses.AstroType
import ru.plovotok.weatherme.presentation.base.viewhelperclasses.WeatherInfo
import ru.plovotok.weatherme.presentation.base.viewhelperclasses.WeatherInfoType

class AstroAdapter : BaseAdapter<WeatherInfoItemLayoutBinding, AstroInfo>() {
    override fun createViewHolder(parent: ViewGroup): BaseViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.weather_info_item_layout, parent, false)
        return ViewHolder(view, WeatherInfoItemLayoutBinding.bind(view))
    }

    inner class ViewHolder(itemView : View, override val binding: WeatherInfoItemLayoutBinding) : BaseViewHolder(itemView) {
        @SuppressLint("SetTextI18n")
        override fun bind(item: AstroInfo) {

            with(binding.weatherInfoIcon) {
                when(item.type) {
                    AstroType.SUNRISE -> {
                        binding.name.text = "Sunrise"
                        setImageResource(R.drawable.sunrise)
                    }
                    AstroType.SUNSET -> {
                        binding.name.text = "Sunset"
                        setImageResource(R.drawable.sunset)
                    }
                }
            }

            binding.value.text = item.time
        }

    }
}