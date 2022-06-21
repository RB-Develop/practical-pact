package com.bulsing.services

import com.bulsing.models.Product
import com.bulsing.models.productStorage
import io.ktor.client.*
import io.ktor.http.HttpStatusCode
import io.ktor.server.application.ApplicationCall
import io.ktor.server.response.respond
import io.ktor.server.response.respondText

class ProductService(
    private val httpClient: HttpClient,
    private val externalService: ExternalService = ExternalService("http://localhost:9001", httpClient)
) {
    init {
        productStorage.apply {
            add(Product("1", "speaker", 200))
            add(Product("2", "tv", 1000))
        }
    }

    suspend fun getAllProducts(call: ApplicationCall) {
        val externalProducts = externalService
            .getExternalSupply()
            .supplies
            .mapIndexed { index, supply -> Product("external-${index + 1}", supply.id, supply.unitPrice) }
        call.respond(productStorage.plus(externalProducts))
    }

    suspend fun getProduct(call: ApplicationCall) {
        val id = call.parameters["id"] ?: return call.respondText(
            "Missing id",
            status = HttpStatusCode.BadRequest
        )
        val product = productStorage
            .firstOrNull { it.name == id }
            ?: externalService
                .getExternalSupplyOf(id)
                .run { Product("external", id, unitPrice) }

        call.respond(product)
    }
}