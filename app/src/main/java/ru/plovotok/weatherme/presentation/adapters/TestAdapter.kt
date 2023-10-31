package ru.plovotok.weatherme.presentation.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import ru.plovotok.weatherme.R
import ru.plovotok.weatherme.databinding.TestLayoutExpandableBinding
import ru.plovotok.weatherme.domain.models.LocationResponse
import ru.plovotok.weatherme.presentation.adapters.locations.BaseExpandableAdapter

class TestAdapter : BaseExpandableAdapter<TestLayoutExpandableBinding, LocationResponse>(object : DiffUtil.ItemCallback<LocationResponse>(){
    override fun areItemsTheSame(oldItem: LocationResponse, newItem: LocationResponse): Boolean {
        return oldItem == newItem
    }

    override fun areContentsTheSame(oldItem: LocationResponse, newItem: LocationResponse): Boolean {
        return oldItem == newItem
    }

}) {
    override fun createViewHolder(parent: ViewGroup): BaseExpandableViewHolder {

        val view = LayoutInflater.from(parent.context).inflate(R.layout.test_layout_expandable, parent, false)
        val expandableView: View = view.findViewById(R.id.expandable_view) as View
        return ViewHolder(view, TestLayoutExpandableBinding.bind(view), expandableView, null)
    }

    inner class ViewHolder(itemView : View,
                           override val binding: TestLayoutExpandableBinding,
                           override val expandableView: View,
                           override val indicatorView: View?
    ) : BaseExpandableViewHolder(itemView) {
        override fun bind(item: LocationResponse) {
            binding.locationName.text = item.name
            binding.locationRegion.text = item.region
            binding.locationCountry.text = item.country
        }
    }
}

class NewTestAdapter : BaseListAdapter<TestLayoutExpandableBinding, LocationResponse>(
    object : DiffUtil.ItemCallback<LocationResponse>() {
        override fun areItemsTheSame(
            oldItem: LocationResponse,
            newItem: LocationResponse
        ): Boolean {
            return oldItem == newItem
        }

        override fun areContentsTheSame(
            oldItem: LocationResponse,
            newItem: LocationResponse
        ): Boolean {
            return oldItem == newItem
        }

    }
) {
    override fun createViewHolder(parent: ViewGroup): ListViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.test_layout_expandable, parent, false)
        return ViewHolder(view, TestLayoutExpandableBinding.bind(view))
    }

    inner class ViewHolder(itemView : View, override val binding: TestLayoutExpandableBinding) : ListViewHolder(itemView) {
        override fun bind(item: LocationResponse) {
            binding.locationName.text = item.name
            binding.locationRegion.text = item.region
            binding.locationCountry.text = item.country
        }
    }

}