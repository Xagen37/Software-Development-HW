package main.kotlin.user

import io.ktor.client.*
import io.ktor.client.request.*
import io.ktor.client.statement.*
import io.ktor.server.application.*
import io.ktor.server.request.*
import io.ktor.server.response.*
import io.ktor.server.routing.*

val posts = listOf(
    "addMoney",
    "addUser",
    "purchase",
    "sell"
)

val gets = listOf(
    "getCompany",
    "getUser",
    "getCompaniesIdByUser",
    "getHoldersIdByCompany",
    "getUserStocks"
)

const val serverAddr = "http://127.0.0.1:8080"

fun Application.configureRouting(client: HttpClient) {
    routing {
        for (currGet in gets) {
            get("/$currGet/{param...}") {
                call.respondText(client.get(serverAddr + call.request.uri).bodyAsText())
            }
        }
        for (currPost in posts) {
            post("/$currPost/{param...}") {
                call.respondText(client.post(serverAddr + call.request.uri).bodyAsText())
            }
        }
    }
}