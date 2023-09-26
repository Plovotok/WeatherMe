package ru.plovotok.weatherme.presentation

import android.animation.ValueAnimator
import android.annotation.SuppressLint
import android.graphics.BitmapFactory
import android.graphics.Color
import android.graphics.LinearGradient
import android.graphics.Shader
import android.os.Build
import android.os.Bundle
import android.text.Spannable
import android.text.SpannableString
import android.text.style.ImageSpan
import android.view.LayoutInflater
import android.view.View
import android.view.animation.AccelerateDecelerateInterpolator
import androidx.annotation.RequiresApi
import androidx.core.content.ContextCompat
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.github.mikephil.charting.components.XAxis
import com.github.mikephil.charting.components.YAxis
import com.github.mikephil.charting.data.Entry
import com.github.mikephil.charting.data.LineData
import com.github.mikephil.charting.data.LineDataSet
import com.github.mikephil.charting.formatter.ValueFormatter
import com.github.mikephil.charting.interfaces.dataprovider.LineDataProvider
import kotlinx.coroutines.launch
import me.everything.android.ui.overscroll.OverScrollBounceEffectDecoratorBase
import me.everything.android.ui.overscroll.OverScrollDecoratorHelper
import me.everything.android.ui.overscroll.VerticalOverScrollBounceEffectDecorator
import me.everything.android.ui.overscroll.adapters.AbsListViewOverScrollDecorAdapter
import me.everything.android.ui.overscroll.adapters.RecyclerViewOverScrollDecorAdapter
import me.everything.android.ui.overscroll.adapters.ScrollViewOverScrollDecorAdapter
import ru.plovotok.weatherme.R
import ru.plovotok.weatherme.databinding.ChanceOfRainItemLayoutBinding
import ru.plovotok.weatherme.databinding.FragmentWeatherBinding
import ru.plovotok.weatherme.presentation.adapters.AstroAdapter
import ru.plovotok.weatherme.presentation.adapters.ChancesAdapter
import ru.plovotok.weatherme.presentation.adapters.HourlyForecastAdapter
import ru.plovotok.weatherme.presentation.adapters.WeatherIconAxisRenderer
import ru.plovotok.weatherme.presentation.adapters.WeatherInfoAdapter
import ru.plovotok.weatherme.presentation.adapters.WeathericonDrawer
import ru.plovotok.weatherme.presentation.base.BaseFragment
import ru.plovotok.weatherme.presentation.base.UIState
import ru.plovotok.weatherme.presentation.base.defineWeatherIconID
import ru.plovotok.weatherme.presentation.base.viewhelperclasses.ChanceOfPrecipitaion
import ru.plovotok.weatherme.presentation.custom.BaseEdgeEffectFactory


class WeatherFragment : BaseFragment<FragmentWeatherBinding>() {

    private val viewModel : WeatherViewModel by viewModels()

    private val weatherInfoAdapter = WeatherInfoAdapter()
    private lateinit var hourAdapter : HourlyForecastAdapter
    private val chancesAdapter = ChancesAdapter()
    private val sunStateAdapter = AstroAdapter()
    private var currentCond = 0

    @SuppressLint("ClickableViewAccessibility")
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewModel.getWeather()
        drawChart()
        val myLayoutManager = LinearLayoutManager(requireContext(), RecyclerView.HORIZONTAL,false )

        binding.toolbar.switch1.setOnCheckedChangeListener { _, isChacked ->
            if (!isChacked) {
//                (requireActivity() as WeatherActivity).setTimeImage(R.drawable.day_time)

//                (requireActivity() as WeatherActivity).setTimeImage(R.drawable.sun_background)
                (requireActivity() as WeatherActivity).setTimeImage(defineWeatherIconID(iconCode = currentCond, isDay = 1).backgroundResource)
            }  else {
//                (requireActivity() as WeatherActivity).setTimeImage(R.drawable.night_forest)
//                (requireActivity() as WeatherActivity).setTimeImage(R.drawable.starry_night_background)
//                currentCond = 1003
                (requireActivity() as WeatherActivity).setTimeImage(defineWeatherIconID(iconCode = currentCond, isDay = 0).backgroundResource)
            }
        }


        binding.toolbar.titleTextView.text = "Moscow, Russia"


        with(binding.weatherInfoRv) {
            adapter = weatherInfoAdapter
//            layoutManager = GridLayoutManager(requireContext(), 2, GridLayoutManager.HORIZONTAL, false)
        }

