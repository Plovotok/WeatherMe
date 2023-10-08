package ru.plovotok.weatherme.presentation

import android.annotation.SuppressLint
import android.util.Log
import androidx.core.content.ContextCompat
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import kotlinx.coroutines.launch
import ru.plovotok.weatherme.R
import ru.plovotok.weatherme.databinding.ChanceOfRainItemLayoutBinding
import ru.plovotok.weatherme.databinding.FragmentWeatherBinding
import ru.plovotok.weatherme.presentation.adapters.AstroAdapter
import ru.plovotok.weatherme.presentation.adapters.ChancesAdapter
import ru.plovotok.weatherme.presentation.adapters.HourlyForecastAdapter
import ru.plovotok.weatherme.presentation.adapters.WeatherInfoAdapter
import ru.plovotok.weatherme.presentation.base.BaseFragment
import ru.plovotok.weatherme.presentation.base.PrecipRate
import ru.plovotok.weatherme.presentation.base.TypeOfPrecip
import ru.plovotok.weatherme.presentation.base.UIState
import ru.plovotok.weatherme.presentation.base.defineWeatherByCondition
import ru.plovotok.weatherme.presentation.base.viewhelperclasses.ChanceOfPrecipitaion
import ru.plovotok.weatherme.presentation.custom.BaseEdgeEffectFactory


class WeatherFragment(private val location : String? = null) : BaseFragment<FragmentWeatherBinding>() {

    private val viewModel : WeatherViewModel by viewModels()

    private val weatherInfoAdapter = WeatherInfoAdapter()
    private lateinit var hourAdapter : HourlyForecastAdapter
    private val chancesAdapter = ChancesAdapter()
    private val sunStateAdapter = AstroAdapter()
    private var currentCond = 0
    private var nowIsDay : Int? = null

    @SuppressLint("ClickableViewAccessibility")
    override fun onResume() {
        Log.i("Weather-Fragment", "${location} onResume()")
        super.onResume()
//        super.onViewCreated(view, savedInstanceState)
        viewModel.getWeather(location)

        binding.rootScroll.setColorSchemeColors(ContextCompat.getColor(requireContext(), R.color.agree_button_color))

        setToolbar(
            toolbar = binding.toolbar,
            title = "Moscow, Russia",
            backButtonEnabled = false,
            addButtonEnabled = true,
            switchEnabled = false)
        binding.toolbar.addButton.setOnClickListener {
            if (nowIsDay == null) return@setOnClickListener
            val nowWeather = defineWeatherByCondition(iconCode = currentCond, isDay = nowIsDay!!)

            try {
                (requireActivity() as WeatherActivity).setTimeImage(nowWeather.backgroundResource, TypeOfPrecip.CLEAR, PrecipRate.CLEAR)
                findNavController().navigate(R.id.action_weatherFragment_to_addLocationFragment)
            } catch (ex : Exception) {
                showSnack("Вы уже на этом фрагменте, а это костыль, вот так вот")
            }
        }
        binding.toolbar.testButton.setOnClickListener {
            try {
                findNavController().navigate(R.id.action_weatherFragment_to_commonWeatherFragment)
            } catch (ex : Exception) {
                showSnack("Вы уже на этом фрагменте, а это костыль, вот так вот")
            }

        }
        binding.toolbar.switch1.setOnCheckedChangeListener { _, isChecked ->
            if (!isChecked) {
//                (requireActivity() as WeatherActivity).setTimeImage(R.drawable.day_time)

//                (requireActivity() as WeatherActivity).setTimeImage(R.drawable.sun_background)
//                (requireActivity() as WeatherActivity).setTimeImage(defineWeatherIconID(iconCode = currentCond, isDay = 1).backgroundResource)
            }  else {
//                (requireActivity() as WeatherActivity).setTimeImage(R.drawable.night_forest)
//                (requireActivity() as WeatherActivity).setTimeImage(R.drawable.starry_night_background)
//                currentCond = 1003
//                (requireActivity() as WeatherActivity).setTimeImage(defineWeatherIconID(iconCode = currentCond, isDay = 0).backgroundResource)
            }
        }

        with(binding.weatherInfoRv) {
            adapter = weatherInfoAdapter
        }

        hourAdapter = HourlyForecastAdapter()
        with(binding.hourlyForecastRv) {
            adapter = hourAdapter
            layoutManager = LinearLayoutManager(requireContext(), RecyclerView.HORIZONTAL,false )
        }

        with(binding.rainChanceRv) {
            adapter = chancesAdapter
            layoutManager = LinearLayoutManager(requireContext(), LinearLayoutManager.VERTICAL, false)
        }

        with(binding.astroInfoRv) {
            adapter = sunStateAdapter
        }

//        OverScrollDecoratorHelper.setUpOverScroll(binding.rootScroll)
//        OverScrollDecoratorHelper.setUpOverScroll(binding.hourlyForecastRv, OverScrollDecoratorHelper.ORIENTATION_HORIZONTAL)
//        OverScrollDecoratorHelper.setUpOverScroll(binding.rainChanceRv, OverScrollDecoratorHelper.ORIENTATION_VERTICAL)
//        VerticalOverScrollBounceEffectDecorator(RecyclerViewOverScrollDecorAdapter(binding.rainChanceRv))
//        VerticalOverScrollBounceEffectDecorator(ScrollViewOverScrollDecorAdapter(binding.rootScroll))

        binding.rainChanceRv.edgeEffectFactory = BaseEdgeEffectFactory<ChanceOfRainItemLayoutBinding, ChanceOfPrecipitaion>()
        binding.rootScroll.setOnRefreshListener(object : SwipeRefreshLayout.OnRefreshListener {
            override fun onRefresh() {
                viewModel.getWeather(location)
            }

        })


        collectSunState()
        collectWeather()
        collectRainChances()
        collectHourForecast()
        collectHeaderInfo()
    }

//    override fun onResume() {
//        super.onResume()
//
//
//    }

