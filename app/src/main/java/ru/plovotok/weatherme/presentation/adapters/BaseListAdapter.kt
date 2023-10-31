package ru.plovotok.weatherme.presentation.adapters

import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import androidx.viewbinding.ViewBinding
import ru.plovotok.weatherme.databinding.TestLayoutExpandableBinding

abstract class BaseListAdapter<VB : ViewBinding, T>(
    private val callback : DiffUtil.ItemCallback<T>
) : ListAdapter<T, BaseListAdapter<VB, T>.ListViewHolder>(
    callback
) {

    private var items : MutableList<T> = mutableListOf()

    fun loadItems(items : List<T>) {
        submitList(items.toMutableList())
    }

    override fun submitList(list: MutableList<T>?) {
        this.items = list?.toMutableList() ?: return
        super.submitList(list)
    }

    abstract inner class ListViewHolder(itemView : View) : RecyclerView.ViewHolder(itemView) {
        abstract val binding : TestLayoutExpandableBinding
        abstract fun bind(item : T)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ListViewHolder {
        return createViewHolder(parent)
    }

    abstract fun createViewHolder(parent: ViewGroup) : ListViewHolder

    override fun onBindViewHolder(holder: ListViewHolder, position: Int) {
        holder.bind(items[position])
    }

}