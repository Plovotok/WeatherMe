package ru.plovotok.weatherme.domain.repository

import ru.plovotok.weatherme.data.models.WeatherResponseDTO

interface WeatherRepository {

    suspend fun getWeatherByQuery(q : String = "moscow", days : Int = 2) : WeatherResponseDTO?

    suspend fun getWeather() : WeatherResponseDTO?
}