package ru.plovotok.weatherme.presentation.base

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asSharedFlow

abstract class BaseViewModel: ViewModel() {

    private val _errorMessage: MutableSharedFlow<String> = MutableSharedFlow()
    val errorMessage = _errorMessage.asSharedFlow()

    val vms = viewModelScope
    val dio = Dispatchers.IO

    suspend fun emitError(error: String) {
        _errorMessage.emit(error)
    }

    suspend fun <T> MutableStateFlow<UIState<T>>.success(message: String? = null, data: T? = null) {
        this.emit(
            UIState.Success(
                message = message,
                data = data
            )
        )
    }

    protected suspend fun <T> MutableStateFlow<UIState<T>>.error(message: String) {
        this.emit(
            UIState.Error(
                message = message
            )
        )
    }

    protected suspend fun <T> MutableStateFlow<UIState<T>>.loading(message: String? = null) {
        this.emit(
            UIState.Loading(
                message = message
            )
        )
    }

    protected suspend fun <T> MutableStateFlow<UIState<T>>.idle() {
        delay(50L)
        this.emit(UIState.Idle())
    }

    protected suspend fun <T> MutableSharedFlow<UIState<T>>.success(message: String? = null, data: T? = null) {
        this.emit(
            UIState.Success(
                message = message,
                data = data
            )
        )
    }

    protected suspend fun <T> MutableSharedFlow<UIState<T>>.error(message: String, data: T? = null) {
        this.emit(
            UIState.Error(
                message = message,
                data = data
            )
        )
    }

    protected suspend fun <T> MutableSharedFlow<UIState<T>>.loading(message: String? = null, data: T? = null) {
        this.emit(
            UIState.Loading(
                message = message
            )
        )
    }

}