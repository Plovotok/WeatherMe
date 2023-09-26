package ru.plovotok.weatherme.data.models

import com.google.gson.annotations.SerializedName
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import ru.plovotok.weatherme.presentation.base.viewhelperclasses.ChanceOfPrecipitaion
import ru.plovotok.weatherme.presentation.base.viewhelperclasses.HourForecast
import ru.plovotok.weatherme.presentation.base.viewhelperclasses.ScreenData

@Serializable
data class WeatherResponseDTO(
    @SerializedName("location")
    val location : LocationDTO,
    @SerializedName("current")
    val current : CurrentWeatherDTO,
    @SerializedName("forecast")
    val forecast : Forecast
) {
    fun getTodayForecast() : ScreenData{
        return ScreenData(
            hourForecastList = forecast.forecastDay[0].toHourForecastList(),
            chancesList = forecast.forecastDay[0].toChancesList(),
            weatherInfoList = current.toWeatherInfoList(),
            astroData = forecast.forecastDay[0].toAstroList()
        )
    }

    fun get2DaysForecast() : List<HourForecast> {
        return forecast.forecastDay.flatMap { it.toHourForecastList() }
    }

    fun get2DaysForecastChances() : List<ChanceOfPrecipitaion> {
        return forecast.forecastDay.flatMap { it.toChancesList() }
    }
}
