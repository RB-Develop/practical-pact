package com.bulsing.plugins

import com.bulsing.routes.CustomerRoutes
import io.ktor.server.application.*
import io.ktor.server.response.*
import io.ktor.server.routing.*

fun Application.configureRouting() {
    // Starting point for a Ktor app:
    routing {
        get("/") {
            call.respondText("Hello World!")
        }
        CustomerRoutes().apply { customerRouting() }
    }
}
