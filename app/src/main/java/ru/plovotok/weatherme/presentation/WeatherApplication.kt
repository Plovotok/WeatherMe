package ru.plovotok.weatherme.presentation

import android.app.Application
import io.ktor.http.URLProtocol
import ru.plovotok.weatherme.WeatherService
import ru.plovotok.weatherme.data.db.AppDatabase
import ru.plovotok.weatherme.data.repository.KtorClient
import ru.plovotok.weatherme.data.repository.LocationsRepository
import ru.plovotok.weatherme.localstorage.LocalStorage

class WeatherApplication : Application() {

    private val database by lazy {
        AppDatabase.getInstance(applicationContext)
    }

    val repository by lazy {
        LocationsRepository(database.dao())
    }


    override fun onCreate() {
        super.onCreate()

        LocalStorage.init(applicationContext)
        KtorClient.initialize(
            host = "api.weatherapi.com/v1",
            path = "forecast.json",
            protocol = URLProtocol.HTTPS
        )
        WeatherService.initialize()
        
    }

}