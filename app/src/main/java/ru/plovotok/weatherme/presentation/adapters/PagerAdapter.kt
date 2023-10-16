package ru.plovotok.weatherme.presentation.adapters

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import androidx.viewpager2.adapter.FragmentStateAdapter
import ru.plovotok.weatherme.presentation.screens.WeatherFragment

class PagerAdapter(private val list: List<String?>, fragmentActivity : FragmentActivity) : FragmentStateAdapter(fragmentActivity) {

    override fun getItemCount(): Int {
        return list.size
    }

    override fun createFragment(position: Int): Fragment {
        return WeatherFragment.newInstance(location = list[position])
    }
}