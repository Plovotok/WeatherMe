package ru.plovotok.weatherme.data.models

import com.google.gson.annotations.SerializedName
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class AstroDTO(
    @SerializedName("sunrise")
    val sunrise : String,
    @SerializedName("sunset")
    val sunset : String
)
