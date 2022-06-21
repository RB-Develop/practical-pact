package com.bulsing.plugins

import com.bulsing.routes.ProductRoutes
import io.ktor.client.*
import io.ktor.server.application.*
import io.ktor.server.response.*
import io.ktor.server.routing.*

fun Application.configureRouting(httpClient: HttpClient) {
    // Starting point for a Ktor app:
    routing {
        get("/") {
            call.respondText("Hello World!")
        }
        ProductRoutes(httpClient).apply { customerRouting() }
    }
}
