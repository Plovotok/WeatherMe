package ru.plovotok.weatherme.presentation.base.viewhelperclasses

import ru.plovotok.weatherme.data.models.ConditionDTO

data class HeaderInfo(
    val location : String,
    val currentTemp : Double,
    val minTemp : Double,
    val maxTemp : Double,
    val feelsLike : Double,
    val condition : ConditionDTO?,
    val time : String,
    val time_epoch : Long,
    val isDay : Int
)