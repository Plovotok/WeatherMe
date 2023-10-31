package ru.plovotok.weatherme.presentation.adapters.locations

import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import androidx.viewbinding.ViewBinding

abstract class BaseExpandableAdapter <ItemVB: ViewBinding, Item: Any?> (

    private val callback : DiffUtil.ItemCallback<Item>
): ListAdapter<Item, BaseExpandableAdapter<ItemVB, Item>.BaseExpandableViewHolder>(callback) {
    private var items: List<Item> = emptyList()
    private var expandedPositions = mutableListOf<Boolean>()

    abstract fun createViewHolder(parent: ViewGroup): BaseExpandableViewHolder

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): BaseExpandableViewHolder {
        return createViewHolder(parent)
    }

    override fun onBindViewHolder(holder: BaseExpandableViewHolder, position: Int) {
        holder.bind(items[position])
        if (expandedPositions[position]) {
            holder.expandableView.visibility = View.VISIBLE
        } else {
            holder.expandableView.visibility = View.GONE
        }
    }

    override fun submitList(list: MutableList<Item>?) {
        this.items = list?.toList() ?: return
        expandedPositions = MutableList(items.size) {
            false
        }
        super.submitList(list)
    }

//    open fun loadItems(items: List<Item>) {
//        this.items = items
//        expandedPositions = MutableList(items.size) {
//            false
//        }
//    }
//
//    fun addItems(newItems : List<Item>) {
//        val oldItems = this.items
//        this.items = oldItems + newItems
//        expandedPositions = MutableList(items.size) {
//            false
//        }
//    }

    fun getItems() = items

    override fun getItemCount(): Int = getItems().size

    abstract inner class BaseExpandableViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        abstract val binding: ItemVB
        abstract val expandableView : View
        //        Если есть View, которая будет показывать развернут или нет элемент
        abstract val indicatorView : View?

        init {
            itemView.setOnClickListener {
                toggleDescription()
            }
        }

        private fun toggleDescription() {

            indicatorView?.animate()?.apply {
                duration = 150
                rotation(180f)
            }
            expandedPositions[bindingAdapterPosition] = !expandedPositions[bindingAdapterPosition]
            notifyItemChanged(bindingAdapterPosition)
        }

        abstract fun bind(item: Item)
    }
}