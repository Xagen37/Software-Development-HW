package response

import db.findUser
import db.getProducts
import db.save
import io.netty.buffer.ByteBuf
import io.netty.handler.codec.http.HttpResponseStatus
import io.netty.handler.codec.http.cookie.DefaultCookie
import io.reactivex.netty.protocol.http.server.HttpServerRequest
import io.reactivex.netty.protocol.http.server.HttpServerResponse
import io.reactivex.netty.protocol.http.server.ResponseContentWriter
import model.Currency
import model.Product
import model.User
import rx.Observable
import rx.Subscriber
import java.nio.charset.Charset

object GetResponseHandler {
    fun request(
        request: HttpServerRequest<ByteBuf>,
        response: HttpServerResponse<ByteBuf>
    ): ResponseContentWriter<ByteBuf> {
        return when (request.decodedPath) {
            "/" -> response.writeString(
                getProducts().toList().map {
                    val oldValues = request.cookies["wallets"]
                    val values = if (oldValues == null || oldValues.size == 0) {
                        Currency.RUB
                    } else {
                        Currency.valueOf(oldValues.first().value())
                    }
                    index(it, values)
                }
            )
            "/register" -> response.writeString(
                Observable.fromCallable {
                    register()
                }
            )
            "/login" -> response.writeString(
                Observable.fromCallable {
                    login()
                }
            )
            "/addProduct" -> response.writeString(
                Observable.fromCallable {
                    addProduct()
                }
            )
            else -> throw IllegalStateException()
        }
    }
}

fun getArgs(request: HttpServerRequest<ByteBuf>) =
    request.content.map {
        val data = it.readCharSequence(request.contentLength.toInt(), Charset.defaultCharset())
        data.split("&").associate {
            val sp = it.split("=")
            sp[0] to sp[1]
        }
    }

fun writeString(
    response: HttpServerResponse<ByteBuf>,
    message: String,
    subscriber: Subscriber<in Void>
) {
    response.apply {
        status = HttpResponseStatus.OK
        writeString(Observable.just(message)).unsafeSubscribe(subscriber)
    }
}

object PostResponseHandler {
    fun request(
        request: HttpServerRequest<ByteBuf>,
        response: HttpServerResponse<ByteBuf>
    ): Observable<Void> {
        return when (request.decodedPath) {
            "/register" -> object : Observable<Void>({ subscribe ->
                subscribe.add(
                    getArgs(request).subscribe {
                        val login = it["login"]!!
                        val wallets = it["wallets"]!!
                        subscribe.add(
                            findUser(login).subscribe({
                                writeString(response, "Already exists", subscribe)
                            }) {
                                User(login, Currency.valueOf(wallets)).save().subscribe()
                                response.addCookie(DefaultCookie("login", login))
                                response.addCookie(DefaultCookie("wallets", wallets))
                                writeString(response, "OK!", subscribe)
                            }
                        )
                    }
                )
            }
            ) {}
            "/login" -> object : Observable<Void>({ subscribe ->
                subscribe.add(
                    getArgs(request).subscribe {
                        val login = it["login"]!!
                        subscribe.add(
                            findUser(login).subscribe({
                                response.addCookie(DefaultCookie("login", login))
                                response.addCookie(DefaultCookie("wallets", it.wallets.name))
                                writeString(response, "OK!", subscribe)
                            }) {
                                writeString(response, "Login not found", subscribe)
                            }
                        )
                    }
                )
            }) {}
            "/addProduct" ->
                object : Observable<Void>({ subscribe ->
                    subscribe.add(
                        getArgs(request).subscribe {
                            val login = request.cookies["login"]
                            if (login == null || login.size == 0) {
                                writeString(response, "Please, login", subscribe)
                            } else {
                                val name = it["name"]!!
                                val rubles = it["rubles"]!!.toDouble()
                                Product(name, "", rubles).save().subscribe()
                                writeString(response, "OK!", subscribe)
                            }
                        }
                    )
                }) {}
            else -> throw IllegalStateException()
        }
    }
}