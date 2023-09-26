package ru.plovotok.weatherme.data.repository

import android.util.Log
import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.engine.android.Android
import io.ktor.client.engine.cio.CIO
import io.ktor.client.plugins.HttpTimeout
import io.ktor.client.plugins.contentnegotiation.ContentNegotiation
import io.ktor.client.plugins.logging.LogLevel
import io.ktor.client.plugins.logging.Logging
import io.ktor.client.request.accept
import io.ktor.client.request.get
import io.ktor.client.request.post
import io.ktor.client.request.setBody
import io.ktor.client.statement.HttpResponse
import io.ktor.client.statement.bodyAsText
import io.ktor.client.statement.request
import io.ktor.http.ContentType
import io.ktor.http.URLProtocol
import io.ktor.serialization.kotlinx.json.json
import io.ktor.util.StringValues
import kotlinx.serialization.json.Json
import ru.plovotok.weatherme.data.repository.KtorUtils.appendRequest

object KtorClient {

    const val TAG = "Ktor-client"
    private const val REQUEST_TIMEOUT = 20_000L
    private lateinit var client: HttpClient

    var HOST = "api.weatherapi.com"
    var PROTOCOL = URLProtocol.HTTP
    var PATH = "/v1/forecast.json"

    fun initialize (
        host : String,
        path : String,
        protocol : URLProtocol = URLProtocol.HTTPS
    ) {
        Log.i(TAG, "initialized.")
        HOST = host
        PATH = path
        PROTOCOL = protocol

        client = HttpClient(Android) {
            install(HttpTimeout) {
                requestTimeoutMillis = REQUEST_TIMEOUT
            }

            install(ContentNegotiation) { json(Json {
                ignoreUnknownKeys = true
                coerceInputValues = false
            })}



            install(Logging) {
                level = LogLevel.ALL
            }

//            engine {
//                https {
//                    trustManager = TrustAllCertsManager()
//                }
//            }
        }
    }


    private fun getClient(): HttpClient = client

    suspend fun rawGet(host: String = HOST, protocol: URLProtocol = PROTOCOL, path: String, body: Any = "", query: StringValues = StringValues.Empty): HttpResponse? {
        Log.w(TAG,
            "\n" +
                    "------------------->\n" +
                    "GET: ${protocol.name}://$host$path${query.entries()}\n" +
                    "QUERY: ${query.entries()}\n" +
                    "BODY: $body"
        )
        val response = getClient().get {
            accept(ContentType.Application.Json)
            appendRequest(path = path, stringValues = query, _host = host, _protocol = protocol)
//            setBody(body)
        }

        Log.w(TAG,
            "<------------------- ${protocol.name}://$host$path ${response.status}\n" +
                    "${response.body<String>()}" +
                    "BODY: ${response.bodyAsText()}\n" +
                    "HEADER: ${response.headers}"
        )
        return response
    }

    suspend inline fun <reified T> getSafely(host: String = HOST, protocol: URLProtocol = PROTOCOL, path: String, body: Any = "", stringValues: StringValues = StringValues.Empty): T? {
        return try { rawGet(host = host, protocol = protocol, path = path, body = body, query = stringValues)?.body() } catch (e: Exception) {
            Log.e(TAG,
                "\n" +
                        "<-------------------\n${protocol.name}://$host$path\n${e.javaClass}\n${e.message}" +
                        e.printStackTrace()
            )
            return null
        }
    }

    suspend fun rawPost(host: String = HOST, protocol: URLProtocol = PROTOCOL, path: String, body: Any = "", query: StringValues = StringValues.Empty): HttpResponse? {
        Log.w(TAG,
            "\n" +
                    "------------------->\n" +
                    "POST: ${protocol.name}://$host$path\n" +
                    "QUERY: ${query.entries()}\n" +
                    "BODY: $body"
        )
        val response = getClient().post {
//            appendRequest(path = path, stringValues = query, _host = host, _protocol = protocol)
            setBody(body)
        }

        Log.w(TAG,
            "<------------------- ${protocol.name}://$host$path ${response.status}\n" +
                    "BODY: ${response.bodyAsText()}\n" +
                    "HEADER: ${response.headers}"
        )
        return response
    }

    suspend inline fun <reified T> postSafely(host: String = HOST, protocol: URLProtocol = PROTOCOL, path: String, body: Any = "", stringValues: StringValues = StringValues.Empty): T? {
        return try { rawPost(host = host, protocol = protocol, path = path, body = body, query = stringValues)?.body() } catch (e: Exception) {
            Log.e(TAG,
                "\n" +
                        "<-------------------\n${protocol.name}://$host$path\n${e.javaClass}\n" +
                        e.printStackTrace()
            )
            return null
        }
    }


    fun closeClient() {
        client.close()
    }


}