package ru.plovotok.weatherme.data.models

import com.google.gson.annotations.SerializedName
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import ru.plovotok.weatherme.presentation.base.viewhelperclasses.WeatherInfo
import ru.plovotok.weatherme.presentation.base.viewhelperclasses.WeatherInfoType

@Serializable
data class DayWeatherDTO(
    @SerializedName("maxtemp_c")
    val maxTemp : Double,
    @SerializedName("mintemp_c")
    val minTemp : Double,
    @SerializedName("avgtemp_c")
    val avgTemp : Double,
    @SerializedName("maxwind_kph")
    val wind : Double,
    @SerializedName("daily_chance_of_rain")
    val chanceOfRain : Double,
    @SerializedName("condition")
    val condition: ConditionDTO,
    @SerializedName("uv")
    val uv : Double
)
