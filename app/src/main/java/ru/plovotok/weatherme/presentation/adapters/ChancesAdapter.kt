package ru.plovotok.weatherme.presentation.adapters

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import ru.plovotok.weatherme.R
import ru.plovotok.weatherme.presentation.base.BaseAdapter
import ru.plovotok.weatherme.presentation.base.viewhelperclasses.ChanceOfPrecipitaion
import ru.plovotok.weatherme.databinding.ChanceOfRainItemLayoutBinding

class ChancesAdapter : BaseAdapter<ChanceOfRainItemLayoutBinding, ChanceOfPrecipitaion>() {
    override fun createViewHolder(parent: ViewGroup): BaseViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.chance_of_rain_item_layout, parent, false)
        return ViewHolder(view, ChanceOfRainItemLayoutBinding.bind(view))
    }

    inner class ViewHolder(itemView : View, override val binding: ChanceOfRainItemLayoutBinding) : BaseViewHolder(itemView) {
        @SuppressLint("SetTextI18n")
        override fun bind(item: ChanceOfPrecipitaion) {
            binding.time.text = item.time
            binding.progress.text = "${item.chance}%"
            binding.progressBar.progress = item.chance.toInt()
        }

    }
}