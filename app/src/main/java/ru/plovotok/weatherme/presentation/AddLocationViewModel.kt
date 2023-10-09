package ru.plovotok.weatherme.presentation

import android.util.Log
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import ru.plovotok.weatherme.WeatherService
import ru.plovotok.weatherme.data.repository.JsonUtils.toJson
import ru.plovotok.weatherme.data.repository.LocationsRepository
import ru.plovotok.weatherme.domain.models.LocationResponse
import ru.plovotok.weatherme.localstorage.LocalStorage
import ru.plovotok.weatherme.presentation.base.BaseViewModel
import ru.plovotok.weatherme.presentation.base.UIState

class AddLocationViewModel(val repository: LocationsRepository) : BaseViewModel() {

    private val weatherService = WeatherService.newInstance()

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
        Log.d("Room", "adding ${location.name}")
        repository.addLocation(location.toDBEntity())
        getLocationsList()
    }

    fun removeLocation(location : LocationResponse) = vms.launch {

        Log.d("Room", "deleting ${location.name}")
        repository.removeLocationById(location.id)

        getLocationsList()
    }

    fun setLocationAsFavourite(location : LocationResponse) {
        val localStorage = LocalStorage.newInstance()
        localStorage.save(LocalStorage.FAVOURITE_LOCATION, location.toJson())
    }

    fun addToEditingList(item : LocationResponse) {
        Log.d("Room", "added:  ${item.name}")
        activeEditingList.add(item)
        Log.d("Room", "deletingList:  ${activeEditingList}")
    }

    fun removeFromEditingList(item : LocationResponse) {
        Log.d("Room", "removed:  ${item.name}")
        activeEditingList.remove(item)
        Log.d("Room", "deletingList:  ${activeEditingList}")
    }

    fun clearEditingList() {
        activeEditingList = mutableListOf()
    }

    fun removeActiveListLocations() = vms.launch {
        Log.d("Room", "deletingList:  ${activeEditingList}")
        val deletingJob = vms.launch {
            activeEditingList.forEach { location ->
                Log.d("Room", "deleting from bd ${location.name}")
                repository.removeLocationById(location.id)
            }
        }
        deletingJob.join()
        getLocationsList()

    }

}