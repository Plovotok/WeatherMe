package ru.plovotok.weatherme.data.models

import com.google.gson.annotations.SerializedName
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import ru.plovotok.weatherme.presentation.base.viewhelperclasses.WeatherInfo
import ru.plovotok.weatherme.presentation.base.viewhelperclasses.WeatherInfoType

@Serializable
data class CurrentWeatherDTO(
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
    @SerializedName("humidity")
    val humidity : Double,
    @SerializedName("feelslike_c")
    @SerialName("feelslike_c")
    val feelsLike : Double,
    @SerializedName("uv")
    val uv : Double
) {
    fun toWeatherInfoList() : List<WeatherInfo> {
        val list = mutableListOf<WeatherInfo>()
        list.add(
            WeatherInfo(
                type = WeatherInfoType.WIND_SPEED,
                name = "Wind speed",
                value = wind / 3.6
            )
        )

        list.add(
            WeatherInfo(
                type = WeatherInfoType.HUMIDITY,
                name = "Humidity",
                value = humidity
            )
        )

        list.add(
            WeatherInfo(
                type = WeatherInfoType.PRESSURE,
                name = "Pressure",
                value = (pressure / 1.3333).toInt().toDouble()
            )
        )

        list.add(
            WeatherInfo(
                type = WeatherInfoType.UV_INDEX,
                name = "UV Index",
                value = uv
            )
        )
        return list
    }
}
