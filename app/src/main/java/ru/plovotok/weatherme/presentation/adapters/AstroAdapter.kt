package ru.plovotok.weatherme.presentation.adapters

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import ru.plovotok.weatherme.R
import ru.plovotok.weatherme.databinding.SunStateItemLayoutBinding
import ru.plovotok.weatherme.presentation.base.BaseAdapter
import ru.plovotok.weatherme.presentation.base.viewhelperclasses.AstroInfo
import ru.plovotok.weatherme.presentation.base.viewhelperclasses.AstroType

class AstroAdapter : BaseAdapter<SunStateItemLayoutBinding, AstroInfo>() {
    override fun createViewHolder(parent: ViewGroup): BaseViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.sun_state_item_layout, parent, false)
        return ViewHolder(view, SunStateItemLayoutBinding.bind(view))
    }

    inner class ViewHolder(itemView : View, override val binding: SunStateItemLayoutBinding) : BaseViewHolder(itemView) {
        @SuppressLint("SetTextI18n")
        override fun bind(item: AstroInfo) {

            with(binding.weatherInfoIcon) {
                when(item.type) {
                    AstroType.SUNRISE -> {
                        binding.name.text = resources.getString(R.string.sunrise)
                        setImageResource(R.drawable.sunrise)
                    }
                    AstroType.SUNSET -> {
                        binding.name.text = resources.getString(R.string.sunset)
                        setImageResource(R.drawable.sunset)
                    }
                }
            }

            binding.value.text = item.time
        }

    }
}