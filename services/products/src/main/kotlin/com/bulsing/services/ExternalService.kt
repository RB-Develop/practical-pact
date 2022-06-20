package com.bulsing.services

import io.ktor.client.*
import io.ktor.client.engine.cio.*
import io.ktor.client.request.*

class ExternalService(
    private val rootUrl: String,
) {
    private val httpClient: HttpClient = HttpClient(CIO)

    suspend fun doCall() {
        httpClient.get("$rootUrl/products")
    }
}