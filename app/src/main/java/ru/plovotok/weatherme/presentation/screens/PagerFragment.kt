package ru.plovotok.weatherme.presentation.screens

import android.os.Bundle
import android.view.View
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch
import ru.plovotok.weatherme.databinding.FragmentPagerBinding
import ru.plovotok.weatherme.presentation.adapters.PagerAdapter
import ru.plovotok.weatherme.presentation.base.BaseFragment
import ru.plovotok.weatherme.presentation.base.UIState

@AndroidEntryPoint
class PagerFragment : BaseFragment<FragmentPagerBinding>() {

    private val viewModel : PagerViewModel by viewModels()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewModel.getLocationsList()

        collectLocations()
    }

    override fun getViewBinding(): FragmentPagerBinding {
        return FragmentPagerBinding.inflate(layoutInflater)
    }

    private fun collectLocations() = viewLifecycleOwner.lifecycleScope.launch {
        viewModel.myLocations.collect { state ->
            when(state) {
                is UIState.Success -> {
                    val list = state.data?.map { "${it.lat},${it.lon}" } ?: listOf()
                    val adapter = PagerAdapter(list = list, fragmentActivity = requireActivity())
                    binding.vPager.adapter = adapter
                }
                is UIState.Error -> {
                    val list = listOf<String?>(null)
                    val adapter = PagerAdapter(list = list, fragmentActivity = requireActivity())
                    binding.vPager.adapter = adapter
                }
                else -> {}
            }
        }
    }

}