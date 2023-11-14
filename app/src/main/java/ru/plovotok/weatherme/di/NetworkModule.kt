package ru.plovotok.weatherme.di

import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import io.ktor.client.HttpClient
import io.ktor.client.engine.cio.CIO
import io.ktor.client.plugins.HttpTimeout
import io.ktor.client.plugins.contentnegotiation.ContentNegotiation
import io.ktor.client.plugins.logging.LogLevel
import io.ktor.client.plugins.logging.Logging
import io.ktor.serialization.kotlinx.json.json
import kotlinx.serialization.json.Json
import ru.plovotok.weatherme.data.repository.WeatherRepositoryImpl
import ru.plovotok.weatherme.domain.repository.WeatherRepository
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
class NetworkModule {

    @Provides
    fun provideWeatherRepository(client: HttpClient) : WeatherRepository{
        return WeatherRepositoryImpl(client)
    }

    @Provides
    @Singleton
    fun provideClient() : HttpClient{
        return HttpClient(CIO) {
            install(HttpTimeout) {
                requestTimeoutMillis = 10_000L
                connectTimeoutMillis = 10_000L
            }

            install(ContentNegotiation) { json(Json {
                ignoreUnknownKeys = true
                coerceInputValues = false
                prettyPrint = true
                isLenient = true
            })}

            install(Logging) {
                level = LogLevel.ALL
            }
        }
    }

}