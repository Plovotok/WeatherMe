package ru.plovotok.weatherme.presentation.base.viewhelperclasses

data class HourForecast(
    val time : String,
    val timeEpoch : Long,
    val code : Int,
    val isDay : Int,
    val avgTemp : Double
)
