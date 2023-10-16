package ru.plovotok.weatherme.presentation.screens

import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import ru.plovotok.weatherme.data.repository.LocationsRepository
import ru.plovotok.weatherme.domain.models.LocationResponse
import ru.plovotok.weatherme.presentation.base.BaseViewModel
import ru.plovotok.weatherme.presentation.base.UIState
import javax.inject.Inject

@HiltViewModel
class PagerViewModel @Inject constructor(
    val repository: LocationsRepository
) : BaseViewModel() {

    private val _myLocations : MutableStateFlow<UIState<List<LocationResponse>>> = MutableStateFlow(UIState.Idle())
    val myLocations = _myLocations.asStateFlow()


    fun getLocationsList() = vms.launch(dio){
        _myLocations.loading()
        val locList = repository.getLocations()
        _myLocations.success(data = locList?.map { it.toModel() })

    }
}