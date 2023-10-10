package ru.plovotok.weatherme.data.repository

import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import ru.plovotok.weatherme.data.models.roommodels.WeatherLocationEntity
import ru.plovotok.weatherme.domain.repository.room.WeatherLocationsDao
import javax.inject.Inject

class LocationsRepository @Inject constructor(private val dao : WeatherLocationsDao) {

//    val locations = dao.getAllLocations()
    private val _locations : MutableStateFlow<List<WeatherLocationEntity>?> = MutableStateFlow(null)
    val locationsFlow = _locations.asStateFlow()

    suspend fun addLocation(location : WeatherLocationEntity) {
        dao.addLocation(location)
    }

    suspend fun removeLocationById(id : Int) {
        dao.deleteLocationById(id)
    }

    suspend fun getLocations() : List<WeatherLocationEntity>? {
        val locationsList = dao.getAllLocations()

//        _locations.emit(locationsList)
        return locationsList
    }
}