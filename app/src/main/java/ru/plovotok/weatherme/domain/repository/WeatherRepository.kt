package ru.plovotok.weatherme.domain.repository

import ru.plovotok.weatherme.data.models.LocationResponseDTO
import ru.plovotok.weatherme.data.models.WeatherResponseDTO

interface WeatherRepository {

    suspend fun getWeatherByQuery(q : String = "55.944183039553764,37.76948432956476", days : Int = 2) : WeatherResponseDTO?

    suspend fun findLocationByQuery(query : String, lang : String) : List<LocationResponseDTO?>?
}