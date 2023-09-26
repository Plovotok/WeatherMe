package ru.plovotok.weatherme.data.repository

import com.google.gson.Gson
import com.google.gson.GsonBuilder

object JsonUtils {

    val gson = GsonBuilder()
        .setLenient()
        .create()

    fun Any.toJson(): String = gson.toJson(this)


    inline fun <reified T> fromJson(string : String) = gson.fromJson(string, T::class.java)

    fun gson(): Gson = gson

}