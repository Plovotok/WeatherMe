package ru.plovotok.weatherme.presentation

import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import ru.plovotok.weatherme.WeatherService
import ru.plovotok.weatherme.presentation.base.BaseViewModel
import ru.plovotok.weatherme.presentation.base.UIState
import ru.plovotok.weatherme.presentation.base.viewhelperclasses.AstroInfo
import ru.plovotok.weatherme.presentation.base.viewhelperclasses.ChanceOfPrecipitaion
import ru.plovotok.weatherme.presentation.base.viewhelperclasses.HeaderInfo
import ru.plovotok.weatherme.presentation.base.viewhelperclasses.HourForecast
import ru.plovotok.weatherme.presentation.base.viewhelperclasses.WeatherInfo

class WeatherViewModel : BaseViewModel() {

    private val _precipitationChances : MutableStateFlow<UIState<List<ChanceOfPrecipitaion>?>> = MutableStateFlow(UIState.Idle())
    val precipitationChances = _precipitationChances.asStateFlow()

    private val _hourlyForecast : MutableStateFlow<UIState<List<HourForecast>?>> = MutableStateFlow(UIState.Idle())
    val hourlyForecast = _hourlyForecast.asStateFlow()

    private val _weatherInfo : MutableStateFlow<UIState<List<WeatherInfo>?>> = MutableStateFlow(UIState.Idle())
    val weatherInfo = _weatherInfo.asStateFlow()

    private val _astroInfo : MutableStateFlow<UIState<List<AstroInfo>?>> = MutableStateFlow(UIState.Idle())
    val astroInfo = _astroInfo.asStateFlow()

    private val _headerInfo : MutableStateFlow<UIState<HeaderInfo?>> = MutableStateFlow(UIState.Idle())
    val headerInfo = _headerInfo.asStateFlow()

    private val weatherService = WeatherService.newInstance()
    private val weatherFlow = weatherService.getWeatherFlow()

    fun getWeather() = vms.launch(dio) {
        weatherService.getWeatherByQuery()

        _precipitationChances.loading()
        _hourlyForecast.loading()
        _weatherInfo.loading()
        _astroInfo.loading()
        _headerInfo.loading()
        weatherFlow.collect { weather ->
            if (weather != null) {

                val location = weather.location
                val forecast = weather.getTodayForecast()
                val current = weather.current
                val twoDaysForecast = weather.get2DaysForecast()
                val chancesList = weather.get2DaysForecastChances()

//                _precipitationChances.success(data = forecast.chancesList)

                _weatherInfo.success(data = forecast.weatherInfoList)
                _astroInfo.success(data = forecast.astroData)

                val nowTime = System.currentTimeMillis()/1000
                var nowIndex = 0

                for (i in twoDaysForecast.indices) {
                    if (nowTime >= twoDaysForecast[i].timeEpoch) {
                        nowIndex = i
                    } else {
                        val delta = 25 + nowIndex
                        val resultList = twoDaysForecast.subList(fromIndex = nowIndex, toIndex = delta)
                        _hourlyForecast.success(data = resultList)
                        break
                    }
                }

                for (i in chancesList.indices) {
                    if (nowTime >= chancesList[i].timeEpoch) {
                        nowIndex = i
                    } else {
                        val delta = 25 + nowIndex
                        val resultList = chancesList.subList(fromIndex = nowIndex, toIndex = delta)
                        _precipitationChances.success(data = resultList)
                        break
                    }
                }


                delay(1000L)

                _headerInfo.success(data = HeaderInfo(
                    location = "${location.name}, ${location.country}",
                    currentTemp = current.temp,
                    minTemp = weather.forecast.forecastDay[0].day.minTemp,
                    maxTemp = weather.forecast.forecastDay[0].day.maxTemp,
                    feelsLike = current.feelsLike,
                    condition = current.condition,
                    time = weather.location.localtime,
                    isDay = current.isDay
                )
                )
            }
        }
    }

}