package ru.plovotok.weatherme.presentation.base.viewhelperclasses

data class ScreenData(
    val hourForecastList: List<HourForecast>,
    val chancesList : List<ChanceOfPrecipitaion>,
    val weatherInfoList : List<WeatherInfo>,
    val astroData : List<AstroInfo>
)
