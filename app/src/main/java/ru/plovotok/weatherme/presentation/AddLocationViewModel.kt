package ru.plovotok.weatherme.presentation

import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import ru.plovotok.weatherme.WeatherService
import ru.plovotok.weatherme.data.models.LocationResponseDTO
import ru.plovotok.weatherme.data.repository.JsonUtils.toJson
import ru.plovotok.weatherme.domain.models.LocationResponse
import ru.plovotok.weatherme.localstorage.LocalStorage
import ru.plovotok.weatherme.presentation.base.BaseViewModel
import ru.plovotok.weatherme.presentation.base.UIState

class AddLocationViewModel : BaseViewModel() {

    private val weatherService = WeatherService.newInstance()

    private val _locationList : MutableStateFlow<UIState<List<LocationResponse?>?>> = MutableStateFlow(UIState.Idle())
    val locationList = _locationList.asStateFlow()

    private val _myLocations : MutableStateFlow<UIState<List<LocationResponse>>> = MutableStateFlow(UIState.Idle())
    val myLocations = _myLocations.asStateFlow()

    private val locationListFlow = weatherService.getLocationListFlow()


    fun getLocationListByQuery(query : String, lang : String = "en") = vms.launch(dio) {
        _locationList.loading()
        weatherService.getLocationListByQuery(query, lang)
        locationListFlow.collect { list ->
            _locationList.success(data = list?.map { it?.toModel() })
        }
    }

    fun getLocationsList() = vms.launch{
        _myLocations.loading()
        val localStorage = LocalStorage.newInstance()
        val json = localStorage.get(LocalStorage.LOCATIONS_LIST)
        if (json != null) {
            val listType = object : TypeToken<List<LocationResponseDTO>>() {}.type
            val list = Gson().fromJson<List<LocationResponseDTO>>(json, listType)
            _myLocations.success(data = list.map { it.toModel() })
        } else {
            _myLocations.success(data = mutableListOf())
        }

    }

    fun addLocationToList(location : LocationResponse) {
        val localStorage = LocalStorage.newInstance()
        val json = localStorage.get(LocalStorage.LOCATIONS_LIST)
        if (json != null) {
            val listType = object : TypeToken<List<LocationResponseDTO>>() {}.type
            val list = Gson().fromJson<List<LocationResponseDTO>>(json, listType).toMutableList()
            if (!list.contains(location.toDTO())) {
                list.add(location.toDTO())
                localStorage.save(LocalStorage.LOCATIONS_LIST, list.toJson())
            }

        } else {
            val list : MutableList<LocationResponseDTO> = mutableListOf()
            list.add(location.toDTO())
            localStorage.save(LocalStorage.LOCATIONS_LIST, list.toJson())
        }

    }

    fun removeLocation(location : LocationResponse) = vms.launch {
        _myLocations.loading()
        val localStorage = LocalStorage.newInstance()
        val json = localStorage.get(LocalStorage.LOCATIONS_LIST)
        if (json != null) {
            val listType = object : TypeToken<List<LocationResponseDTO>>() {}.type
            val list = Gson().fromJson<List<LocationResponseDTO>>(json, listType).toMutableList()
            list.remove(location.toDTO())
            localStorage.save(LocalStorage.LOCATIONS_LIST, list.toJson())
            _myLocations.success(data = list.map { it.toModel() })
        }
    }

    fun setLocationAsFavourite(location : LocationResponse) {
        val localStorage = LocalStorage.newInstance()
        localStorage.save(LocalStorage.FAVOURITE_LOCATION, location.toJson())
    }

}