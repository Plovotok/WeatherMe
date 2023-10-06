package ru.plovotok.weatherme.data.db

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import ru.plovotok.weatherme.data.models.roommodels.WeatherLocationEntity
import ru.plovotok.weatherme.domain.repository.room.WeatherLocationsDao

@Database(entities = [WeatherLocationEntity::class], version = 1, exportSchema = false)
abstract class AppDatabase : RoomDatabase(){

    abstract fun dao() : WeatherLocationsDao

    companion object {
        @Volatile
        private var INSTANCE : AppDatabase? = null

        fun getInstance(appContext : Context) : AppDatabase {
            synchronized(this) {
                var instance = INSTANCE

                if (instance == null) {
                    instance = Room.databaseBuilder(
                        context = appContext,
                        klass = AppDatabase::class.java,
                        name = "locations"
                    )
                        .fallbackToDestructiveMigration()
                        .build()

                    INSTANCE = instance
                }
                return instance
            }
        }
    }
}