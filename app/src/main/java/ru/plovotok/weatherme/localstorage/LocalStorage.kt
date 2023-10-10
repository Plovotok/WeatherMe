package ru.plovotok.weatherme.localstorage

import android.content.SharedPreferences
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class LocalStorage @Inject constructor(
    private val sharedPreferences: SharedPreferences
) : ILocalStorage {

    override fun save(key: String, data: String?) {
        sharedPreferences.edit().apply {
            putString(key, data)
        }.apply()
    }

    override fun has(key: String): Boolean =
        sharedPreferences.getString(key, null) != null

    override fun get(key: String): String? =
        sharedPreferences.getString(key, null)

    override fun clear() {
        sharedPreferences.edit().apply {
            save(API_TOKEN, null)
            save(LAST_LOCATION, null)
            apply()
        }
    }

    companion object {

        const val TAG = "local-storage"

        const val API_TOKEN = "api-token"
        const val LAST_LOCATION = "last-location"
        const val FAVOURITE_LOCATION = "favourite-location"

    }

}