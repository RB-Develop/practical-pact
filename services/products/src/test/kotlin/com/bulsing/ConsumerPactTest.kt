package com.bulsing

import au.com.dius.pact.consumer.MockServer
import au.com.dius.pact.consumer.dsl.LambdaDslJsonArray
import au.com.dius.pact.consumer.dsl.LambdaDslObject
import au.com.dius.pact.consumer.dsl.PactBuilder
import au.com.dius.pact.consumer.dsl.newJsonObject
import au.com.dius.pact.consumer.junit5.PactConsumerTestExt
import au.com.dius.pact.consumer.junit5.PactTestFor
import au.com.dius.pact.core.model.V4Pact
import au.com.dius.pact.core.model.annotations.Pact
import com.bulsing.services.ExternalService
import io.ktor.client.*
import io.ktor.client.engine.cio.*
import io.ktor.client.plugins.contentnegotiation.*
import io.ktor.serialization.kotlinx.json.*
import kotlinx.coroutines.runBlocking
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.extension.ExtendWith
import kotlin.collections.HashMap
import kotlin.collections.Map
import kotlin.collections.set
import kotlin.test.assertEquals


@ExtendWith(PactConsumerTestExt::class)
class ConsumerPactTest {
    @Pact(consumer = "ProductService", provider = "SupplierService")
    fun getAllProducts(builder: PactBuilder): V4Pact {
        return builder
            .usingLegacyDsl()
            .given("products exist")
            .uponReceiving("get all products")
            .method("GET")
            .path("/supply")
            .willRespondWith()
            .status(200)
            .headers(headers())
            .body(
                newJsonObject {
                    array("supplies") { array: LambdaDslJsonArray ->
                        array.`object` { `object`: LambdaDslObject ->
                            `object`.stringType("id", "chair")
                            `object`.integerType("quantity", 10)
                            `object`.integerType("unitPrice", 50)
                        }
                        array.`object` { `object`: LambdaDslObject ->
                            `object`.stringType("id", "orange")
                            `object`.integerType("quantity", 20)
                            `object`.integerType("unitPrice", 2)
                        }
                    }
                }
            ).toPact(V4Pact::class.java)
    }

    @Test
    @PactTestFor(pactMethod = "getAllProducts")
    fun getAllProducts_whenProductsExist(mockServer: MockServer) = runBlocking {
        val response = ExternalService(mockServer.getUrl(), HttpClient(CIO) {
            install(ContentNegotiation) {
                json()
            }
        }).getExternalSupply()
        assertEquals(2, response.supplies.size)
    }

    private fun headers(): Map<String, String> {
        return HashMap<String, String>().apply {
            this["Content-Type"] = "application/json"
        }
    }
}