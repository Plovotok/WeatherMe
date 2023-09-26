package ru.plovotok.weatherme.presentation.base

sealed class UIState<T> {
    class Loading<T>(val message: String? = null) : UIState<T>()
    class Success<T>(val message: String? = null, val data: T? = null) : UIState<T>()
    class Error<T>(val message: String, val data: T? = null) : UIState<T>()

    class Idle<T> : UIState<T>()
}