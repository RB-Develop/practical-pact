package com.bulsing

import io.ktor.server.engine.*
import io.ktor.server.netty.*
import com.bulsing.plugins.*
import io.ktor.server.application.Application

fun Application.main() {
    configureRouting()
    configureSerialization()
}

fun main() {
    embeddedServer(Netty, port = 9001, host = "0.0.0.0") {
        main()
    }.start(wait = true)
}
