package ru.plovotok.weatherme.presentation.base

import android.util.Log
import ru.plovotok.weatherme.R

data class ScreenWeather(
    val iconResource : Int,
    val backgroundResource : Int
)

fun defineWeatherIconID(iconCode : Int, isDay : Int) : ScreenWeather{
    Log.d("Weather", "iconCode : $iconCode, isDay : $isDay")
    when(iconCode) {
        1000 -> {
            when(isDay) {
                0 -> return ScreenWeather(R.drawable.sunny_night,R.drawable.starry_night_background)
                1 -> return ScreenWeather(R.drawable.sunny_day,R.drawable.header_background)
            }
        }
        1003 -> {
            when(isDay) {
                0 -> return ScreenWeather(R.drawable.partly_cloudy_night, R.drawable.cloudy_night_background_two)
                1 -> return ScreenWeather(R.drawable.partly_cloudy_day, R.drawable.partly_cloudy_background)
            }
        }
        1006 -> {
            when(isDay) {
                0 -> return ScreenWeather(R.drawable.cloudy_night, R.drawable.cloudy_night_background_two)
                1 -> return ScreenWeather(R.drawable.cloudy_day, R.drawable.cloudy_background)
            }
        }
        1009 -> {
            return ScreenWeather(R.drawable.overcast, R.drawable.cloudy_background)
        }
        1030 -> {
            return ScreenWeather(R.drawable.mist, R.drawable.cloudy_background)
        }
        1063 -> {
            when(isDay) {
                0 -> return ScreenWeather(R.drawable.patchy_rain_possible_night, R.drawable.cloudy_night_background_two)
                1 -> return ScreenWeather(R.drawable.patchy_rain_possible_day, R.drawable.cloudy_background)
            }
        }
        1066 -> {
            when(isDay) {
                0 -> return ScreenWeather(R.drawable.patchy_snow_possible_night, R.drawable.cloudy_night_background_two)
                1 -> return ScreenWeather(R.drawable.patchy_snow_possible_day, R.drawable.cloudy_background)
            }
        }
        1069 -> {
            when(isDay) {
                0 -> return ScreenWeather(R.drawable.patchy_snow_possible_night, R.drawable.cloudy_night_background_two)
                1 -> return ScreenWeather(R.drawable.patchy_snow_possible_day, R.drawable.cloudy_background)
            }
        }
        1072 -> {
            when(isDay) {
                0 -> return ScreenWeather(R.drawable.patchy_freezing_dreezing_possible_night, R.drawable.cloudy_night_background_two)
                1 -> return ScreenWeather(R.drawable.patchy_freezing_dreezing_possible_day, R.drawable.cloudy_background)
            }
        }
        1087 -> {
            when(isDay) {
                0 -> return ScreenWeather(R.drawable.thundery_outbreaks_possible_night, R.drawable.thunderstorm_night_background)
                1 -> return ScreenWeather(R.drawable.thundery_outbreaks_possible_day, R.drawable.thunderstorm_background)
            }
        }
        1114 -> {
            return ScreenWeather(R.drawable.blowing_snow, R.drawable.cloudy_background)
        }
        1117 -> {
            return ScreenWeather(R.drawable.blizzard, R.drawable.cloudy_background)
        }
        1135 -> {
            return ScreenWeather(R.drawable.mist, R.drawable.cloudy_background)
        }
        1147 -> {
            return ScreenWeather(R.drawable.freezing_fog, R.drawable.cloudy_background)
        }
        1150 -> {
            return ScreenWeather(R.drawable.light_drizzle, R.drawable.cloudy_background)
        }
        1153 -> {
            return ScreenWeather(R.drawable.light_drizzle, R.drawable.cloudy_background)
        }
        1168 -> {
            when(isDay) {
                0 -> return ScreenWeather(R.drawable.patchy_freezing_dreezing_possible_night, R.drawable.cloudy_night_background_two)
                1 -> return ScreenWeather(R.drawable.patchy_freezing_dreezing_possible_day, R.drawable.cloudy_background)
            }
        }
        1171 -> {
            return ScreenWeather(R.drawable.heavy_freezing_drizzle, R.drawable.cloudy_background)
        }
        1180 -> {
            when(isDay) {
                0 -> return ScreenWeather(R.drawable.patchy_rain_possible_night, R.drawable.cloudy_night_background_two)
                1 -> return ScreenWeather(R.drawable.patchy_rain_possible_day, R.drawable.cloudy_background)
            }
        }
        1183 -> {
            return ScreenWeather(R.drawable.light_drizzle, R.drawable.cloudy_background)
        }
        1186 -> {
            when(isDay) {
                0 -> return ScreenWeather(R.drawable.patchy_rain_possible_night, R.drawable.cloudy_night_background_two)
                1 -> return ScreenWeather(R.drawable.patchy_rain_possible_day, R.drawable.cloudy_background)
            }
        }
        1189 -> {
            return ScreenWeather(R.drawable.light_drizzle, R.drawable.cloudy_background)
        }
        1192 -> {
            when(isDay) {
                0 -> return ScreenWeather(R.drawable.patchy_rain_possible_night, R.drawable.cloudy_night_background_two)
                1 -> return ScreenWeather(R.drawable.patchy_rain_possible_day, R.drawable.cloudy_background)
            }
        }
        1195 -> {
            return ScreenWeather(R.drawable.heavy_rain, R.drawable.cloudy_background)
        }
        1198 -> {
            when(isDay) {
                0 -> return ScreenWeather(R.drawable.patchy_freezing_dreezing_possible_night, R.drawable.cloudy_night_background_two)
                1 -> return ScreenWeather(R.drawable.patchy_freezing_dreezing_possible_day, R.drawable.cloudy_background)
            }
        }
        1201 -> {
            when(isDay) {
                0 -> return ScreenWeather(R.drawable.patchy_freezing_dreezing_possible_night, R.drawable.cloudy_night_background_two)
                1 -> return ScreenWeather(R.drawable.patchy_freezing_dreezing_possible_day, R.drawable.cloudy_background)
            }
        }
        1204 -> {
            when(isDay) {
                0 -> return ScreenWeather(R.drawable.patchy_freezing_dreezing_possible_night, R.drawable.cloudy_night_background_two)
                1 -> return ScreenWeather(R.drawable.patchy_freezing_dreezing_possible_day, R.drawable.cloudy_background)
            }
        }
        1207 -> {
            when(isDay) {
                0 -> return ScreenWeather(R.drawable.patchy_freezing_dreezing_possible_night, R.drawable.cloudy_night_background_two)
                1 -> return ScreenWeather(R.drawable.patchy_freezing_dreezing_possible_day, R.drawable.cloudy_background)
            }
        }
        1210 -> {
            when(isDay) {
                0 -> return ScreenWeather(R.drawable.patchy_snow_possible_night, R.drawable.cloudy_night_background_two)
                1 -> return ScreenWeather(R.drawable.patchy_snow_possible_day, R.drawable.cloudy_background)
            }
        }
        1213 -> {
            when(isDay) {
                0 -> return ScreenWeather(R.drawable.patchy_snow_possible_night, R.drawable.cloudy_night_background_two)
                1 -> return ScreenWeather(R.drawable.patchy_snow_possible_day, R.drawable.cloudy_background)
            }
        }
        1216 -> {
            when(isDay) {
                0 -> return ScreenWeather(R.drawable.patchy_snow_possible_night, R.drawable.cloudy_night_background_two)
                1 -> return ScreenWeather(R.drawable.patchy_snow_possible_day, R.drawable.cloudy_background)
            }
        }
        1219 -> {
            when(isDay) {
                0 -> return ScreenWeather(R.drawable.patchy_snow_possible_night, R.drawable.cloudy_night_background_two)
                1 -> return ScreenWeather(R.drawable.patchy_snow_possible_day, R.drawable.cloudy_background)
            }
        }
        1222 -> {
            when(isDay) {
                0 -> return ScreenWeather(R.drawable.patchy_snow_possible_night, R.drawable.cloudy_night_background_two)
                1 -> return ScreenWeather(R.drawable.patchy_snow_possible_day, R.drawable.cloudy_background)
            }
        }
        1225 -> {
            return ScreenWeather(R.drawable.heavy_snow, R.drawable.cloudy_background)
        }
        1237 -> {
            return ScreenWeather(R.drawable.snow, R.drawable.cloudy_background)
        }
        1240 -> {
            when(isDay) {
                0 -> return ScreenWeather(R.drawable.patchy_rain_possible_night, R.drawable.cloudy_night_background_two)
                1 -> return ScreenWeather(R.drawable.patchy_rain_possible_day, R.drawable.cloudy_background)
            }
        }
        1243 -> {
            when(isDay) {
                0 -> return ScreenWeather(R.drawable.patchy_rain_possible_night, R.drawable.cloudy_night_background_two)
                1 -> return ScreenWeather(R.drawable.patchy_rain_possible_day, R.drawable.cloudy_background)
            }
        }
        1246 -> {
            return ScreenWeather(R.drawable.torrential_rain_shower, R.drawable.cloudy_background)
        }
        1249 -> {
            when(isDay) {
                0 -> return ScreenWeather(R.drawable.patchy_freezing_dreezing_possible_night, R.drawable.cloudy_night_background_two)
                1 -> return ScreenWeather(R.drawable.patchy_freezing_dreezing_possible_day, R.drawable.cloudy_background)
            }
        }
        1252 -> {
            when(isDay) {
                0 -> return ScreenWeather(R.drawable.patchy_freezing_dreezing_possible_night, R.drawable.cloudy_night_background_two)
                1 -> return ScreenWeather(R.drawable.patchy_freezing_dreezing_possible_day, R.drawable.cloudy_background)
            }
        }
        1255 -> {
            when(isDay) {
                0 -> return ScreenWeather(R.drawable.patchy_snow_possible_night, R.drawable.cloudy_night_background_two)
                1 -> return ScreenWeather(R.drawable.patchy_snow_possible_day, R.drawable.cloudy_background)
            }
        }
        1258 -> {
            when(isDay) {
                0 -> return ScreenWeather(R.drawable.patchy_snow_possible_night, R.drawable.cloudy_night_background_two)
                1 -> return ScreenWeather(R.drawable.patchy_snow_possible_day, R.drawable.cloudy_background)
            }
        }
        1261 -> {
            when(isDay) {
                0 -> return ScreenWeather(R.drawable.patchy_snow_possible_night, R.drawable.cloudy_night_background_two)
                1 -> return ScreenWeather(R.drawable.patchy_snow_possible_day, R.drawable.cloudy_background)
            }
        }
        1264 -> {
            when(isDay) {
                0 -> return ScreenWeather(R.drawable.patchy_snow_possible_night, R.drawable.cloudy_night_background_two)
                1 -> return ScreenWeather(R.drawable.patchy_snow_possible_day, R.drawable.cloudy_background)
            }
        }
        1273 -> {
            return ScreenWeather(R.drawable.patchy_light_rain_with_thunder, R.drawable.thunderstorm_background)
        }
        1276 -> {
            return ScreenWeather(R.drawable.heavy_rain_with_thunder, R.drawable.thunderstorm_background)
        }
        1279 -> {
            return ScreenWeather(R.drawable.snow_with_thunder, R.drawable.thunderstorm_background)
        }
        1282 -> {
            return ScreenWeather(R.drawable.snow_with_thunder, R.drawable.thunderstorm_background)
        }

    }

    return ScreenWeather(0, R.drawable.header_background)
}