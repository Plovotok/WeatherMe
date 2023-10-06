package ru.plovotok.weatherme.domain.repository.room

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import ru.plovotok.weatherme.data.models.roommodels.WeatherLocationEntity

@Dao
interface WeatherLocationsDao {
    @Query("SELECT * FROM locations ")
    fun getAllLocations() : List<WeatherLocationEntity>?

    @Query("SELECT * FROM locations WHERE remote_id = :remoteId")
    fun getWeatherLocationById(remoteId : Int) : WeatherLocationEntity?

    @Query("DELETE FROM locations WHERE remote_id = :id ")
    suspend fun deleteLocationById(id : Int)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun addLocation(location : WeatherLocationEntity)
}