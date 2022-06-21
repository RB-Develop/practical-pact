package com.bulsing


import com.bulsing.plugins.configureCors
import com.bulsing.plugins.configureRouting
import com.bulsing.plugins.configureSerialization
import io.ktor.client.*
import io.ktor.client.engine.cio.*
import io.ktor.client.plugins.contentnegotiation.*
import io.ktor.serialization.kotlinx.json.*
import io.ktor.server.application.Application
import io.ktor.server.engine.embeddedServer
import io.ktor.server.netty.Netty

fun Application.main(httpClient: HttpClient) {
    configureRouting(httpClient)
    configureSerialization()
    configureCors()
}

fun main() {
    embeddedServer(Netty, port = 9000, host = "0.0.0.0") {
        main(HttpClient(CIO) {
            install(ContentNegotiation) {
                json()
            }
        })
    }.start(wait = true)
}