        hourAdapter = HourlyForecastAdapter()
        with(binding.hourlyForecastRv) {
            adapter = hourAdapter
            layoutManager = myLayoutManager
//            layoutManager = LinearLayoutManager(requireContext(), LinearLayoutManager.HORIZONTAL, false)
        }

        with(binding.rainChanceRv) {
            adapter = chancesAdapter
            layoutManager = LinearLayoutManager(requireContext(), LinearLayoutManager.VERTICAL, false)
        }

        with(binding.astroInfoRv) {
            adapter = sunStateAdapter
//            layoutManager = GridLayoutManager(requireContext(), 2, GridLayoutManager.HORIZONTAL, false)
        }

//        OverScrollDecoratorHelper.setUpOverScroll(binding.rootScroll)
//        OverScrollDecoratorHelper.setUpOverScroll(binding.hourlyForecastRv, OverScrollDecoratorHelper.ORIENTATION_HORIZONTAL)
//        OverScrollDecoratorHelper.setUpOverScroll(binding.rainChanceRv, OverScrollDecoratorHelper.ORIENTATION_VERTICAL)
//        VerticalOverScrollBounceEffectDecorator(RecyclerViewOverScrollDecorAdapter(binding.rainChanceRv))
        VerticalOverScrollBounceEffectDecorator(ScrollViewOverScrollDecorAdapter(binding.rootScroll))

        binding.rainChanceRv.edgeEffectFactory = BaseEdgeEffectFactory<ChanceOfRainItemLayoutBinding, ChanceOfPrecipitaion>()


