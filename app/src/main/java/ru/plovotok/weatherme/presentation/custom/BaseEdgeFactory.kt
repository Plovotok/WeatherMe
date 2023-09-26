package ru.plovotok.weatherme.presentation.custom

import android.widget.EdgeEffect
import androidx.recyclerview.widget.RecyclerView
import androidx.viewbinding.ViewBinding
import ru.plovotok.weatherme.presentation.base.BaseAdapter

class BaseEdgeEffectFactory<ItemVB : ViewBinding,Item : Any?>(val isVertical : Boolean? = null) : RecyclerView.EdgeEffectFactory() {

    override fun createEdgeEffect(view: RecyclerView, direction: Int): EdgeEffect =
        object : EdgeEffect(view.context) {

            override fun onPull(deltaDistance: Float) {
                super.onPull(deltaDistance)
                handlePull(deltaDistance)
            }

            override fun onPull(deltaDistance: Float, displacement: Float) {
                super.onPull(deltaDistance, displacement)
                handlePull(deltaDistance)
            }

            override fun onRelease() {
                super.onRelease()
                repeat(view.childCount) {
                    (view.getChildViewHolder(view.getChildAt(it)) as BaseAdapter<ItemVB, Item>.BaseViewHolder).run {
                        translationY.start()
                    }
                }
            }

            override fun onAbsorb(velocity: Int) {
                super.onAbsorb(velocity)
                val sign = if (direction == DIRECTION_BOTTOM) -1 else 1
                val translationVelocity = sign * velocity * FLING_TRANSLATION_MAGNITUDE
                repeat(view.childCount) {
                    (view.getChildViewHolder(view.getChildAt(it)) as BaseAdapter<ItemVB, Item>.BaseViewHolder).run {
                        translationY
                            .setStartVelocity(translationVelocity)
                            .start()
                    }
                }
            }

            private fun handlePull(deltaDistance: Float) {
                val sign = if (direction == DIRECTION_BOTTOM) -1 else 1
                val translationYDelta = sign * view.width * deltaDistance * OVERSCROLL_TRANSLATION_MAGNITUDE
                repeat(view.childCount) {
                    (view.getChildViewHolder(view.getChildAt(it)) as BaseAdapter<ItemVB, Item>.BaseViewHolder).run {
                        translationY.cancel()
                        itemView.translationY += translationYDelta
                    }
                }
            }
        }

    companion object {
        private const val OVERSCROLL_TRANSLATION_MAGNITUDE = 0.1f
        private const val FLING_TRANSLATION_MAGNITUDE = 0.3f
    }
}