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
//            binding.favouriteButton.x = binding.root.width.toFloat()

            val horizontalPadding = binding.root.paddingRight.toFloat()

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


            binding.root.setOnLongClickListener(object : View.OnLongClickListener {
                override fun onLongClick(v: View?): Boolean {
                    isEditing = !isEditing
                    listener.onListEditing(isEditing)
                    notifyDataSetChanged()
                    return false
                }
            })

            if (isEditing) {
                binding.marker.visibility = View.GONE
//                binding.favouriteButton.visibility = View.GONE
                binding.checkBox.visibility = View.VISIBLE
                binding.checkBox.animate()
                    .setInterpolator(DecelerateInterpolator())
                    .scaleX(1f)
                    .translationX(0f)
                    .setDuration(900L).start()
                binding.favouriteButton.animate()
                    .setInterpolator(DecelerateInterpolator())
                    .translationX(binding.favouriteButton.width.toFloat() + horizontalPadding)
                    .withEndAction {
                        binding.favouriteButton.visibility = View.GONE
                    }
                    .setDuration(900L).start()
            } else {
                binding.checkBox.visibility = View.GONE
                binding.favouriteButton.visibility = View.VISIBLE
                binding.marker.visibility = View.VISIBLE
                binding.marker.animate()
                    .setInterpolator(DecelerateInterpolator())
                    .scaleX(1f)
                    .translationX(0f)
                    .setDuration(900L).start()
                binding.favouriteButton.animate()
                    .setInterpolator(DecelerateInterpolator())
                    .translationX(0f)
                    .setDuration(900L).start()
            }
            binding.locationName.text = item.name
            binding.locationRegion.text = item.region
            binding.locationCountry.text = item.country

            binding.addButton.setOnClickListener {
                listener.onItemAdd(item = item)
            }

            binding.removeButton.setOnClickListener {
//                listener.onItemRemove(item = item)
            }

            binding.favouriteButton.setOnClickListener {
                listener.onItemFavourite(item = item)
            }


            when (type) {
                Type.MY_LOCATIONS -> {
                    with(binding) {
                        removeButton.visibility = View.GONE
                        addButton.visibility = View.GONE
//                        favouriteButton.visibility = View.VISIBLE
                    }
                }
                Type.SERVER_LOCATIONS -> {
                    with(binding) {
                        removeButton.visibility = View.GONE
                        addButton.visibility = View.VISIBLE
                        favouriteButton.visibility = View.GONE
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