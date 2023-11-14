package ru.plovotok.weatherme.localstorage

import android.content.Context
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

        private var INSTANCE: LocalStorage? = null
        const val TAG = "local-storage"

        const val API_TOKEN = "api-token"
        const val LAST_LOCATION = "last-location"
        const val FAVOURITE_LOCATION = "favourite-location"

        fun init(context: Context) {
            val sharedPreferences = context.getSharedPreferences(TAG, Context.MODE_PRIVATE)
            if (INSTANCE == null) {
                INSTANCE = LocalStorage(sharedPreferences = sharedPreferences)
            }

        }

        fun newInstance(): LocalStorage {
            if (INSTANCE != null) return INSTANCE!!
            throw Exception("LocalStorage is not initialized. Use LocalStorage.init(...)")
        }

    }

}