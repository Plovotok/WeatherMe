package ru.plovotok.weatherme.data.models

import kotlinx.serialization.Serializable

@Serializable
data class LocationDTO(
    val name : String,
    val region : String,
    val country : String,
    val localtime : String
)
