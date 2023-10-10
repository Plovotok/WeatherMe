package ru.plovotok.weatherme.di

import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import ru.plovotok.weatherme.WeatherService
import ru.plovotok.weatherme.domain.repository.WeatherRepository
import ru.plovotok.weatherme.localstorage.ILocalStorage
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object WeatherModule {

    @Provides
    @Singleton
    fun provideWeatherService(localStorage : ILocalStorage, repository: WeatherRepository) : WeatherService{
        return WeatherService(localStorage, repository)
    }
}