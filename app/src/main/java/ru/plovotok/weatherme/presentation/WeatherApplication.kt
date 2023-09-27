package ru.plovotok.weatherme.presentation

import android.app.Application
import io.ktor.http.URLProtocol
import ru.plovotok.weatherme.WeatherService
import ru.plovotok.weatherme.data.repository.KtorClient
import ru.plovotok.weatherme.localstorage.LocalStorage

class WeatherApplication : Application() {

    override fun onCreate() {
        super.onCreate()

        KtorClient.initialize(
            host = "api.weatherapi.com/v1",
            path = "forecast.json",
            protocol = URLProtocol.HTTPS
        )
        WeatherService.initialize()

        LocalStorage.init(applicationContext)
    }

}