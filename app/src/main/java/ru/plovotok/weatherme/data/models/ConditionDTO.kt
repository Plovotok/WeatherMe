package ru.plovotok.weatherme.data.models

import com.google.gson.annotations.SerializedName
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class ConditionDTO(
    @SerializedName("text")
    val text : String,
    @SerializedName("code")
    val code : Int
)