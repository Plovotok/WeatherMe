package ru.plovotok.weatherme.di

import android.content.Context
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import ru.plovotok.weatherme.localstorage.ILocalStorage
import ru.plovotok.weatherme.localstorage.LocalStorage
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
class LocalStorageModule {

    @Provides
    @Singleton
    fun provideLocalStorage(@ApplicationContext appContext : Context) : ILocalStorage {
        val preferences = appContext.getSharedPreferences(LocalStorage.TAG, Context.MODE_PRIVATE)
        return LocalStorage(preferences)
    }
}