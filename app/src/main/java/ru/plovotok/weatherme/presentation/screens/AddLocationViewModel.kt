package ru.plovotok.weatherme.presentation.screens

import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import ru.plovotok.weatherme.WeatherService
import ru.plovotok.weatherme.data.repository.JsonUtils.toJson
import ru.plovotok.weatherme.data.repository.LocationsRepository
import ru.plovotok.weatherme.domain.models.LocationResponse
import ru.plovotok.weatherme.localstorage.ILocalStorage
import ru.plovotok.weatherme.localstorage.LocalStorage
import ru.plovotok.weatherme.presentation.base.BaseViewModel
import ru.plovotok.weatherme.presentation.base.UIState
import javax.inject.Inject

@HiltViewModel
class AddLocationViewModel @Inject constructor(
    private val repository: LocationsRepository,
    private val weatherService : WeatherService,
    private val localStorage : ILocalStorage
) : BaseViewModel() {

    private val _locationList : MutableStateFlow<UIState<List<LocationResponse?>?>> = MutableStateFlow(UIState.Idle())
    val locationList = _locationList.asStateFlow()

    private val _myLocations : MutableStateFlow<UIState<List<LocationResponse>>> = MutableStateFlow(UIState.Idle())
    val myLocations = _myLocations.asStateFlow()

    private val locationListFlow = weatherService.getLocationListFlow()

    private var activeEditingList = mutableListOf<LocationResponse>()

    fun getLocationListByQuery(query : String, lang : String = "en") = vms.launch(dio) {
        _locationList.loading()
        weatherService.getLocationListByQuery(query, lang)
        locationListFlow.collect { list ->
            _locationList.success(data = list?.map { it?.toModel() })
        }
    }

    fun getLocationsList() = vms.launch(dio){
        _myLocations.loading()
        val locList = repository.getLocations()
        _myLocations.success(data = locList?.map { it.toModel() })

    }

    fun addLocation(location: LocationResponse) = vms.launch(dio) {
        repository.addLocation(location.toDBEntity())
        getLocationsList()
    }

    fun removeLocation(location : LocationResponse) = vms.launch {
        repository.removeLocationById(location.id)
        getLocationsList()
    }

    fun setLocationAsFavourite(location : LocationResponse) {
        localStorage.save(LocalStorage.FAVOURITE_LOCATION, location.toJson())
    }

    fun addToEditingList(item : LocationResponse) {
        activeEditingList.add(item)
    }

    fun removeFromEditingList(item : LocationResponse) {
        activeEditingList.remove(item)
    }

    fun clearEditingList() {
        activeEditingList = mutableListOf()
    }

    fun removeActiveListLocations() = vms.launch {
        val deletingJob = vms.launch {
            activeEditingList.forEach { location ->
                repository.removeLocationById(location.id)
            }
        }
        deletingJob.join()
        getLocationsList()

    }

}