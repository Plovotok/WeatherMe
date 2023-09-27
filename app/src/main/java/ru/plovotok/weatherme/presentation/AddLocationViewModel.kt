package ru.plovotok.weatherme.presentation

import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import ru.plovotok.weatherme.WeatherService
import ru.plovotok.weatherme.data.models.LocationResponseDTO
import ru.plovotok.weatherme.presentation.base.BaseViewModel
import ru.plovotok.weatherme.presentation.base.UIState

class AddLocationViewModel : BaseViewModel() {

    private val weatherService = WeatherService.newInstance()

    private val _locationList : MutableStateFlow<UIState<List<LocationResponseDTO?>?>> = MutableStateFlow(UIState.Idle())
    val locationList = _locationList.asStateFlow()

    private val locationListFlow = weatherService.getLocationListFlow()



    fun getLocationListByQuery(query : String, lang : String = "en") = vms.launch(dio) {
        _locationList.loading()
        weatherService.getLocationListByQuery(query, lang)
        locationListFlow.collect { list ->
            _locationList.success(data = list)
        }
    }

}