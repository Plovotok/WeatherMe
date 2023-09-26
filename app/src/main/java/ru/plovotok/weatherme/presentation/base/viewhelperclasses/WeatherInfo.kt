package ru.plovotok.weatherme.presentation.base.viewhelperclasses

data class WeatherInfo(
    val type : WeatherInfoType,
    val name : String,
    val value : Double
)
