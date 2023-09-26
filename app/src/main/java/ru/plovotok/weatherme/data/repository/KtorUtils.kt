package ru.plovotok.weatherme.data.repository

import io.ktor.client.request.HttpRequestBuilder
import io.ktor.client.request.accept
import io.ktor.client.request.headers
import io.ktor.http.ContentType
import io.ktor.http.HeadersBuilder
import io.ktor.http.URLBuilder
import io.ktor.http.URLProtocol
import io.ktor.http.contentType
import io.ktor.http.path
import io.ktor.util.StringValues

object KtorUtils {

    private fun HeadersBuilder.appendHeaders() {
        val accept = "application/json"
//        append("auth-token", AUTH_TOKEN)
        append("Acccept", accept)

    }

    private fun URLBuilder.appendUrl(protocol: URLProtocol = URLProtocol.HTTPS, host: String, path: String) {
        this.protocol = protocol
        this.host = host

        path(path)
    }

    fun HttpRequestBuilder.appendRequest(_host: String, _protocol: URLProtocol, path: String, stringValues: StringValues) {
        url {
            appendUrl(path = path, host = _host, protocol = _protocol)
            parameters.appendAll(stringValues)
        }

//        headers {
//            appendHeaders()
//        }

        accept(ContentType.Application.Json)

        contentType(ContentType.Application.Json)
    }
}