        collectSunState()
        collectWeather()
        collectRainChances()
        collectHourForecast()
        collectHeaderInfo()
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
                    val headerInfo = state.data
                    with(binding.head) {
                        avgTemp.text = "${headerInfo?.currentTemp}°"
                        feelsLike.text ="Feels like ${headerInfo?.feelsLike}°"
                        weatherDesc.text = headerInfo?.condition?.text
                        if (headerInfo != null) {
                            weatherIcon.setImageResource(defineWeatherIconID(iconCode = headerInfo.condition.code, isDay = headerInfo.isDay).iconResource)
                            (requireActivity() as WeatherActivity).setTimeImage(defineWeatherIconID(iconCode = headerInfo.condition.code, isDay = headerInfo.isDay).backgroundResource)
                            currentCond = headerInfo.condition.code
                        }
//                        (requireActivity() as WeatherActivity).setTimeImage(R.drawable.night_forest)
//                        (requireActivity() as WeatherActivity).setTimeImage(R.drawable.day_time)
                        minTemp.text = "min: ${headerInfo?.minTemp}°"
                        maxTemp.text = "max: ${headerInfo?.maxTemp}°"
                        date.text = headerInfo?.time
                    }

                }
                is UIState.Loading -> showLoading()
                else -> {}
            }
        }
    }

    private fun drawChart() {

        // Массив координат точек
        val entries = mutableListOf<Entry>()
        val icon = ContextCompat.getDrawable(requireContext(), R.drawable.hourly_forecast)
        icon?.setBounds(0, 0, 24, 24)
        entries.add(Entry(1f, 5f,icon))
        entries.add(Entry(2f, 2f,"Sun"))
        entries.add(Entry(3f, 1f,"Sun"))
        entries.add(Entry(4f, 10f,"Sun"))
        entries.add(Entry(5f, 6f,"Sun"))
        entries.add(Entry(6f, 1f,"Sun"))
        entries.add(Entry(7f, 3f,"Sun"))
        entries.add(Entry(8f, -2f,"Sun"))
        entries.add(Entry(9f, 10f,"Sun"))
        entries.add(Entry(10f, 10f,"Sun"))
        entries.add(Entry(11f, 12f))
        entries.add(Entry(12f, 9f))
        entries.add(Entry(13f, 8f))
        entries.add(Entry(14f, 5f,icon))
        entries.add(Entry(15f, 2f,"Sun"))
        entries.add(Entry(16f, 1f,"Sun"))
        entries.add(Entry(17f, 10f,"Sun"))
        entries.add(Entry(18f, 6f,"Sun"))
        entries.add(Entry(19f, 1f,"Sun"))
        entries.add(Entry(20f, 3f,"Sun"))
        entries.add(Entry(21f, -2f,"Sun"))
        entries.add(Entry(22f, 10f,"Sun"))
        entries.add(Entry(23f, 12f))
        entries.add(Entry(24f, 9f))

// На основании массива точек создадим первую линию с названием
        val dataset = LineDataSet(entries, "График первый")
//        val gradient = GradientDrawable(
//            GradientDrawable.Orientation.TOP_BOTTOM,
//            intArrayOf(Color.parseColor("#8A2BE2"), Color.parseColor("#1A9FC1F4"))
//        )
//        val gradient = resources.getDrawable(R.drawable.chart_gradient)
        val colors = intArrayOf(Color.parseColor("#8A2BE2"), Color.parseColor("#1A9FC1F4"))
        val gradient = LinearGradient(0f, 0f, 0f, binding.lineChart.height.toFloat(), colors, null, Shader.TileMode.CLAMP)
        dataset.color = Color.WHITE
//        dataset.fillColor = Color.parseColor("#8A2BE2")
//        dataset.setDrawHighlightIndicators(true)
//        dataset.fillDrawable = GradientDrawable(GradientDrawable.Orientation.TOP_BOTTOM, colors)
        dataset.lineWidth = 2f
        dataset.valueTextColor = Color.WHITE
        dataset.fillAlpha = 100
        dataset.setDrawFilled(false)
        dataset.setDrawValues(true)
        dataset.setDrawIcons(true)
        dataset.mode = LineDataSet.Mode.CUBIC_BEZIER
        dataset.cubicIntensity = 0.2f
        dataset.valueTextSize = 14f
        dataset.valueFormatter = object : ValueFormatter() {
            override fun getFormattedValue(value: Float): String {
                return "$value°"
            }
        }
        dataset.highLightColor = Color.rgb(244, 117, 117)

// Создадим переменную данных для графика
        val data = LineData(dataset)
        val imageList = arrayListOf(BitmapFactory.decodeResource(resources, R.drawable.hourly_forecast))
// Передадим данные для графика в сам график
        binding.lineChart.data = data
        binding.lineChart.pointerIcon
        binding.lineChart.legend.isEnabled = false
        binding.lineChart.setBackgroundColor(Color.TRANSPARENT)
        binding.lineChart.description.isEnabled = false
        binding.lineChart.setTouchEnabled(false)
        binding.lineChart.isDragEnabled = true
//        binding.lineChart.renderer = BarChartIconRenderer(binding.lineChart as LineDataProvider, binding.lineChart.animator, binding.lineChart.viewPortHandler, imageList, requireContext())

        val xAxis = binding.lineChart.xAxis
        xAxis.position = XAxis.XAxisPosition.BOTTOM
        xAxis.textColor = Color.WHITE
        xAxis.textSize = 20f
        xAxis.setDrawGridLines(false)
        xAxis.setDrawAxisLine(false)
        xAxis.labelCount = 24
        xAxis.setDrawLabels(true)
        xAxis.valueFormatter = WeathericonDrawer(requireContext())
//        xAxis.valueFormatter = object : ValueFormatter() {
//
//
//            override fun getFormattedValue(value: Float): String {
////                 xAxis.la  txtview.setCompoundDrawablesWithIntrinsicBounds(
////                    R.drawable.image, 0, 0, 0)
//
//                val drawable = ContextCompat.getDrawable(requireContext(), R.drawable.hourly_forecast)
//                drawable?.setBounds(0, 0, 24, 24)
//                val spannable = SpannableString("Sun")
//                spannable.setSpan(ImageSpan(drawable!!), 0, 1, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE)
//                return spannable.toString()
//            }
//        }

        val yAxis = binding.lineChart.axisLeft
        yAxis.isEnabled = false
//        yAxis.labelCount = 7
        yAxis.granularity = 5f
        yAxis.setPosition(YAxis.YAxisLabelPosition.INSIDE_CHART)

        yAxis.setDrawGridLines(false)
        yAxis.axisLineColor = Color.WHITE
        yAxis.valueFormatter = object : ValueFormatter() {


            override fun getFormattedValue(value: Float): String {
                return "$value°"
            }
        }
//        binding.lineChart.axisLeft.isEnabled = false
        binding.lineChart.axisRight.isEnabled = false
        binding.lineChart.maxHighlightDistance = 100f
        binding.lineChart.animateY(500)
        binding.lineChart.offsetLeftAndRight(0)

// Не забудем отправить команду на перерисовку кадра, иначе график не отобразится
        binding.lineChart.invalidate()
    }

    override fun getViewBinding(): FragmentWeatherBinding {
        return FragmentWeatherBinding.inflate(layoutInflater)
    }


}