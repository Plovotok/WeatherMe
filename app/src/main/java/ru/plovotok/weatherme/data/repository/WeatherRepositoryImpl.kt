package ru.plovotok.weatherme.data.repository

import android.util.Log
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.request.get
import io.ktor.client.statement.request
import ru.plovotok.weatherme.data.models.LocationResponseDTO
import ru.plovotok.weatherme.data.models.WeatherResponseDTO
import ru.plovotok.weatherme.domain.repository.WeatherRepository
import ru.plovotok.weatherme.presentation.base.Constants
import java.nio.channels.UnresolvedAddressException
import java.util.Locale
import javax.inject.Inject

class WeatherRepositoryImpl @Inject constructor(private val client : HttpClient) : WeatherRepository {


    override suspend fun getWeatherByQuery(q : String, days : Int): WeatherResponseDTO? {

        return try {
            val lang = Locale.getDefault().language
            val response = client.get("${Constants.BASE_URL}?q=$q&key=${Constants.API_KEY}&days=$days&aqi=no&alerts=no&lang=$lang")
    //        Log.d("Ktor-client", response.body())
            Log.d("Ktor-client", response.request.url.encodedPathAndQuery)

            val json = response.body<String>()
            val weatherResponse = JsonUtils.fromJson<WeatherResponseDTO>(json)
            Log.d("Ktor-client", weatherResponse.toString())

            weatherResponse
        } catch (e: UnresolvedAddressException) {
            null
        }

    }

    override suspend fun findLocationByQuery(query: String, lang : String): List<LocationResponseDTO?>? {

        return try {
            Log.d("Ktor-client", "$query, $lang")
            val response = client.get("${Constants.SEARCH_URL}?key=${Constants.API_KEY}&q=$query")
            Log.d("Ktor-client", response.request.url.encodedPathAndQuery)
            Log.d("Ktor-client", "response body ----> ${response.body<String>()}")
//        Log.d("Ktor-client", response.request.toString())

            val json = response.body<String>()
            val listType = object : TypeToken<List<LocationResponseDTO>>() {}.type

            Gson().fromJson(json, listType)
        } catch (e: Exception) {
            e.printStackTrace()
            null
        }

    }
}