package ru.plovotok.weatherme.localstorage

import android.content.Context
import android.content.SharedPreferences

class LocalStorage(
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

        private const val TAG = "local-storage"
        private var INSTANCE: LocalStorage? = null

        const val API_TOKEN = "api-token"
        const val LAST_LOCATION = "last-location"
        const val FAVOURITE_LOCATION = "favourite-location"
        const val LOCATIONS_LIST = "locations-list"

        fun init(context: Context) {
            val sharedPreferences = context.getSharedPreferences(TAG, Context.MODE_PRIVATE)
            INSTANCE = LocalStorage(sharedPreferences = sharedPreferences)
        }

        fun newInstance(): LocalStorage {
            if (INSTANCE != null) return INSTANCE!!
            throw Exception("LocalStorage is not initialized. Use LocalStorage.init(...)")
        }

    }

}