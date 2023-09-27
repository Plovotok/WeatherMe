package ru.plovotok.weatherme.domain.models

import ru.plovotok.weatherme.data.models.LocationResponseDTO

data class LocationResponse(
    val id : Int,
    val name : String,
    val region : String,
    val country : String,
    val lat : Double,
    val lon : Double
) {
    fun toDTO() = LocationResponseDTO(id, name, region, country, lat, lon)
}