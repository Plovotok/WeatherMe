package ru.plovotok.weatherme

import android.util.Log
import com.google.gson.Gson
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import ru.plovotok.weatherme.data.models.LocationResponseDTO
import ru.plovotok.weatherme.data.models.WeatherResponseDTO
import ru.plovotok.weatherme.data.repository.WeatherRepositoryImpl
import ru.plovotok.weatherme.domain.repository.WeatherRepository
import ru.plovotok.weatherme.localstorage.LocalStorage

class WeatherService {

    private val getWeatherScope = CoroutineScope(Dispatchers.IO)
    private val getLocationsScope = CoroutineScope(Dispatchers.IO)

    private val _weatherFlow : MutableStateFlow<WeatherResponseDTO?> = MutableStateFlow(null)
    private val weatherFlow = _weatherFlow.asStateFlow()
    private val _locationListFlow : MutableStateFlow<List<LocationResponseDTO?>?> = MutableStateFlow(null)
    private val locationListFlow = _locationListFlow.asStateFlow()

    private val repository : WeatherRepository = WeatherRepositoryImpl()

    private val localStorage = LocalStorage.newInstance()

    fun getWeatherFlow() = weatherFlow

    fun getLocationListFlow() = locationListFlow

    fun getWeatherByQuery() = getWeatherScope.launch {
        var query = "Moscow"

        val locationJson = localStorage.get(LocalStorage.FAVOURITE_LOCATION)
        Log.d("Ktor-client", "favourite location: $locationJson")

        if (locationJson != null) {
            val location = Gson().fromJson(locationJson, LocationResponseDTO::class.java)
            query = "${location.lat},${location.lon}"
        } else {
            query = "Moscow"
        }

        val weather = repository.getWeatherByQuery(q = query)
        _weatherFlow.emit(weather)
    }

//    init {
//        getWeatherByQuery()
//    }

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