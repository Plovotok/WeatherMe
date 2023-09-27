package ru.plovotok.weatherme.domain.repository

import ru.plovotok.weatherme.data.models.LocationResponseDTO
import ru.plovotok.weatherme.data.models.WeatherResponseDTO

interface WeatherRepository {

    suspend fun getWeatherByQuery(q : String = "Moskva", days : Int = 2) : WeatherResponseDTO?

    suspend fun getWeather() : WeatherResponseDTO?

    suspend fun findLocationByQuery(query : String, lang : String) : List<LocationResponseDTO?>?
}