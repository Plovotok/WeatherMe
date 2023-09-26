package ru.plovotok.weatherme.data.repository

import android.util.Log
import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.engine.cio.CIO
import io.ktor.client.plugins.HttpTimeout
import io.ktor.client.plugins.contentnegotiation.ContentNegotiation
import io.ktor.client.plugins.logging.LogLevel
import io.ktor.client.plugins.logging.Logging
import io.ktor.client.request.get
import io.ktor.http.URLProtocol
import io.ktor.serialization.kotlinx.json.json
import io.ktor.util.StringValues
import io.ktor.util.StringValuesBuilder
import kotlinx.serialization.json.Json
import ru.plovotok.weatherme.data.models.WeatherResponseDTO
import ru.plovotok.weatherme.data.repository.KtorUtils.appendRequest
import ru.plovotok.weatherme.domain.repository.WeatherRepository
import ru.plovotok.weatherme.presentation.base.Constants

class WeatherRepositoryImpl : WeatherRepository {

    private val client = HttpClient(CIO) {
        install(HttpTimeout) {
            requestTimeoutMillis = 20_000L
        }

        install(ContentNegotiation) { json(Json {
            ignoreUnknownKeys = true
            coerceInputValues = false
        })}



        install(Logging) {
            level = LogLevel.ALL
        }

        engine {
            https {
                trustManager = TrustAllCertsManager()
            }
        }
    }


    override suspend fun getWeatherByQuery(q : String, days : Int): WeatherResponseDTO? {
        val response = client.get("${Constants.BASE_URL}?q=$q&key=eb86f070b9df43f9a6c80906231509&days=$days&aqi=no&alerts=no")
        Log.d("Ktor-client", response.body())

        val json = response.body<String>()
        val weatherResponse = JsonUtils.fromJson<WeatherResponseDTO>(json)
        Log.d("Ktor-client", weatherResponse.toString())

//        getWeather()

        return weatherResponse
    }

    override suspend fun getWeather(): WeatherResponseDTO? {

//        return client.get {
//            appendRequest(_host = "api.weatherapi.com/v1", _protocol = URLProtocol.HTTPS, path = "/forecast.json", stringValues = StringValues.build {
//                append("key", Constants.API_KEY)
//                append("q", "moscow")
//                append("days", "2")
//                append("aqi", "no")
//                append("alerts", "no")
//            })
//        }.body()

        return KtorClient.getSafely(
            host = "api.weatherapi.com",
            protocol = URLProtocol.HTTP,
            path = "/v1/forecast.json",
            stringValues = StringValues.build {
                append("q", "Moscow")
                append("key", Constants.API_KEY)
                append("days", "2")
                append("aqi", "no")
                append("alerts", "no")
            }
        )

    }
}