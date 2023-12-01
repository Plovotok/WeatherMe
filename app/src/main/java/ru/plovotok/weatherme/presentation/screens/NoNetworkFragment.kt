package ru.plovotok.weatherme.presentation.screens

import android.net.ConnectivityManager
import android.net.NetworkCapabilities
import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import ru.plovotok.weatherme.R
import ru.plovotok.weatherme.databinding.FragmentNoNetworkBinding
import ru.plovotok.weatherme.presentation.base.BaseBottomSheetDialogFragment

class NoNetworkFragment : BaseBottomSheetDialogFragment<FragmentNoNetworkBinding>(isDraggable = false) {

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        with(binding) {
            checkNetworkButton.button.text = getString(R.string.check)
            checkNetworkButton.button.setOnClickListener {

                val connectivityManager =
                    requireActivity().getSystemService(AppCompatActivity.CONNECTIVITY_SERVICE) as ConnectivityManager

                var result = false
                connectivityManager.getNetworkCapabilities(connectivityManager.activeNetwork)?.run {
                    result = when {
                        hasTransport(NetworkCapabilities.TRANSPORT_WIFI) -> true
                        hasTransport(NetworkCapabilities.TRANSPORT_CELLULAR) -> true
                        hasTransport(NetworkCapabilities.TRANSPORT_ETHERNET) -> true
                        else -> false
                    }
                }

                if (result)
                    dismiss()
                else
                    showToast(text = getString(R.string.require_internet))
            }

            continueButton.button.background = ContextCompat.getDrawable(requireContext(), R.drawable.gray_button_background)
            continueButton.button.text = resources.getString(R.string.continue_without_internet)

            continueButton.button.setOnClickListener {
                dismiss()
            }

            noNetworkLottie.playAnimation()
        }
    }

    override fun onDestroy() {
        binding.noNetworkLottie.cancelAnimation()
        super.onDestroy()
    }

    override fun getViewBinding(): FragmentNoNetworkBinding {
        return FragmentNoNetworkBinding.inflate(layoutInflater)
    }
}