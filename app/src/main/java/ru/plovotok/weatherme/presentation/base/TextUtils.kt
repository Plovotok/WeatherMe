package ru.plovotok.weatherme.presentation.base

import android.util.Log
import androidx.annotation.DrawableRes
import ru.plovotok.weatherme.R

enum class PrecipRate(val rate : Int) {
    CLEAR(0),
    LIGHT(1),
    MEDIUM(2),
    HARD(3)
}

enum class TypeOfPrecip{
    CLEAR,
    RAIN,
    SNOW
}

data class ScreenWeather(
    @DrawableRes
    val iconResource : Int,
    @DrawableRes
    val backgroundResource : Int,
    val precipType : TypeOfPrecip = TypeOfPrecip.CLEAR,
    val precipRate : PrecipRate = PrecipRate.CLEAR,
    @DrawableRes
    val toolbarDrawable: Int = R.drawable.map_toolbar_background
)

fun defineWeatherByCondition(iconCode : Int, isDay : Int) : ScreenWeather{
    Log.d("Weather", "iconCode : $iconCode, isDay : $isDay")
    when(iconCode) {
        1000 -> {
            when(isDay) {
                0 -> return ScreenWeather(R.drawable.sunny_night,R.drawable.starry_night_background, TypeOfPrecip.CLEAR, PrecipRate.CLEAR, R.color.clear_night_toolbar)
                1 -> return ScreenWeather(R.drawable.sunny_day,R.drawable.header_background, TypeOfPrecip.CLEAR, PrecipRate.CLEAR, R.color.clear_day_toolbar)
            }
        }
        1003 -> {
            when(isDay) {
                0 -> return ScreenWeather(R.drawable.partly_cloudy_night, R.drawable.cloudy_night_background_two, TypeOfPrecip.CLEAR, PrecipRate.CLEAR, R.color.cloudy_night_toolbar)
                1 -> return ScreenWeather(R.drawable.partly_cloudy_day, R.drawable.partly_cloudy_background, TypeOfPrecip.CLEAR, PrecipRate.CLEAR, R.color.cloudy_day_toolbar)
            }
        }
        1006 -> {
            when(isDay) {
                0 -> return ScreenWeather(R.drawable.cloudy_night, R.drawable.cloudy_night_background_two, TypeOfPrecip.CLEAR, PrecipRate.CLEAR, R.color.cloudy_night_toolbar)
                1 -> return ScreenWeather(R.drawable.cloudy_day, R.drawable.cloudy_background, TypeOfPrecip.CLEAR, PrecipRate.CLEAR, R.color.cloudy_gray_toolbar)
            }
        }
        1009 -> {
            return ScreenWeather(R.drawable.overcast, R.drawable.cloudy_background, TypeOfPrecip.CLEAR, PrecipRate.CLEAR, R.color.cloudy_gray_toolbar)
        }
        1030 -> {
            return ScreenWeather(R.drawable.mist, R.drawable.cloudy_background, TypeOfPrecip.CLEAR, PrecipRate.CLEAR, R.color.cloudy_gray_toolbar)
        }
        1063 -> {
            when(isDay) {
                0 -> return ScreenWeather(R.drawable.patchy_rain_possible_night, R.drawable.cloudy_night_background_two, TypeOfPrecip.RAIN, PrecipRate.LIGHT, R.color.cloudy_night_toolbar)
                1 -> return ScreenWeather(R.drawable.patchy_rain_possible_day, R.drawable.cloudy_background, TypeOfPrecip.RAIN, PrecipRate.LIGHT, R.color.cloudy_gray_toolbar)
            }
        }
        1066 -> {
            when(isDay) {
                0 -> return ScreenWeather(R.drawable.patchy_snow_possible_night, R.drawable.cloudy_night_background_two, TypeOfPrecip.RAIN, PrecipRate.LIGHT, R.color.cloudy_night_toolbar)
                1 -> return ScreenWeather(R.drawable.patchy_snow_possible_day, R.drawable.cloudy_background, TypeOfPrecip.RAIN, PrecipRate.LIGHT, R.color.cloudy_gray_toolbar)
            }
        }
        1069 -> {
            when(isDay) {
                0 -> return ScreenWeather(R.drawable.patchy_snow_possible_night, R.drawable.cloudy_night_background_two, TypeOfPrecip.RAIN, PrecipRate.LIGHT, R.color.cloudy_night_toolbar)
                1 -> return ScreenWeather(R.drawable.patchy_snow_possible_day, R.drawable.cloudy_background, TypeOfPrecip.RAIN, PrecipRate.LIGHT, R.color.cloudy_gray_toolbar)
            }
        }
        1072 -> {
            when(isDay) {
                0 -> return ScreenWeather(R.drawable.patchy_freezing_dreezing_possible_night, R.drawable.cloudy_night_background_two, TypeOfPrecip.RAIN, PrecipRate.LIGHT, R.color.cloudy_night_toolbar)
                1 -> return ScreenWeather(R.drawable.patchy_freezing_dreezing_possible_day, R.drawable.cloudy_background, TypeOfPrecip.RAIN, PrecipRate.LIGHT, R.color.cloudy_gray_toolbar)
            }
        }
        1087 -> {
            when(isDay) {
                0 -> return ScreenWeather(R.drawable.thundery_outbreaks_possible_night, R.drawable.thunderstorm_night_background, TypeOfPrecip.RAIN, PrecipRate.MEDIUM, R.color.thunder_night_toolbar)
                1 -> return ScreenWeather(R.drawable.thundery_outbreaks_possible_day, R.drawable.thunderstorm_background, TypeOfPrecip.RAIN, PrecipRate.MEDIUM, R.color.thunder_day_toolbar)
            }
        }
        1114 -> {
            return ScreenWeather(R.drawable.blowing_snow, R.drawable.cloudy_background, TypeOfPrecip.SNOW, PrecipRate.LIGHT, R.color.cloudy_gray_toolbar)
        }
        1117 -> {
            return ScreenWeather(R.drawable.blizzard, R.drawable.cloudy_background, TypeOfPrecip.SNOW, PrecipRate.LIGHT, R.color.cloudy_gray_toolbar)
        }
        1135 -> {
            return ScreenWeather(R.drawable.mist, R.drawable.cloudy_background, TypeOfPrecip.CLEAR, PrecipRate.LIGHT, R.color.cloudy_gray_toolbar)
        }
        1147 -> {
            return ScreenWeather(R.drawable.freezing_fog, R.drawable.cloudy_background, TypeOfPrecip.SNOW, PrecipRate.LIGHT, R.color.cloudy_gray_toolbar)
        }
        1150 -> {
            return ScreenWeather(R.drawable.light_drizzle, R.drawable.cloudy_background, TypeOfPrecip.RAIN, PrecipRate.LIGHT, R.color.cloudy_gray_toolbar)
        }
        1153 -> {
            return ScreenWeather(R.drawable.light_drizzle, R.drawable.cloudy_background, TypeOfPrecip.RAIN, PrecipRate.LIGHT, R.color.cloudy_gray_toolbar)
        }
        1168 -> {
            when(isDay) {
                0 -> return ScreenWeather(R.drawable.patchy_freezing_dreezing_possible_night, R.drawable.cloudy_night_background_two, TypeOfPrecip.RAIN, PrecipRate.LIGHT, R.color.cloudy_night_toolbar)
                1 -> return ScreenWeather(R.drawable.patchy_freezing_dreezing_possible_day, R.drawable.cloudy_background, TypeOfPrecip.RAIN, PrecipRate.LIGHT, R.color.cloudy_gray_toolbar)
            }
        }
        1171 -> {
            return ScreenWeather(R.drawable.heavy_freezing_drizzle, R.drawable.cloudy_background, TypeOfPrecip.RAIN, PrecipRate.HARD, R.color.cloudy_gray_toolbar)
        }
        1180 -> {
            when(isDay) {
                0 -> return ScreenWeather(R.drawable.patchy_rain_possible_night, R.drawable.cloudy_night_background_two, TypeOfPrecip.RAIN, PrecipRate.LIGHT, R.color.cloudy_night_toolbar)
                1 -> return ScreenWeather(R.drawable.patchy_rain_possible_day, R.drawable.cloudy_background, TypeOfPrecip.RAIN, PrecipRate.LIGHT, R.color.cloudy_gray_toolbar)
            }
        }
        1183 -> {
            return ScreenWeather(R.drawable.light_drizzle, R.drawable.cloudy_background, TypeOfPrecip.RAIN, PrecipRate.LIGHT, R.color.cloudy_gray_toolbar)
        }
        1186 -> {
            when(isDay) {
                0 -> return ScreenWeather(R.drawable.patchy_rain_possible_night, R.drawable.cloudy_night_background_two, TypeOfPrecip.RAIN, PrecipRate.MEDIUM, R.color.cloudy_night_toolbar)
                1 -> return ScreenWeather(R.drawable.patchy_rain_possible_day, R.drawable.cloudy_background, TypeOfPrecip.RAIN, PrecipRate.MEDIUM, R.color.cloudy_gray_toolbar)
            }
        }
        1189 -> {
            return ScreenWeather(R.drawable.light_drizzle, R.drawable.cloudy_background, TypeOfPrecip.RAIN, PrecipRate.MEDIUM, R.color.cloudy_gray_toolbar)
        }
        1192 -> {
            when(isDay) {
                0 -> return ScreenWeather(R.drawable.patchy_rain_possible_night, R.drawable.cloudy_night_background_two, TypeOfPrecip.RAIN, PrecipRate.HARD, R.color.cloudy_night_toolbar)
                1 -> return ScreenWeather(R.drawable.patchy_rain_possible_day, R.drawable.cloudy_background, TypeOfPrecip.RAIN, PrecipRate.HARD, R.color.cloudy_gray_toolbar)
            }
        }
        1195 -> {
            return ScreenWeather(R.drawable.heavy_rain, R.drawable.cloudy_background, TypeOfPrecip.RAIN, PrecipRate.HARD, R.color.cloudy_gray_toolbar)
        }
        1198 -> {
            when(isDay) {
                0 -> return ScreenWeather(R.drawable.patchy_freezing_dreezing_possible_night, R.drawable.cloudy_night_background_two, TypeOfPrecip.RAIN, PrecipRate.LIGHT, R.color.cloudy_night_toolbar)
                1 -> return ScreenWeather(R.drawable.patchy_freezing_dreezing_possible_day, R.drawable.cloudy_background, TypeOfPrecip.RAIN, PrecipRate.LIGHT, R.color.cloudy_gray_toolbar)
            }
        }
        1201 -> {
            when(isDay) {
                0 -> return ScreenWeather(R.drawable.patchy_freezing_dreezing_possible_night, R.drawable.cloudy_night_background_two, TypeOfPrecip.RAIN, PrecipRate.MEDIUM, R.color.cloudy_night_toolbar)
                1 -> return ScreenWeather(R.drawable.patchy_freezing_dreezing_possible_day, R.drawable.cloudy_background, TypeOfPrecip.RAIN, PrecipRate.MEDIUM, R.color.cloudy_gray_toolbar)
            }
        }
        1204 -> {
            when(isDay) {
                0 -> return ScreenWeather(R.drawable.patchy_freezing_dreezing_possible_night, R.drawable.cloudy_night_background_two, TypeOfPrecip.RAIN, PrecipRate.LIGHT, R.color.cloudy_night_toolbar)
                1 -> return ScreenWeather(R.drawable.patchy_freezing_dreezing_possible_day, R.drawable.cloudy_background, TypeOfPrecip.RAIN, PrecipRate.LIGHT, R.color.cloudy_gray_toolbar)
            }
        }
        1207 -> {
            when(isDay) {
                0 -> return ScreenWeather(R.drawable.patchy_freezing_dreezing_possible_night, R.drawable.cloudy_night_background_two, TypeOfPrecip.RAIN, PrecipRate.MEDIUM, R.color.cloudy_night_toolbar)
                1 -> return ScreenWeather(R.drawable.patchy_freezing_dreezing_possible_day, R.drawable.cloudy_background, TypeOfPrecip.RAIN, PrecipRate.MEDIUM, R.color.cloudy_gray_toolbar)
            }
        }
        1210 -> {
            when(isDay) {
                0 -> return ScreenWeather(R.drawable.patchy_snow_possible_night, R.drawable.cloudy_night_background_two, TypeOfPrecip.SNOW, PrecipRate.LIGHT, R.color.cloudy_night_toolbar)
                1 -> return ScreenWeather(R.drawable.patchy_snow_possible_day, R.drawable.cloudy_background, TypeOfPrecip.SNOW, PrecipRate.LIGHT, R.color.cloudy_gray_toolbar)
            }
        }
        1213 -> {
            when(isDay) {
                0 -> return ScreenWeather(R.drawable.patchy_snow_possible_night, R.drawable.cloudy_night_background_two, TypeOfPrecip.SNOW, PrecipRate.LIGHT, R.color.cloudy_night_toolbar)
                1 -> return ScreenWeather(R.drawable.patchy_snow_possible_day, R.drawable.cloudy_background, TypeOfPrecip.SNOW, PrecipRate.LIGHT, R.color.cloudy_gray_toolbar)
            }
        }
        1216 -> {
            when(isDay) {
                0 -> return ScreenWeather(R.drawable.patchy_snow_possible_night, R.drawable.cloudy_night_background_two, TypeOfPrecip.SNOW, PrecipRate.MEDIUM, R.color.cloudy_night_toolbar)
                1 -> return ScreenWeather(R.drawable.patchy_snow_possible_day, R.drawable.cloudy_background, TypeOfPrecip.SNOW, PrecipRate.MEDIUM, R.color.cloudy_gray_toolbar)
            }
        }
        1219 -> {
            when(isDay) {
                0 -> return ScreenWeather(R.drawable.patchy_snow_possible_night, R.drawable.cloudy_night_background_two, TypeOfPrecip.SNOW, PrecipRate.MEDIUM, R.color.cloudy_night_toolbar)
                1 -> return ScreenWeather(R.drawable.patchy_snow_possible_day, R.drawable.cloudy_background, TypeOfPrecip.SNOW, PrecipRate.MEDIUM, R.color.cloudy_gray_toolbar)
            }
        }
        1222 -> {
            when(isDay) {
                0 -> return ScreenWeather(R.drawable.patchy_snow_possible_night, R.drawable.cloudy_night_background_two, TypeOfPrecip.SNOW, PrecipRate.HARD, R.color.cloudy_night_toolbar)
                1 -> return ScreenWeather(R.drawable.patchy_snow_possible_day, R.drawable.cloudy_background, TypeOfPrecip.SNOW, PrecipRate.HARD)
            }
        }
        1225 -> {
            return ScreenWeather(R.drawable.heavy_snow, R.drawable.cloudy_background, TypeOfPrecip.SNOW, PrecipRate.HARD, R.color.cloudy_gray_toolbar)
        }
        1237 -> {
            return ScreenWeather(R.drawable.snow, R.drawable.cloudy_background, TypeOfPrecip.SNOW, PrecipRate.LIGHT, R.color.cloudy_gray_toolbar)
        }
        1240 -> {
            when(isDay) {
                0 -> return ScreenWeather(R.drawable.patchy_rain_possible_night, R.drawable.cloudy_night_background_two, TypeOfPrecip.RAIN, PrecipRate.LIGHT, R.color.cloudy_night_toolbar)
                1 -> return ScreenWeather(R.drawable.patchy_rain_possible_day, R.drawable.cloudy_background, TypeOfPrecip.RAIN, PrecipRate.LIGHT, R.color.cloudy_gray_toolbar)
            }
        }
        1243 -> {
            when(isDay) {
                0 -> return ScreenWeather(R.drawable.patchy_rain_possible_night, R.drawable.cloudy_night_background_two, TypeOfPrecip.RAIN, PrecipRate.MEDIUM, R.color.cloudy_night_toolbar)
                1 -> return ScreenWeather(R.drawable.patchy_rain_possible_day, R.drawable.cloudy_background, TypeOfPrecip.RAIN, PrecipRate.MEDIUM, R.color.cloudy_gray_toolbar)
            }
        }
        1246 -> {
            return ScreenWeather(R.drawable.torrential_rain_shower, R.drawable.cloudy_background, TypeOfPrecip.RAIN, PrecipRate.HARD, R.color.cloudy_gray_toolbar)
        }
        1249 -> {
            when(isDay) {
                0 -> return ScreenWeather(R.drawable.patchy_freezing_dreezing_possible_night, R.drawable.cloudy_night_background_two, TypeOfPrecip.RAIN, PrecipRate.LIGHT, R.color.cloudy_night_toolbar)
                1 -> return ScreenWeather(R.drawable.patchy_freezing_dreezing_possible_day, R.drawable.cloudy_background, TypeOfPrecip.RAIN, PrecipRate.LIGHT, R.color.cloudy_gray_toolbar)
            }
        }
        1252 -> {
            when(isDay) {
                0 -> return ScreenWeather(R.drawable.patchy_freezing_dreezing_possible_night, R.drawable.cloudy_night_background_two, TypeOfPrecip.RAIN, PrecipRate.MEDIUM, R.color.cloudy_night_toolbar)
                1 -> return ScreenWeather(R.drawable.patchy_freezing_dreezing_possible_day, R.drawable.cloudy_background, TypeOfPrecip.RAIN, PrecipRate.MEDIUM, R.color.cloudy_gray_toolbar)
            }
        }
        1255 -> {
            when(isDay) {
                0 -> return ScreenWeather(R.drawable.patchy_snow_possible_night, R.drawable.cloudy_night_background_two, TypeOfPrecip.SNOW, PrecipRate.LIGHT, R.color.cloudy_night_toolbar)
                1 -> return ScreenWeather(R.drawable.patchy_snow_possible_day, R.drawable.cloudy_background, TypeOfPrecip.SNOW, PrecipRate.LIGHT, R.color.cloudy_gray_toolbar)
            }
        }
        1258 -> {
            when(isDay) {
                0 -> return ScreenWeather(R.drawable.patchy_snow_possible_night, R.drawable.cloudy_night_background_two, TypeOfPrecip.SNOW, PrecipRate.MEDIUM, R.color.cloudy_night_toolbar)
                1 -> return ScreenWeather(R.drawable.patchy_snow_possible_day, R.drawable.cloudy_background, TypeOfPrecip.SNOW, PrecipRate.MEDIUM, R.color.cloudy_gray_toolbar)
            }
        }
        1261 -> {
            when(isDay) {
                0 -> return ScreenWeather(R.drawable.patchy_snow_possible_night, R.drawable.cloudy_night_background_two, TypeOfPrecip.SNOW, PrecipRate.LIGHT, R.color.cloudy_night_toolbar)
                1 -> return ScreenWeather(R.drawable.patchy_snow_possible_day, R.drawable.cloudy_background, TypeOfPrecip.SNOW, PrecipRate.LIGHT, R.color.cloudy_gray_toolbar)
            }
        }
        1264 -> {
            when(isDay) {
                0 -> return ScreenWeather(R.drawable.patchy_snow_possible_night, R.drawable.cloudy_night_background_two, TypeOfPrecip.SNOW, PrecipRate.HARD, R.color.cloudy_night_toolbar)
                1 -> return ScreenWeather(R.drawable.patchy_snow_possible_day, R.drawable.cloudy_background, TypeOfPrecip.SNOW, PrecipRate.HARD, R.color.cloudy_gray_toolbar)
            }
        }
        1273 -> {
            return ScreenWeather(R.drawable.patchy_light_rain_with_thunder, R.drawable.thunderstorm_background, TypeOfPrecip.RAIN, PrecipRate.LIGHT, R.color.thunder_day_toolbar)
        }
        1276 -> {
            return ScreenWeather(R.drawable.heavy_rain_with_thunder, R.drawable.thunderstorm_background, TypeOfPrecip.RAIN, PrecipRate.HARD, R.color.thunder_day_toolbar)
        }
        1279 -> {
            return ScreenWeather(R.drawable.snow_with_thunder, R.drawable.thunderstorm_background, TypeOfPrecip.SNOW, PrecipRate.LIGHT, R.color.thunder_day_toolbar)
        }
        1282 -> {
            return ScreenWeather(R.drawable.snow_with_thunder, R.drawable.thunderstorm_background, TypeOfPrecip.SNOW, PrecipRate.HARD, R.color.thunder_day_toolbar)
        }

    }

    return ScreenWeather(0, R.drawable.header_background, TypeOfPrecip.CLEAR, PrecipRate.CLEAR, R.color.clear_day_toolbar)
}