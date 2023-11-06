package ru.plovotok.weatherme.presentation.screens

import android.Manifest
import android.annotation.SuppressLint
import android.content.pm.PackageManager
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.webkit.GeolocationPermissions
import android.webkit.WebChromeClient
import android.widget.AdapterView
import android.widget.ArrayAdapter
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.navigation.fragment.findNavController
import com.mapbox.mapboxsdk.Mapbox
import ru.plovotok.weatherme.R
import ru.plovotok.weatherme.databinding.FragmentMapBinding
import ru.plovotok.weatherme.presentation.base.BaseFragment


class MapFragment : BaseFragment<FragmentMapBinding>() {

//    https://www.maptiler.com/tools/weather/?_gl=1*1o1ldu4*_gcl_au*MTg2Njk3NDU3MS4xNjk5MDA0Njc5*_ga*MTU5NDUzNDM5OS4xNjk5MDA0NjQw*_ga_K4SXYBF4HT*MTY5OTAwNDY3OS4xLjEuMTY5OTAwNzU2NC41MC4wLjA.&_ga=2.61845742.37548981.1699004641-1594534399.1699004640#3/48.79/26.46

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Init MapLibre
        Mapbox.getInstance(requireActivity())
        return super.onCreateView(inflater, container, savedInstanceState)
    }

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

        val key = "fXim7Ni65DIFdKO7LlcC"

        // Find other maps in https://cloud.maptiler.com/maps/
        val mapId = "streets-v2"

//        val styleUrl = "https://api.maptiler.com/maps/$mapId/style.json?key=$key"
//        val styleUrl = "https://api.maptiler.com/maps/satellite/style.json?key=$key"
        val styleUrl = "https://api.maptiler.com/maps/1a9b3721-ac2a-4090-98d4-1dc723f196b2/style.json?key=$key"

        val settings = binding.webView.settings
        settings.builtInZoomControls = true
        settings.javaScriptEnabled = true
        settings.setGeolocationEnabled(true)
        settings.allowContentAccess = true
        settings.domStorageEnabled = true
        settings.databaseEnabled = true
//        binding.webView.webViewClient = object : WebViewClient() {
//            override fun shouldOverrideUrlLoading(view: WebView?, url: String?): Boolean {
////                return super.shouldOverrideUrlLoading(view, url)
//                return false
//            }
//        }


        binding.toolbar.backButton.setOnClickListener {
            findNavController().popBackStack()
        }
        binding.toolbar.backButton.visibility = View.VISIBLE

        binding.webView.webChromeClient = object : WebChromeClient() {
            override fun onGeolocationPermissionsShowPrompt(origin: String?, callback: GeolocationPermissions.Callback?) {
                // Check if the necessary permissions are granted
                if (ContextCompat.checkSelfPermission(requireActivity(), Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED
                    && ContextCompat.checkSelfPermission(requireActivity(), Manifest.permission.ACCESS_COARSE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
                    // Grant the geolocation permission
                    callback?.invoke(origin, true, false)
                } else {
                    // Request the necessary permissions
                    ActivityCompat.requestPermissions(requireActivity(), arrayOf(Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_COARSE_LOCATION), 1)
                }
            }
        }

//        binding.webView.loadUrl("file:///android_asset/index.html")
//        binding.webView.loadUrl(styleUrl)
        binding.webView.loadUrl("file:///android_asset/wind.html")

//        binding.mapView.getMapAsync { map ->
//            map.setStyle(styleUrl)
//            map.addMarker(MarkerOptions().setTitle("Me").setPosition(LatLng(latitude = 57.0, longitude = 57.2)))
//            map.cameraPosition = CameraPosition.Builder().target(LatLng(0.0, 0.0)).zoom(1.0).build()
//        }
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

    override fun onStart() {
        super.onStart()
//        binding.mapView.onStart()
    }

    override fun onStop() {
        super.onStop()
//        binding.mapView.onStop()
    }

    override fun onDestroy() {
//        binding.mapView.onDestroy()
        super.onDestroy()

    }

    override fun getViewBinding(): FragmentMapBinding {
        return FragmentMapBinding.inflate(layoutInflater)
    }


}