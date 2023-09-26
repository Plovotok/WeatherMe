package ru.plovotok.weatherme.localstorage

interface ILocalStorage {

    fun save(key: String, data: String?)

    fun has(key: String): Boolean

    fun get(key: String): String?

    fun clear()

}