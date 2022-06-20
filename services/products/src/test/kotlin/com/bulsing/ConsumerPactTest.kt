package com.bulsing

import au.com.dius.pact.consumer.MockServer
import au.com.dius.pact.consumer.dsl.LambdaDsl.newJsonArrayMinLike
import au.com.dius.pact.consumer.dsl.LambdaDslJsonArray
import au.com.dius.pact.consumer.dsl.LambdaDslObject
import au.com.dius.pact.consumer.dsl.PactBuilder
import au.com.dius.pact.consumer.junit5.PactConsumerTestExt
import au.com.dius.pact.consumer.junit5.PactTestFor
import au.com.dius.pact.core.model.V4Pact
import au.com.dius.pact.core.model.annotations.Pact
import com.bulsing.services.ExternalService
import kotlinx.coroutines.runBlocking
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.extension.ExtendWith
import kotlin.test.assertEquals


@ExtendWith(PactConsumerTestExt::class)
class ConsumerPactTest {
    @Pact(consumer = "FrontendApplication", provider = "ProductService")
    fun getAllProducts(builder: PactBuilder): V4Pact {
        return builder
            .usingLegacyDsl()
            .given("products exist")
            .uponReceiving("get all products")
            .method("GET")
            .path("/products")
            .willRespondWith()
            .status(200)
            .headers(headers())
            .body(
                newJsonArrayMinLike(
                    2
                ) { array: LambdaDslJsonArray ->
                    array.`object` { `object`: LambdaDslObject ->
                        `object`.stringType("id", "09")
                        `object`.stringType("type", "CREDIT_CARD")
                        `object`.stringType("name", "Gem Visa")
                    }
                }.build()
            )
            .toPact(V4Pact::class.java)
    }

    @Test
    @PactTestFor(pactMethod = "getAllProducts")
    fun getAllProducts_whenProductsExist(mockServer: MockServer) = runBlocking {
        ExternalService(mockServer.getUrl()).doCall()
        assertEquals(1, 1)
    }

    private fun headers(): Map<String, String> {
        return HashMap<String, String>().apply {
            this["Content-Type"] = "application/json; charset=utf-8"
        }
    }
}