package com.bulsing.services

import com.bulsing.models.ExternalSupply
import com.bulsing.models.ExternalSupplyListing
import io.ktor.client.*
import io.ktor.client.call.body
import io.ktor.client.engine.cio.*
import io.ktor.client.request.*
import io.ktor.serialization.kotlinx.json.*
import io.ktor.client.plugins.contentnegotiation.ContentNegotiation

class ExternalService(
    private val rootUrl: String,
) {
    private val httpClient: HttpClient = HttpClient(CIO) {
        install(ContentNegotiation) {
            json()
        }
    }

    suspend fun getExternalSupply(): ExternalSupplyListing {
        return httpClient.get("$rootUrl/supply").body()
    }

    suspend fun getExternalSupplyOf(id: String): ExternalSupply {
        return httpClient.get("$rootUrl/supply/$id").body()
    }
}