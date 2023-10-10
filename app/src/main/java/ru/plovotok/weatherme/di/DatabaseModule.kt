package ru.plovotok.weatherme.di

import android.content.Context
import androidx.room.Room
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import ru.plovotok.weatherme.data.db.AppDatabase
import ru.plovotok.weatherme.domain.repository.room.WeatherLocationsDao

@Module
@InstallIn(SingletonComponent::class)
object DatabaseModule {

    @Provides
    fun provideAppDatabase(@ApplicationContext appContext : Context) : AppDatabase {
        return Room.databaseBuilder(
            context = appContext,
            klass = AppDatabase::class.java,
            name = "locations"
        )
            .fallbackToDestructiveMigration()
            .build()
    }

    @Provides
    fun provideDao(database: AppDatabase) : WeatherLocationsDao {
        return database.dao()
    }
}