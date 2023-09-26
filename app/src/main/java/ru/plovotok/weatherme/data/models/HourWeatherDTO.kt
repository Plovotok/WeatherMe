package ru.plovotok.weatherme.data.models

import com.google.gson.annotations.SerializedName
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import ru.plovotok.weatherme.presentation.base.viewhelperclasses.ChanceOfPrecipitaion
import ru.plovotok.weatherme.presentation.base.viewhelperclasses.HourForecast

@Serializable
data class HourWeatherDTO(
    @SerializedName("time")
    val time : String,
    @SerializedName("time_epoch")
    val timeEpoch : Long,
    @SerializedName("temp_c")
    @SerialName("temp_c")
    val temp : Double,
    @SerializedName("is_day")
    @SerialName("is_day")
    val isDay : Int,
    @SerializedName("condition")
    val condition : ConditionDTO,
    @SerializedName("wind_kph")
    @SerialName("wind_kph")
    val wind : Double,
    @SerializedName("pressure_mb")
    @SerialName("pressure_mb")
    val pressure : Double,
    @SerializedName("feelslike_c")
    @SerialName("feelslike_c")
    val feelsLike : Double,
    @SerializedName("chance_of_rain")
    @SerialName("chance_of_rain")
    val chanceOfRain : Double,
    @SerializedName("uv")
    val uv : Double
) {
    fun toChancesOfPrecipitation() = ChanceOfPrecipitaion(
        timeEpoch = timeEpoch,
        time = time.split(" ").last(),
        chance = chanceOfRain
    )

    fun toHourForecast() = HourForecast(
        time = time.split(" ").last(),
        timeEpoch = timeEpoch,
        code = condition.code,
        isDay = isDay,
        avgTemp = temp
    )
}
