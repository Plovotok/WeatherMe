package ru.plovotok.weatherme.presentation.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import ru.plovotok.weatherme.R
import ru.plovotok.weatherme.data.models.LocationResponseDTO
import ru.plovotok.weatherme.databinding.LocationItemLayoutBinding
import ru.plovotok.weatherme.presentation.base.BaseAdapter

class LocationsAdapter(private val listener : LocationItemClickListener) : BaseAdapter<LocationItemLayoutBinding, LocationResponseDTO>() {
    override fun createViewHolder(parent: ViewGroup): BaseViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.location_item_layout, parent, false)
        return ViewHolder(view, LocationItemLayoutBinding.bind(view))
    }

    inner class ViewHolder(itemView : View, override val binding: LocationItemLayoutBinding) : BaseViewHolder(itemView) {
        override fun bind(item: LocationResponseDTO) {
            binding.locationName.text = item.name
            binding.locationRegion.text = item.region
            binding.locationCountry.text = item.country

            binding.addButton.setOnClickListener {
                listener.onItemClick(item = item)
            }
        }

    }

    interface LocationItemClickListener {
        fun onItemClick(item : LocationResponseDTO)
    }
}