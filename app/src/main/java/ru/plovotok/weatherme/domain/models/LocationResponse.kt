package ru.plovotok.weatherme.domain.models

import ru.plovotok.weatherme.data.models.LocationResponseDTO
import ru.plovotok.weatherme.data.models.roommodels.WeatherLocationEntity

data class LocationResponse(
    val id : Int,
    val name : String,
    val region : String,
    val country : String,
    val lat : Double,
    val lon : Double
) {
    fun toDTO() = LocationResponseDTO(id, name, region, country, lat, lon)

    fun toDBEntity() = WeatherLocationEntity(
        remoteId = id, name = name,
        region = region, country = country,
        lat = lat, lon = lon
    )
}