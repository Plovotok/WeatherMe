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
import ru.plovotok.weatherme.presentation.base.viewhelperclasses.AstroInfo
import ru.plovotok.weatherme.presentation.base.viewhelperclasses.AstroType
import ru.plovotok.weatherme.presentation.custom.SunData
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
//            findNavController().navigate(R.id.action_weatherFragment_to_mapFragment)

        }

        binding.head.root.alpha = 0f
        binding.weatherInfoRv.alpha = 0f
        binding.hourlyForecastLayout.alpha = 0f
        binding.chanceOfRainLayout.alpha = 0f
        binding.weatherMapButton.root.alpha = 0f
        binding.sunViewLayout.alpha = 0f
        binding.astroInfoRv.alpha = 0f

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

        binding.rootScroll.setOnRefreshListener { viewModel.getWeatherForecast() }

        binding.weatherMapButton.root.setOnClickListener {
            if (nowIsDay == null) return@setOnClickListener
            val nowWeather = defineWeatherByCondition(iconCode = currentCond, isDay = nowIsDay!!)
            (requireActivity() as WeatherActivity).setTimeImage(nowWeather.backgroundResource, TypeOfPrecip.CLEAR, PrecipRate.CLEAR)
            findNavController().navigate(R.id.action_weatherFragment_to_mapFragment)
        }

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

                    binding.weatherInfoRv
                        .animate()
                        .alpha(1f)
                        .setDuration(400L)
                        .start()
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

                    binding.hourlyForecastLayout
                        .animate()
                        .alpha(1f)
                        .setDuration(400L)
                        .start()
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

                    binding.chanceOfRainLayout
                        .animate()
                        .alpha(1f)
                        .setDuration(400L)
                        .start()
                }
                else -> {}
            }
        }
    }

    private fun collectSunState() = viewLifecycleOwner.lifecycleScope.launch {
        viewModel.astroInfo.collect { state ->
            when(state) {
                is UIState.Success -> {
                    state.data?.let {
                        val riseSetTimes = countMillisRiseSet(it)
                        binding.sunView.setSunRiseAndSunSetTime(
                            riseSetTimes.sunRiseMillis, riseSetTimes.sunSetMillis
                        )

                        sunStateAdapter.loadItems(items = listOf(
                            AstroInfo(type = AstroType.SUNRISE, time = riseSetTimes.sunRiseTime),
                            AstroInfo(type = AstroType.SUNSET, time = riseSetTimes.sunSetTime)
                        ))
                        sunStateAdapter.notifyDataSetChanged()

                        binding.sunViewLayout
                            .animate()
                            .alpha(1f)
                            .setDuration(400L)
                            .start()

                        binding.astroInfoRv
                            .animate()
                            .alpha(1f)
                            .setDuration(400L)
                            .start()
                    }

                }
                else -> {}
            }
        }
    }

    override fun onResume() {
        super.onResume()
        collectSunState()
        collectWeather()
        collectRainChances()
        collectHourForecast()
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
                        val deltaHour = countDeltaHour(state.data.time)
                        val daysLeft = (it / SunStateView.SECONDS_PER_DAY).toInt()
                        val millis = (it - daysLeft * SunStateView.SECONDS_PER_DAY + deltaHour * 60 * 60) * 1000

                        binding.sunView.setCurrentTime(millis)
                    }

                    with(binding.head) {
                        avgTemp.text = "${headerInfo?.currentTemp?.toInt()}°"
                        val feelLikeText = resources.getString(R.string.feels_like)
                        feelsLike.text ="$feelLikeText ${headerInfo?.feelsLike?.toInt()}°"
                        weatherDesc.text = headerInfo?.condition?.text
                        if (headerInfo != null) {
                            nowIsDay = headerInfo.isDay
                            val screenWeather = defineWeatherByCondition(iconCode = headerInfo.condition!!.code, isDay = headerInfo.isDay)

                            weatherIcon.setImageResource(screenWeather.iconResource)
                            (requireActivity() as WeatherActivity).setTimeImage(screenWeather.backgroundResource, screenWeather.precipType, screenWeather.precipRate)
                            currentCond = headerInfo.condition.code
                        }
                        val nightText = resources.getString(R.string.night)
                        val dayText = resources.getString(R.string.day)
                        minTemp.text = "$nightText: ${headerInfo?.minTemp?.toInt()}°"
                        maxTemp.text = "$dayText: ${headerInfo?.maxTemp?.toInt()}°"
                        date.text = headerInfo?.time

                        binding.head.root
                            .animate()
                            .alpha(1f)
                            .setDuration(400L)
                            .start()

                        binding.weatherMapButton.root
                            .animate()
                            .alpha(1f)
                            .setDuration(400L)
                            .start()
                    }

                }
                is UIState.Loading -> showLoading()
                else -> {}
            }
        }
    }
    private fun countMillisRiseSet(data : List<AstroInfo>) : SunData {
        val hoursRise = data[0].time.substring(0, 5).split(":").first()
        val minutesRise = data[0].time.substring(0, 5).split(":").last()
        val timeInMillisRise = hoursRise.toLong().times((60 * 60 * 1000)) + minutesRise.toLong().times((60 * 1000))

        val hoursSet = data[1].time.substring(0, 5).split(":").first().toInt() + 12
        val minutesSet = data[1].time.substring(0, 5).split(":").last()

        val timeInMillisSet = hoursSet.toLong().times((60 * 60 * 1000)) + minutesSet.toLong().times((60 * 1000))
        val riseString = "$hoursRise:$minutesRise"
        val setString = "$hoursSet:$minutesSet"
        return SunData(timeInMillisRise, timeInMillisSet, riseString, setString)
    }

    private fun countDeltaHour(serverTime: String) : Int {
        val splittedString = serverTime.split(":").first()
        val stringLength = splittedString.length

        val hoursByLocationTime =
            splittedString.substring(stringLength - 2, stringLength).split(" ").last().toInt()
        val hoursByGmt =
            Calendar.getInstance(TimeZone.getTimeZone("GMT+00")).get(Calendar.HOUR_OF_DAY)

        return hoursByLocationTime - hoursByGmt
    }

    override fun getViewBinding(): FragmentWeatherBinding {
        return FragmentWeatherBinding.inflate(layoutInflater)
    }

}