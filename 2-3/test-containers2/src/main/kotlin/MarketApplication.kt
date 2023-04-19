package main.kotlin

import io.ktor.http.*
import io.ktor.serialization.kotlinx.json.*
import io.ktor.server.application.*
import io.ktor.server.engine.*
import io.ktor.server.netty.*
import io.ktor.server.plugins.contentnegotiation.*
import io.ktor.server.response.*
import io.ktor.server.routing.*
import io.ktor.util.pipeline.*
import kotlinx.serialization.json.Json
import market.*

fun main() {
    embeddedServer(Netty, port=8080, host="0.0.0.0") {
        configureSerialization()
        configureAdministration()
        val market = InMemoryMarket()
        // <Sample data>
        market.addCompany(Company("Company1", Stocks(100, 1)))
        market.addCompany(Company("Company2", Stocks(10, 100500)))
        market.addUser(User("User1", 1000))
        market.addUser(User("User2", 300))
        // </Sample data>
        configureRouting(market)
    }.start(wait=true)
}

fun Application.configureSerialization() {
    install(ContentNegotiation) {
        json(Json {
            // Prettyprinted Json looks ugly in tests :(
//            prettyPrint = true
        })
    }
}

fun Application.configureAdministration() {
    install(ShutDownUrl.ApplicationCallPlugin) {
        shutDownUrl = "/ktor/application/shutdown"
        exitCodeSupplier = {0}
    }
}

typealias Handler<T> = suspend PipelineContext<*, ApplicationCall>.(T) -> Unit

suspend fun <T: Any> PipelineContext<*, ApplicationCall>.onParameter(
    name: String, validate: (String) -> T?, then: Handler<T>,
) {
    call.parameters[name]?.let(validate)?.let { then(it) }
        ?: call.respond(HttpStatusCode.BadRequest, "Bad $name")
}

suspend fun PipelineContext<*, ApplicationCall>.onString(name: String, then: Handler<String>) {
    onParameter(name, { it }, then)
}

suspend fun PipelineContext<*, ApplicationCall>.onInt(name: String, then: Handler<Int>) {
    onParameter(name, { it.toIntOrNull() }, then)
}

suspend fun PipelineContext<*, ApplicationCall>.onLong(name: String, then: Handler<Long>) {
    onParameter(name, { it.toLongOrNull() }, then)
}

fun Application.configureRouting(stockMarket: Market) {
    routing {
        getMethods(stockMarket)
        postMethods(stockMarket)
        putMethods(stockMarket)
    }
}

private fun Routing.putMethods(stockMarket: Market) {
    put("/adjustPrice/{companyId}/{newPrice}") {
        onInt("companyId") { companyId ->
            onLong("newPrice") { newPrice ->
                call.respond(stockMarket.adjustPrice(companyId, newPrice))
            }
        }
    }
}


private fun Routing.postMethods(stockMarket: Market) {
    post("/addCompany/{name}") {
        onString("name") { name ->
            val number = call.request.queryParameters["number"]?.toLong()?.takeIf { it >= 0 } ?: 0
            val price = call.request.queryParameters["price"]?.toLong()?.takeIf { it >= 0 } ?: 0
            call.respond(stockMarket.addCompany(Company(name, Stocks(number, price))))
        }
    }
    post("/addStocks/{companyId}/{count}") {
        onInt("companyId") { companyId ->
            onLong("count") { count ->
                call.respond(stockMarket.addStocks(companyId, count))
            }
        }
    }
    post("/addMoney/{userId}/{value}") {
        onInt("userId") { userId ->
            onLong("value") { value ->
                call.respond(stockMarket.addMoney(userId, value))
            }
        }
    }
    post("/addUser/{name}") {
        onString("name") { name ->
            val money = call.request.queryParameters["money"]?.toLong()?.takeIf { it >= 0 } ?: 0
            call.respond(stockMarket.addUser(User(name, money)))
        }
    }
    post("/purchase/{userId}/{companyId}/{count}/{price}") {
        System.err.println("here1")
        onInt("userId") { userId ->
            System.err.println("here2")
            onInt("companyId") { companyId ->
                System.err.println("here3")
                onLong("count") { count ->
                    System.err.println("here4")
                    onLong("price") { price ->
                        System.err.println("here5")
                        call.respond(stockMarket.purchase(userId, companyId, count, price))
                    }
                }
            }
        }
    }
    post("/sell/{userId}/{companyId}/{count}/{price}") {
        onInt("userId") { userId ->
            onInt("companyId") { companyId ->
                onLong("count") { count ->
                    onLong("price") { price ->
                        call.respond(stockMarket.sell(userId, companyId, count, price))
                    }
                }
            }
        }
    }
}

private fun Routing.getMethods(stockMarket: Market) {
    get("/getCompany/{companyId}") {
        onInt("companyId") {
            call.respond(stockMarket.getCompany(it) ?: return@onInt call.respond(HttpStatusCode.NotFound))
        }
    }
    get("/getUser/{userId}") {
        onInt("userId") {
            call.respond(stockMarket.getUser(it) ?:  return@onInt call.respond(HttpStatusCode.NotFound))
        }
    }
    get("/getCompaniesIdByUser/{userId}") {
        onInt("userId") {
            call.respond(stockMarket.getCompaniesIdByUser(it))
        }
    }
    get("/getHoldersIdByCompany/{companyId}") {
        onInt("companyId") {
            call.respond(stockMarket.getHoldersIdByCompany(it))
        }
    }
    get("/getUserStocks/{companyId}/{userId}") {
        onInt("companyId") { companyId ->
            onInt("userId") { userId ->
                call.respond(stockMarket.getUserStocks(companyId, userId))
            }
        }
    }
}