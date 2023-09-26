package ru.plovotok.weatherme.presentation.base

import android.view.View
import android.view.ViewGroup
import androidx.dynamicanimation.animation.SpringAnimation
import androidx.dynamicanimation.animation.SpringForce
import androidx.recyclerview.widget.RecyclerView
import androidx.viewbinding.ViewBinding

abstract class BaseAdapter <ItemVB: ViewBinding, Item: Any?> (
    private var items: List<Item> = emptyList()
): RecyclerView.Adapter<BaseAdapter<ItemVB, Item>.BaseViewHolder>() {

    abstract fun createViewHolder(parent: ViewGroup): BaseViewHolder

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): BaseViewHolder {
        return createViewHolder(parent)
    }

    override fun onBindViewHolder(holder: BaseViewHolder, position: Int) {
        holder.bind(items[position])
    }

    fun loadItems(items: List<Item>) {
        this.items = items
    }

    fun addItems(newItems : List<Item>) {
        val oldItems = this.items
        this.items = oldItems + newItems
    }

    fun getItems() = items

    override fun getItemCount(): Int = getItems().size

    abstract inner class BaseViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

        val translationY: SpringAnimation = SpringAnimation(itemView, SpringAnimation.TRANSLATION_Y)
            .setSpring(
                SpringForce()
                    .setFinalPosition(0f)
                    .setDampingRatio(SpringForce.DAMPING_RATIO_MEDIUM_BOUNCY)
                    .setStiffness(SpringForce.STIFFNESS_LOW)
            )

        val translationX: SpringAnimation = SpringAnimation(itemView, SpringAnimation.TRANSLATION_X)
            .setSpring(
                SpringForce()
                    .setFinalPosition(0f)
                    .setDampingRatio(SpringForce.DAMPING_RATIO_MEDIUM_BOUNCY)
                    .setStiffness(SpringForce.STIFFNESS_LOW)
            )
        abstract val binding: ItemVB
        abstract fun bind(item: Item)
    }
}