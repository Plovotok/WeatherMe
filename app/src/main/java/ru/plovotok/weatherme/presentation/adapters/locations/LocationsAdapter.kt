package ru.plovotok.weatherme.presentation.adapters.locations

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.animation.DecelerateInterpolator
import androidx.recyclerview.widget.DiffUtil
import ru.plovotok.weatherme.R
import ru.plovotok.weatherme.databinding.LocationItemLayoutBinding
import ru.plovotok.weatherme.domain.models.LocationResponse
import ru.plovotok.weatherme.presentation.base.BaseAdapter

class LocationsAdapter(private val listener : LocationItemClickListener, private val type : Type) : BaseAdapter<LocationItemLayoutBinding, LocationResponse>() {

    private var isEditing = false

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
            binding.marker.scaleX = 0f
            binding.checkBox.scaleX = 0f
            binding.checkBox.x = -binding.marker.width.toFloat()
            binding.marker.x = -binding.marker.width.toFloat()


            when (isEditing) {
                true -> listener.onListEditing(true)
                false -> listener.onListEditing(false)
            }


            binding.checkBox.setOnCheckedChangeListener { _, isChecked ->
                when(isChecked) {
                    true -> listener.onItemAddToDeleteList(item)
                    false -> listener.onItemRemoveFromDeleteList(item)
                }
            }


            if (isEditing) {
                binding.marker.visibility = View.GONE
                binding.checkBox.visibility = View.VISIBLE
                binding.checkBox.animate()
                    .setInterpolator(DecelerateInterpolator())
                    .scaleX(1f)
                    .translationX(0f)
                    .setDuration(400L).start()
            } else {
                binding.checkBox.visibility = View.GONE
                binding.marker.visibility = View.VISIBLE
                binding.marker.animate()
                    .setInterpolator(DecelerateInterpolator())
                    .scaleX(1f)
                    .translationX(0f)
                    .setDuration(400L).start()
            }
            binding.locationName.text = item.name
            binding.locationRegion.text = item.region
            binding.locationCountry.text = item.country

            binding.addButton.setOnClickListener {
                listener.onItemAdd(item = item)
            }



            when (type) {
                Type.MY_LOCATIONS -> {
                    with(binding) {
                        addButton.visibility = View.GONE

                        root.setOnLongClickListener {
                            isEditing = !isEditing
                            listener.onListEditing(isEditing)
                            notifyDataSetChanged()
                            false
                        }

                        binding.root.setOnClickListener {
                            listener.onItemFavourite(item)
                        }
                    }
                }
                Type.SERVER_LOCATIONS -> {
                    with(binding) {
                        addButton.visibility = View.VISIBLE
                    }
                }
            }
        }

    }

    fun finishEditing() {
        isEditing = false
        notifyDataSetChanged()
    }

    interface LocationItemClickListener {
        fun onItemAdd(item : LocationResponse)
        fun onItemRemove(item: LocationResponse)
        fun onItemFavourite(item: LocationResponse)
        fun onListEditing(isEditing : Boolean)
        fun onItemAddToDeleteList(item : LocationResponse)
        fun onItemRemoveFromDeleteList(item : LocationResponse)
    }

    enum class Type {
        MY_LOCATIONS,
        SERVER_LOCATIONS
    }
}