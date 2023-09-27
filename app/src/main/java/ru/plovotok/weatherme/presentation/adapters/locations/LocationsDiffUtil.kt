package ru.plovotok.weatherme.presentation.adapters.locations

import androidx.recyclerview.widget.DiffUtil
import ru.plovotok.weatherme.domain.models.LocationResponse

class LocationsDiffUtil(val oldList : List<LocationResponse>, val newList: List<LocationResponse>) : DiffUtil.Callback() {
    override fun getOldListSize(): Int {
        return oldList.size
    }

    override fun getNewListSize(): Int {
        return newList.size
    }

    override fun areItemsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean {
        return oldList[oldItemPosition] == newList[newItemPosition]
    }

    override fun areContentsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean {
        return oldList[oldItemPosition] == newList[newItemPosition]
    }
}