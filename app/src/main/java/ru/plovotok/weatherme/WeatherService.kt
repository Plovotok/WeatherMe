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
import ru.plovotok.weatherme.domain.repository.WeatherRepository
import ru.plovotok.weatherme.localstorage.ILocalStorage
import ru.plovotok.weatherme.localstorage.LocalStorage
import javax.inject.Inject

class WeatherService @Inject constructor(
    private val localStorage : ILocalStorage,
    private val repository : WeatherRepository
) {

    private val getWeatherScope = CoroutineScope(Dispatchers.IO)
    private val getLocationsScope = CoroutineScope(Dispatchers.IO)

    private val _weatherFlow : MutableStateFlow<WeatherResponseDTO?> = MutableStateFlow(null)
    private val weatherFlow = _weatherFlow.asStateFlow()
    private val _locationListFlow : MutableStateFlow<List<LocationResponseDTO?>?> = MutableStateFlow(null)
    private val locationListFlow = _locationListFlow.asStateFlow()


    fun getWeatherFlow() = weatherFlow

    fun getLocationListFlow() = locationListFlow

    suspend fun getForecast() : WeatherResponseDTO? {
        var query = "Moscow"
        val locationJson = localStorage.get(LocalStorage.FAVOURITE_LOCATION)
        Log.d("Ktor-client", "favourite location: $locationJson")
        if (locationJson != null) {
            val location = Gson().fromJson(locationJson, LocationResponseDTO::class.java)
            query = "${location.lat},${location.lon}"
        } else {
            query = "Moscow"
        }

        return repository.getWeatherByQuery(query)
    }

   fun getLocationListByQuery(query : String, lang : String = "en") = getLocationsScope.launch {
       val response = repository.findLocationByQuery(query, lang)
       if (response != null) {
           _locationListFlow.emit(response)
       } else {
           _locationListFlow.emit(listOf())
       }
    }

    companion object {
        const val TAG = "Weather-Service"
    }

}