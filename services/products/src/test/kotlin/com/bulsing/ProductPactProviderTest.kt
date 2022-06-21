package com.bulsing

import au.com.dius.pact.provider.junit5.HttpTestTarget
import au.com.dius.pact.provider.junit5.PactVerificationContext
import au.com.dius.pact.provider.junit5.PactVerificationInvocationContextProvider
import au.com.dius.pact.provider.junitsupport.Provider
import au.com.dius.pact.provider.junitsupport.State
import au.com.dius.pact.provider.junitsupport.loader.PactBroker
import au.com.dius.pact.provider.junitsupport.loader.PactBrokerAuth
import io.ktor.server.engine.ApplicationEngine
import io.ktor.server.engine.embeddedServer
import io.ktor.server.netty.Netty
import org.junit.jupiter.api.AfterAll
import org.junit.jupiter.api.BeforeAll
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.TestInstance
import org.junit.jupiter.api.TestTemplate
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
    private lateinit var productApplication: ApplicationEngine

    @BeforeAll
    fun setupApp() {
        productApplication = embeddedServer(Netty, port = port) {
            main()
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

    @State("products exist")
    fun toProductsExistState() {

    }
}