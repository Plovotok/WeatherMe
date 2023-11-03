package ru.plovotok.weatherme.data.repository

import android.util.Log
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.engine.cio.CIO
import io.ktor.client.plugins.HttpTimeout
import io.ktor.client.plugins.contentnegotiation.ContentNegotiation
import io.ktor.client.plugins.logging.LogLevel
import io.ktor.client.plugins.logging.Logging
import io.ktor.client.request.get
import io.ktor.client.statement.request
import io.ktor.serialization.kotlinx.json.json
import kotlinx.serialization.json.Json
import ru.plovotok.weatherme.data.models.LocationResponseDTO
import ru.plovotok.weatherme.data.models.WeatherResponseDTO
import ru.plovotok.weatherme.domain.repository.WeatherRepository
import ru.plovotok.weatherme.presentation.base.Constants
import java.util.Locale
import javax.inject.Inject

class WeatherRepositoryImpl @Inject constructor() : WeatherRepository {

    private val client = HttpClient(CIO) {
        install(HttpTimeout) {
            requestTimeoutMillis = 20_000L
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

        engine {
//            https {
//                trustManager = TrustAllCertsManager()
//            }
        }
    }


    override suspend fun getWeatherByQuery(q : String, days : Int): WeatherResponseDTO? {
        val lang = Locale.getDefault().language
        val response = client.get("${Constants.BASE_URL}?q=$q&key=${Constants.API_KEY}&days=$days&aqi=no&alerts=no&lang=$lang")
//        Log.d("Ktor-client", response.body())
        Log.d("Ktor-client", response.request.url.encodedPathAndQuery)

        val json = response.body<String>()
        val weatherResponse = JsonUtils.fromJson<WeatherResponseDTO>(json)
        Log.d("Ktor-client", weatherResponse.toString())

        return weatherResponse
    }

    override suspend fun findLocationByQuery(query: String, lang : String): List<LocationResponseDTO?>? {
        Log.d("Ktor-client", "$query, $lang")
        val response = client.get("${Constants.SEARCH_URL}?key=${Constants.API_KEY}&q=$query")
        Log.d("Ktor-client", response.request.url.encodedPathAndQuery)
        Log.d("Ktor-client", "response body ----> ${response.body<String>()}")
//        Log.d("Ktor-client", response.request.toString())

        val json = response.body<String>()
        val listType = object : TypeToken<List<LocationResponseDTO>>() {}.type
        return Gson().fromJson(json, listType)
    }
}