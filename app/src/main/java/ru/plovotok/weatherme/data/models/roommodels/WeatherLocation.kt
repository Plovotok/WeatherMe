package ru.plovotok.weatherme.data.models.roommodels

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "locations")
data class WeatherLocation(
    @PrimaryKey(autoGenerate = false)
    @ColumnInfo(name = "id")
    val id : Int,
    @ColumnInfo(name = "name")
    val name : String,
    @ColumnInfo(name = "region")
    val region : String,
    @ColumnInfo(name = "country")
    val country : String,
    @ColumnInfo(name = "lat")
    val lat : Double,
    @ColumnInfo(name = "lon")
    val lon : Double
)
