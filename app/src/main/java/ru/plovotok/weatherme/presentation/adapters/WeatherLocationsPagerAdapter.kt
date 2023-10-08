package ru.plovotok.weatherme.presentation.adapters

import androidx.fragment.app.FragmentActivity
import androidx.viewpager2.adapter.FragmentStateAdapter
import ru.plovotok.weatherme.presentation.WeatherFragment

class WeatherLocationsPagerAdapter(private val locationsList: List<String?>, fragmentActivity: FragmentActivity) : FragmentStateAdapter(fragmentActivity) {

    override fun getItemCount(): Int {
        return locationsList.size
    }

    override fun createFragment(position: Int): WeatherFragment {
        return WeatherFragment(locationsList[position])
//        return WeatherFragment.newInstance(locationsList[position])
    }
}