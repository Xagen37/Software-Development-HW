import io.netty.buffer.ByteBuf
import io.reactivex.netty.protocol.http.server.HttpServerRequest
import io.reactivex.netty.protocol.http.server.HttpServerResponse
import io.netty.handler.codec.http.HttpMethod
import io.reactivex.netty.protocol.http.server.HttpServer
import java.lang.IllegalStateException
import response.GetResponseHandler
import response.PostResponseHandler

fun main() {
    HttpServer.newServer(8080)
        .start { request: HttpServerRequest<ByteBuf>, response: HttpServerResponse<ByteBuf> ->
            when {
                HttpMethod.GET == request.httpMethod -> {
                    GetResponseHandler.request(request, response)
                }
                HttpMethod.POST == request.httpMethod -> {
                    PostResponseHandler.request(request, response)
                }
                else -> {
                    throw IllegalStateException()
                }
            }
        }
    while(true) {

    }
}