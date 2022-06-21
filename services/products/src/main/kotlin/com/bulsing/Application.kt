package com.bulsing


import com.bulsing.plugins.configureRouting
import com.bulsing.plugins.configureSerialization
import io.ktor.server.application.Application
import io.ktor.server.engine.embeddedServer
import io.ktor.server.netty.Netty

fun Application.main() {
    configureRouting()
    configureSerialization()
}

fun main() {
    embeddedServer(Netty, port = 9000, host = "0.0.0.0") {
        main()
    }.start(wait = true)
}
