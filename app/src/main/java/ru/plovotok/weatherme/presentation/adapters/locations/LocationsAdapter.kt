package ru.plovotok.weatherme.presentation.adapters.locations

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import ru.plovotok.weatherme.R
import ru.plovotok.weatherme.databinding.LocationItemLayoutBinding
import ru.plovotok.weatherme.domain.models.LocationResponse
import ru.plovotok.weatherme.presentation.base.BaseAdapter

class LocationsAdapter(private val listener : LocationItemClickListener, private val type : Type) : BaseAdapter<LocationItemLayoutBinding, LocationResponse>() {
    override fun createViewHolder(parent: ViewGroup): BaseViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.location_item_layout, parent, false)
        return ViewHolder(view, LocationItemLayoutBinding.bind(view))
    }

    override fun difLoadItems(items: List<LocationResponse>) {
        val diffUtil = LocationsDiffUtil(getItems(), items)
        val diffResults = DiffUtil.calculateDiff(diffUtil)
        loadItems(items)
        diffResults.dispatchUpdatesTo(this)
    }

    inner class ViewHolder(itemView : View, override val binding: LocationItemLayoutBinding) : BaseViewHolder(itemView) {
        override fun bind(item: LocationResponse) {
            binding.locationName.text = item.name
            binding.locationRegion.text = item.region
            binding.locationCountry.text = item.country

            binding.addButton.setOnClickListener {
                listener.onItemAdd(item = item)
            }

            binding.removeButton.setOnClickListener {
                listener.onItemRemove(item = item)
            }

            when (type) {
                Type.MY_LOCATIONS -> {
                    with(binding) {
                        removeButton.visibility = View.VISIBLE
                        addButton.visibility = View.GONE
                    }
                }
                Type.SERVER_LOCATIONS -> {
                    with(binding) {
                        removeButton.visibility = View.GONE
                        addButton.visibility = View.VISIBLE
                    }
                }
            }
        }

    }

    interface LocationItemClickListener {
        fun onItemAdd(item : LocationResponse)
        fun onItemRemove(item: LocationResponse)
    }

    enum class Type {
        MY_LOCATIONS,
        SERVER_LOCATIONS
    }
}