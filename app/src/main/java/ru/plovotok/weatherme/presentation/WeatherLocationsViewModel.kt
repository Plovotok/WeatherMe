package ru.plovotok.weatherme.presentation

import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import ru.plovotok.weatherme.data.repository.LocationsRepository
import ru.plovotok.weatherme.presentation.base.BaseViewModel
import ru.plovotok.weatherme.presentation.base.UIState

class WeatherLocationsViewModel(val repository: LocationsRepository) : BaseViewModel() {

    private val _myLocations : MutableStateFlow<UIState<List<String>>> = MutableStateFlow(
        UIState.Idle())
    val myLocations = _myLocations.asStateFlow()

    fun getLocationsList() = vms.launch(dio){
        _myLocations.loading()
        val locList = repository.getLocations()
        val modelList = locList?.map { it.toModel()}
        val needList = modelList?.map { location -> "${location.lat},${location.lon}" }
        _myLocations.success(data = needList)

    }
}