package com.bulsing

import io.ktor.server.engine.*
import io.ktor.server.netty.*
import com.bulsing.plugins.*

fun main() {
    embeddedServer(Netty, port = 9001, host = "0.0.0.0") {
        configureRouting()
        configureSerialization()
    }.start(wait = true)
}
