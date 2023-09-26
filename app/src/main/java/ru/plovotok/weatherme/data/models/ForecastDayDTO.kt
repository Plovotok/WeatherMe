package ru.plovotok.weatherme.data.models

import com.google.gson.annotations.SerializedName
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import ru.plovotok.weatherme.presentation.base.viewhelperclasses.AstroInfo
import ru.plovotok.weatherme.presentation.base.viewhelperclasses.AstroType
import ru.plovotok.weatherme.presentation.base.viewhelperclasses.ChanceOfPrecipitaion
import ru.plovotok.weatherme.presentation.base.viewhelperclasses.HourForecast
import ru.plovotok.weatherme.presentation.base.viewhelperclasses.WeatherInfo
import ru.plovotok.weatherme.presentation.base.viewhelperclasses.WeatherInfoType

@Serializable
data class ForecastDayDTO (
    @SerializedName("date")
    val date : String,
    @SerializedName("day")
    val day : DayWeatherDTO,
    @SerializedName("astro")
    val astro : AstroDTO,
    @SerializedName("hour")
    val hour : List<HourWeatherDTO>
) {
    fun toChancesList() : List<ChanceOfPrecipitaion> {
        return hour.map { it.toChancesOfPrecipitation() }
    }

    fun toHourForecastList() : List<HourForecast> {
        return hour.map { it.toHourForecast() }
    }

    fun toAstroList() : List<AstroInfo> {
        val list = mutableListOf<AstroInfo>()
        list.add(
            AstroInfo(
                type = AstroType.SUNRISE,
                time = astro.sunrise
            )
        )

        list.add(
            AstroInfo(
                type = AstroType.SUNSET,
                time = astro.sunset
            )
        )

        return list
    }
}