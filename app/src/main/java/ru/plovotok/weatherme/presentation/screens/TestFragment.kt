package ru.plovotok.weatherme.presentation.screens

import android.os.Bundle
import android.view.View
import androidx.navigation.fragment.findNavController
import ru.plovotok.weatherme.databinding.FragmentTestBinding
import ru.plovotok.weatherme.presentation.base.BaseFragment


class TestFragment : BaseFragment<FragmentTestBinding>() {

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.backButton.visibility = View.VISIBLE
        binding.backButton.setOnClickListener {
            findNavController().popBackStack()
        }
        (requireActivity() as WeatherActivity).setSupportActionBar(binding.toolbar)
    }

    override fun getViewBinding(): FragmentTestBinding {
        return FragmentTestBinding.inflate(layoutInflater)
    }


}