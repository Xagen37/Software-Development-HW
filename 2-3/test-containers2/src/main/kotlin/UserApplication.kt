package main.kotlin

import io.ktor.client.*
import io.ktor.server.engine.*
import io.ktor.server.netty.*
import main.kotlin.user.configureRouting

fun main() {
    embeddedServer(Netty, port = 9090, host = "0.0.0.0") {
        configureRouting(HttpClient())
    }.start(wait = true)
}