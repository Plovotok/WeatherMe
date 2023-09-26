package ru.plovotok.weatherme.data.models

import com.google.gson.annotations.SerializedName
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class Forecast(
    @SerializedName("forecastday")
    val forecastDay : List<ForecastDayDTO>
)
