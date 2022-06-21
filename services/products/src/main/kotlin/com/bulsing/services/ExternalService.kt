package com.bulsing.services

import com.bulsing.models.ExternalSupply
import com.bulsing.models.ExternalSupplyListing
import io.ktor.client.*
import io.ktor.client.call.*
import io.ktor.client.request.*

class ExternalService(
    private val rootUrl: String,
    private val httpClient: HttpClient
) {
    suspend fun getExternalSupply(): ExternalSupplyListing {
        return httpClient.get("$rootUrl/supply").body()
    }

    suspend fun getExternalSupplyOf(id: String): ExternalSupply {
        return httpClient.get("$rootUrl/supply/$id").body()
    }
}