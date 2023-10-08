package ru.plovotok.weatherme.presentation

import android.os.Bundle
import android.view.View
import androidx.lifecycle.lifecycleScope
import kotlinx.coroutines.launch
import ru.plovotok.weatherme.databinding.FragmentCommonWeatherBinding
import ru.plovotok.weatherme.presentation.adapters.WeatherLocationsPagerAdapter
import ru.plovotok.weatherme.presentation.base.BaseFragment
import ru.plovotok.weatherme.presentation.base.UIState


class CommonWeatherFragment : BaseFragment<FragmentCommonWeatherBinding>() {

    private val viewModel : WeatherLocationsViewModel by lazy {
        WeatherLocationsViewModel((requireActivity().application as WeatherApplication).repository)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        viewModel.getLocationsList()
        collectWeatherList()
    }

    override fun getViewBinding(): FragmentCommonWeatherBinding {
        return FragmentCommonWeatherBinding.inflate(layoutInflater)
    }

    private fun collectWeatherList() = viewLifecycleOwner.lifecycleScope.launch {
        viewModel.myLocations.collect{ state ->
            when(state) {
                is UIState.Success -> {
                    val locationsAdapter = WeatherLocationsPagerAdapter(
                        locationsList = state.data ?: listOf(),
                        this@CommonWeatherFragment.requireActivity()
                    )
                    binding.weatherPager.adapter = locationsAdapter


                }
                else -> {}
            }
        }
    }

}