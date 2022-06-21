package com.bulsing

import au.com.dius.pact.provider.junit5.HttpTestTarget
import au.com.dius.pact.provider.junit5.PactVerificationContext
import au.com.dius.pact.provider.junit5.PactVerificationInvocationContextProvider
import au.com.dius.pact.provider.junitsupport.Provider
import au.com.dius.pact.provider.junitsupport.State
import au.com.dius.pact.provider.junitsupport.loader.PactBroker
import au.com.dius.pact.provider.junitsupport.loader.PactBrokerAuth
import io.ktor.client.*
import io.ktor.client.engine.mock.*
import io.ktor.client.plugins.contentnegotiation.*
import io.ktor.http.*
import io.ktor.serialization.kotlinx.json.*
import io.ktor.server.engine.*
import io.ktor.server.netty.*
import org.junit.jupiter.api.*
import org.junit.jupiter.api.extension.ExtendWith

@Provider("ProductService")
@PactBroker(
    host = "localhost",
    port = "8000",
    authentication = PactBrokerAuth(username = "pact_workshop", password = "pact_workshop")
)
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
class ProductPactProviderTest {
    private val port: Int = 8050
    private val responseHeaders = headersOf("Content-Type" to listOf(ContentType.Application.Json.toString()))
    private lateinit var productApplication: ApplicationEngine


    @BeforeAll
    fun setupApp() {
        productApplication = embeddedServer(Netty, port = port) {
            main(HttpClient(MockEngine) {
                install(ContentNegotiation) {
                    json()
                }
                engine {
                    addHandler { request ->
                        if (request.url.encodedPath == "/supply") {
                            respond(
                                """
                                {
                                    "supplies": [
                                        {
                                            "id": "orange",
                                            "quantity": 10,
                                            "unitPrice": 10
                                        },
                                        {
                                            "id": "chairs",
                                            "quantity": 10,
                                            "unitPrice": 10
                                        }
                                    ]
                                }
                                """, HttpStatusCode.OK, responseHeaders
                            )
                        } else {
                            println(request.url.encodedPath)
                            respond("Not matched", HttpStatusCode.NotFound, responseHeaders)
                        }
                    }
                }
            })
        }
        productApplication.start(wait = false)
        Thread.sleep(50)
    }

    @AfterAll
    fun tearDownApp() {
        productApplication.stop(1000, 2000)
    }

    @BeforeEach
    fun setUp(context: PactVerificationContext) {
        context.target = HttpTestTarget("localhost", port);
    }

    @TestTemplate
    @ExtendWith(PactVerificationInvocationContextProvider::class)
    fun verifyPact(context: PactVerificationContext) {
        context.verifyInteraction();
    }

    @State("product with name speaker exists")
    fun toProductSpeakerExists() {
        // Already exists
    }

    @State("product with name not-exists does not exist")
    fun toProductNotExistingState() {
        // Configure the application for this state
    }

    @State("products exist")
    fun toProductsExistState() {

    }
}