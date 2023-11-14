package ru.plovotok.weatherme.data.db

import androidx.room.Database
import androidx.room.RoomDatabase
import ru.plovotok.weatherme.data.models.roommodels.WeatherLocationEntity
import ru.plovotok.weatherme.domain.repository.room.WeatherLocationsDao

@Database(entities = [WeatherLocationEntity::class], version = 1, exportSchema = false)
abstract class AppDatabase : RoomDatabase(){

    abstract fun dao() : WeatherLocationsDao
    companion object {
        const val TAG = "App-Database"
    }

}