package ru.plovotok.weatherme.presentation.screens

import android.annotation.SuppressLint
import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.core.content.ContextCompat
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch
import ru.plovotok.weatherme.R
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
import ru.plovotok.weatherme.presentation.custom.SunStateView
import java.util.Calendar
import java.util.TimeZone

@AndroidEntryPoint
class WeatherFragment() : BaseFragment<FragmentWeatherBinding>() {

    private val viewModel : WeatherViewModel by viewModels()

    private val weatherInfoAdapter = WeatherInfoAdapter()
    private lateinit var hourAdapter : HourlyForecastAdapter
    private val chancesAdapter = ChancesAdapter()
    private val sunStateAdapter = AstroAdapter()
    private var currentCond = 0
    private var nowIsDay : Int? = null


    @SuppressLint("ClickableViewAccessibility")
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        viewModel.getWeatherForecast()

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

            (requireActivity() as WeatherActivity).setTimeImage(nowWeather.backgroundResource, TypeOfPrecip.CLEAR, PrecipRate.CLEAR)
            findNavController().navigate(R.id.action_weatherFragment_to_addLocationFragment)
//            findNavController().navigate(R.id.action_weatherFragment_to_testFragment)

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

//        binding.rainChanceRv.edgeEffectFactory = BaseEdgeEffectFactory<ChanceOfRainItemLayoutBinding, ChanceOfPrecipitaion>()
        binding.rootScroll.setOnRefreshListener { viewModel.getWeatherForecast() }

        collectSunState()
        collectWeather()
        collectRainChances()
        collectHourForecast()
//        collectHeaderInfo()
    }

    override fun onPause() {
        super.onPause()
        Log.i("Weather-Fragment", "Weather-Fragment onPause()")
    }

    override fun onStop() {
        super.onStop()
        Log.i("Weather-Fragment", "Weather-Fragment onStop()")
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

                    val hoursRise = state.data?.get(0)?.time?.substring(0, 5)?.split(":")?.first()
                    val minutesRise = state.data?.get(0)?.time?.substring(0, 5)?.split(":")?.last()
                    val timeInMillisRise = hoursRise?.toLong()?.times((60 * 60 * 1000))?.plus(minutesRise?.toLong()?.times((60 * 1000))!!)
                    Log.d("SunState", "rise : ${timeInMillisRise}")

                    val hoursSet =
                        state.data?.get(1)?.time?.substring(0, 5)?.split(":")?.first()?.toInt()
                            ?.plus(12)
                    val minutesSet = state.data?.get(1)?.time?.substring(0, 5)?.split(":")?.last()

                    val timeInMillisSet = hoursSet?.toLong()?.times((60 * 60 * 1000))?.plus(minutesSet?.toLong()?.times((60 * 1000))!!)
                    Log.d("SunState", "set : ${timeInMillisSet}")

                    binding.sunView.setSunRiseAndSetTime(
                        timeInMillisRise ?: (SunStateView.MILLIS_IN_DAY * 0.25).toLong(),
                        timeInMillisSet ?: (SunStateView.MILLIS_IN_DAY * 0.75).toLong()
                    )
                }
                else -> {}
            }
        }
    }

    override fun onResume() {
        super.onResume()
        collectHeaderInfo()
    }

    private fun collectHeaderInfo() = viewLifecycleOwner.lifecycleScope.launch {
        viewModel.headerInfo.collect { state ->
            when(state) {
                is UIState.Success -> {
                    hideLoading()
                    binding.rootScroll.isRefreshing = false
                    val headerInfo = state.data
                    binding.toolbar.titleTextView.text = headerInfo?.location

                    state.data?.time_epoch?.let {
                        val splittedString = state.data.time.split(":").first()
                        val stringLength = splittedString.length

                        val hoursByLocationTime = splittedString.substring(stringLength - 2,stringLength).split(" ").last().toInt()
                        val hoursByGmt = Calendar.getInstance(TimeZone.getTimeZone("GMT+00")).get(Calendar.HOUR_OF_DAY)
                        val deltaHour = hoursByLocationTime - hoursByGmt

                        val daysLeft = (it / SunStateView.SECONDS_PER_DAY).toInt()
                        val millis = (it - daysLeft * SunStateView.SECONDS_PER_DAY + deltaHour * 60 * 60) * 1000


//                        val millis = 780000L - 1L
//                        val millis = 26400000L + 1L
//                        val millis = 61560000L + 1L

                        binding.sunView.setCurrentTime(millis)
                    }

                    with(binding.head) {
                        avgTemp.text = "${headerInfo?.currentTemp?.toInt()}°"
                        feelsLike.text ="Feels like ${headerInfo?.feelsLike?.toInt()}°"
                        weatherDesc.text = headerInfo?.condition?.text
                        if (headerInfo != null) {
                            nowIsDay = headerInfo.isDay
                            val screenWeather = defineWeatherByCondition(iconCode = headerInfo.condition!!.code, isDay = headerInfo.isDay)

                            weatherIcon.setImageResource(screenWeather.iconResource)
                            (requireActivity() as WeatherActivity).setTimeImage(screenWeather.backgroundResource, screenWeather.precipType, screenWeather.precipRate)
                            currentCond = headerInfo.condition.code
                        }
                        minTemp.text = "night: ${headerInfo?.minTemp?.toInt()}°"
                        maxTemp.text = "day: ${headerInfo?.maxTemp?.toInt()}°"
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

}