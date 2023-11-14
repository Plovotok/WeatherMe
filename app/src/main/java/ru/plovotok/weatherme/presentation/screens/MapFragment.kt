package ru.plovotok.weatherme.presentation.screens

import android.Manifest
import android.annotation.SuppressLint
import android.content.pm.PackageManager
import android.os.Bundle
import android.view.View
import android.webkit.GeolocationPermissions
import android.webkit.WebChromeClient
import android.widget.AdapterView
import android.widget.ArrayAdapter
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.navigation.fragment.findNavController
import ru.plovotok.weatherme.R
import ru.plovotok.weatherme.databinding.FragmentMapBinding
import ru.plovotok.weatherme.presentation.base.BaseFragment


class MapFragment : BaseFragment<FragmentMapBinding>() {

    override fun onResume() {
        super.onResume()
        val layers = arrayOf(
            resources.getString(R.string.wind_layer),
            resources.getString(R.string.temp_layer),
            resources.getString(R.string.precip_layer),
        )

        val arrayAdapter = ArrayAdapter(requireContext(), R.layout.pop_up_menu_item, layers)
        binding.toolbar.filledExposedDropdown.setAdapter(arrayAdapter)
        binding.toolbar.filledExposedDropdown.setText(layers[0], false)
        binding.toolbar.filledExposedDropdown.onItemClickListener =
            AdapterView.OnItemClickListener { _, _, position, _ ->
                when(position) {
                    0 -> {binding.webView.loadUrl("file:///android_asset/wind.html")}
                    1 -> {binding.webView.loadUrl("file:///android_asset/temperature.html")}
                    2 -> {binding.webView.loadUrl("file:///android_asset/precipitation.html")}
                    else ->{}
                }
            }
    }

    @SuppressLint("SetJavaScriptEnabled")
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val settings = binding.webView.settings
        settings.builtInZoomControls = true
        settings.javaScriptEnabled = true
        settings.setGeolocationEnabled(true)
        settings.allowContentAccess = true
        settings.domStorageEnabled = true
        settings.databaseEnabled = true

        binding.toolbar.backButton.setOnClickListener {
            findNavController().popBackStack()
        }
        binding.toolbar.backButton.visibility = View.VISIBLE

        binding.webView.webChromeClient = object : WebChromeClient() {
            override fun onGeolocationPermissionsShowPrompt(origin: String?, callback: GeolocationPermissions.Callback?) {
                // Check if the necessary permissions are granted
                if (ContextCompat.checkSelfPermission(
                        requireActivity(),
                        Manifest.permission.ACCESS_FINE_LOCATION
                    ) == PackageManager.PERMISSION_GRANTED
                    && ContextCompat.checkSelfPermission(
                        requireActivity(),
                        Manifest.permission.ACCESS_COARSE_LOCATION
                    ) == PackageManager.PERMISSION_GRANTED) {
                    // Grant the geolocation permission
                    callback?.invoke(origin, true, false)
                } else {
                    // Request the necessary permissions
                    ActivityCompat.requestPermissions(
                        requireActivity(),
                        arrayOf(
                            Manifest.permission.ACCESS_FINE_LOCATION,
                            Manifest.permission.ACCESS_COARSE_LOCATION),
                        1)
                }
            }
        }

        binding.webView.loadUrl("file:///android_asset/wind.html")
    }

    @Deprecated("Deprecated in Java")
    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        if (requestCode == 1001) {
            if (grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                binding.webView.reload()
            }
        }
    }

    override fun getViewBinding(): FragmentMapBinding {
        return FragmentMapBinding.inflate(layoutInflater)
    }


}