    override fun onPause() {
        super.onPause()
        Log.i("Weather-Fragment", "${location} onPause()")
    }

    override fun onStop() {
        super.onStop()
        Log.i("Weather-Fragment", "${location} onStop()")
    }

    private fun collectWeather() = viewLifecycleOwner.lifecycleScope.launch {
        viewModel.weatherInfo.collect { state ->
            when(state) {
                is UIState.Success -> {
                    state.data?.let { weatherInfoAdapter.loadItems(items = it) }
                    weatherInfoAdapter.notifyDataSetChanged()
                }
                else -> {}
            }
        }

    }

    private fun collectHourForecast() = viewLifecycleOwner.lifecycleScope.launch {
        viewModel.hourlyForecast.collect { state ->
            when(state) {
                is UIState.Success -> {
                    state.data?.let { hourAdapter.loadItems(items = it) }
                    hourAdapter.notifyDataSetChanged()
                }
                else -> {}
            }
        }
    }

    private fun collectRainChances() = viewLifecycleOwner.lifecycleScope.launch {
        viewModel.precipitationChances.collect { state ->
            when(state) {
                is UIState.Success -> {
                    state.data?.let { chancesAdapter.loadItems(items = it) }
                    chancesAdapter.notifyDataSetChanged()
                }
                else -> {}
            }
        }
    }

    private fun collectSunState() = viewLifecycleOwner.lifecycleScope.launch {
        viewModel.astroInfo.collect { state ->
            when(state) {
                is UIState.Success -> {
                    state.data?.let { sunStateAdapter.loadItems(items = it) }
                    sunStateAdapter.notifyDataSetChanged()
                }
                else -> {}
            }
        }
    }

    private fun collectHeaderInfo() = viewLifecycleOwner.lifecycleScope.launch {
        viewModel.headerInfo.collect { state ->
            when(state) {
                is UIState.Success -> {
                    hideLoading()
                    binding.rootScroll.isRefreshing = false
                    val headerInfo = state.data
                    binding.toolbar.titleTextView.text = headerInfo?.location
                    with(binding.head) {
                        avgTemp.text = "${headerInfo?.currentTemp}°"
                        feelsLike.text ="Feels like ${headerInfo?.feelsLike}°"
                        weatherDesc.text = headerInfo?.condition?.text
                        if (headerInfo != null) {
                            nowIsDay = headerInfo.isDay
                            val screenWeather = defineWeatherByCondition(iconCode = headerInfo.condition!!.code, isDay = headerInfo.isDay)

                            weatherIcon.setImageResource(screenWeather.iconResource)
                            (requireActivity() as WeatherActivity).setTimeImage(screenWeather.backgroundResource, screenWeather.precipType, screenWeather.precipRate)
                            currentCond = headerInfo.condition.code
                        }
                        minTemp.text = "night: ${headerInfo?.minTemp}°"
                        maxTemp.text = "day: ${headerInfo?.maxTemp}°"
                        date.text = headerInfo?.time
                    }

                }
                is UIState.Loading -> showLoading()
                else -> {}
            }
        }
    }

    override fun getViewBinding(): FragmentWeatherBinding {
        return FragmentWeatherBinding.inflate(layoutInflater)
    }

    companion object {

        fun newInstance(location : String?) : WeatherFragment {
            return WeatherFragment(location)
        }
    }


}