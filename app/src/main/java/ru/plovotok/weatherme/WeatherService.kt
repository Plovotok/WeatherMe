package ru.plovotok.weatherme

import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import ru.plovotok.weatherme.data.models.LocationResponseDTO
import ru.plovotok.weatherme.data.models.WeatherResponseDTO
import ru.plovotok.weatherme.data.repository.WeatherRepositoryImpl
import ru.plovotok.weatherme.domain.repository.WeatherRepository

class WeatherService {

    private val getWeatherScope = CoroutineScope(Dispatchers.IO)
    private val getLocationsScope = CoroutineScope(Dispatchers.IO)

    private val _weatherFlow : MutableStateFlow<WeatherResponseDTO?> = MutableStateFlow(null)
    private val weatherFlow = _weatherFlow.asStateFlow()
    private val _locationListFlow : MutableStateFlow<List<LocationResponseDTO?>?> = MutableStateFlow(null)
    private val locationListFlow = _locationListFlow.asStateFlow()

    private val repository : WeatherRepository = WeatherRepositoryImpl()

    fun getWeatherFlow() = weatherFlow

    fun getLocationListFlow() = locationListFlow

    private fun getWeatherByQuery() = getWeatherScope.launch {
        val weather = repository.getWeatherByQuery()
        _weatherFlow.emit(weather)
    }

    init {
        getWeatherByQuery()
    }

    companion object {
        private var INSTANCE: WeatherService? = null

        fun newInstance(): WeatherService {
            if (INSTANCE != null) return INSTANCE!!

            val temp = WeatherService()
            INSTANCE = temp
            return temp
        }

        fun initialize() {
            INSTANCE = WeatherService()
        }
    }

   fun getLocationListByQuery(query : String, lang : String = "en") = getLocationsScope.launch {
       val response = repository.findLocationByQuery(query, lang)
       if (response != null) {
           _locationListFlow.emit(response)
       } else {
           _locationListFlow.emit(listOf())
       }
    }

}