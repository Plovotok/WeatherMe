package ru.plovotok.weatherme.data.models.roommodels

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.Index
import androidx.room.PrimaryKey
import ru.plovotok.weatherme.domain.models.LocationResponse

@Entity(tableName = "locations", indices = [Index(value = ["remote_id"], unique = true)])
data class WeatherLocationEntity(
    @PrimaryKey(autoGenerate = true) val id : Int = 0,
    @ColumnInfo(name = "remote_id") val remoteId : Int,
    @ColumnInfo(name = "name") val name : String,
    @ColumnInfo(name = "region") val region : String,
    @ColumnInfo(name = "country") val country : String,
    @ColumnInfo(name = "lat") val lat : Double,
    @ColumnInfo(name = "lon") val lon : Double
) {
    fun toModel() = LocationResponse(remoteId,name, region, country, lat, lon)
}
