package com.bulsing


import com.bulsing.plugins.configureRouting
import com.bulsing.plugins.configureSerialization
import io.ktor.server.engine.embeddedServer
import io.ktor.server.netty.Netty

fun main() {
    embeddedServer(Netty, port = 9000, host = "0.0.0.0") {
        configureRouting()
        configureSerialization()
    }.start(wait = true)


}
