package ru.plovotok.weatherme.di

import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import ru.plovotok.weatherme.data.repository.WeatherRepositoryImpl
import ru.plovotok.weatherme.domain.repository.WeatherRepository

@Module
@InstallIn(SingletonComponent::class)
class NetworkModule {

    @Provides
    fun provideWeatherRepository() : WeatherRepository{
        return WeatherRepositoryImpl()
    }
}