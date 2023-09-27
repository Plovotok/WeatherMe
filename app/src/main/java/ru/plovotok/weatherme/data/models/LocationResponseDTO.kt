package ru.plovotok.weatherme.data.models

import com.google.gson.annotations.SerializedName
import kotlinx.serialization.Serializable

@Serializable
data class LocationResponseDTO(
    @SerializedName("id")
    val id : Int,
    @SerializedName("name")
    val name : String,
    @SerializedName("region")
    val region : String,
    @SerializedName("country")
    val country : String,
    @SerializedName("lat")
    val lat : Double,
    @SerializedName("lon")
    val lon : Double
)
