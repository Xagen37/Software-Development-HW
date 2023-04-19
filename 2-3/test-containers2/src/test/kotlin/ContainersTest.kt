import io.ktor.client.*
import io.ktor.client.request.*
import io.ktor.client.statement.*
import io.ktor.http.*
import io.ktor.server.testing.*
import main.kotlin.user.configureRouting
import org.junit.jupiter.api.Assertions
import org.junit.jupiter.api.Test
import org.testcontainers.containers.FixedHostPortGenericContainer
import org.testcontainers.junit.jupiter.Container
import org.testcontainers.junit.jupiter.Testcontainers

@Testcontainers
class ContainersTest {

    @Container
    val marketContainer = FixedHostPortGenericContainer(
        "market"
    ).withFixedExposedPort(8080, 8080).withExposedPorts(8080)

    fun testWithConfiguration(body: suspend ApplicationTestBuilder.() -> Unit)
    = testApplication {
        application {
            configureRouting(HttpClient())
        }
        body()
    }

    @Test
    fun getUser() = testWithConfiguration {
        client.get("/getUser/0").apply {
            Assertions.assertEquals(HttpStatusCode.OK, status)
            Assertions.assertEquals("""{"name":"User1","money":1000}""", bodyAsText())
        }

        client.get("/getUser/1").apply {
            Assertions.assertEquals(HttpStatusCode.OK, status)
            Assertions.assertEquals("""{"name":"User2","money":300}""", bodyAsText())
        }
    }

    @Test
    fun getCompany() = testWithConfiguration {
        client.get("/getCompany/0").apply {
            Assertions.assertEquals(HttpStatusCode.OK, status)
            Assertions.assertEquals("""{"name":"Company1","stocks":{"number":100,"price":1}}""", bodyAsText())
        }

        client.get("/getCompany/1").apply {
            Assertions.assertEquals(HttpStatusCode.OK, status)
            Assertions.assertEquals("""{"name":"Company2","stocks":{"number":10,"price":100500}}""", bodyAsText())
        }
    }

    @Test
    fun assMoney() = testWithConfiguration {
        client.post("/addMoney/0/337").apply {
            Assertions.assertEquals(HttpStatusCode.OK, status)
        }
        client.get("/getUser/0").apply {
            Assertions.assertEquals(HttpStatusCode.OK, status)
            Assertions.assertEquals("""{"name":"User1","money":1337}""", bodyAsText())
        }
    }

    @Test
    fun addUser() = testWithConfiguration {
        client.post("/addUser/User3?money=234").apply {
            Assertions.assertEquals(HttpStatusCode.OK, status)
            Assertions.assertEquals("2", bodyAsText())
        }
        client.get("/getUser/2").apply {
            Assertions.assertEquals(HttpStatusCode.OK, status)
            Assertions.assertEquals("""{"name":"User3","money":234}""", bodyAsText())
        }
    }

    @Test
    fun purchase() = testWithConfiguration {
        client.post("purchase/0/0/1/1").apply {
            Assertions.assertEquals(HttpStatusCode.OK, status)
            Assertions.assertEquals("true", bodyAsText())
        }
        client.get("/getUser/0").apply {
            Assertions.assertEquals(HttpStatusCode.OK, status)
            Assertions.assertEquals("""{"name":"User1","money":999}""", bodyAsText())
        }
        client.get("/getCompany/0").apply {
            Assertions.assertEquals(HttpStatusCode.OK, status)
            Assertions.assertEquals("""{"name":"Company1","stocks":{"number":99,"price":1}}""", bodyAsText())
        }

        client.post("purchase/0/0/1/2").apply {
            Assertions.assertEquals(HttpStatusCode.OK, status)
            Assertions.assertEquals("false", bodyAsText())
        }
        client.get("/getUser/0").apply {
            Assertions.assertEquals(HttpStatusCode.OK, status)
            Assertions.assertEquals("""{"name":"User1","money":999}""", bodyAsText())
        }
        client.get("/getCompany/0").apply {
            Assertions.assertEquals(HttpStatusCode.OK, status)
            Assertions.assertEquals("""{"name":"Company1","stocks":{"number":99,"price":1}}""", bodyAsText())
        }

        client.post("purchase/0/1/1/2").apply {
            Assertions.assertEquals(HttpStatusCode.OK, status)
            Assertions.assertEquals("false", bodyAsText())
        }

        client.get("/getCompaniesIdByUser/0").apply {
            Assertions.assertEquals(HttpStatusCode.OK, status)
            Assertions.assertEquals("[0]", bodyAsText())
        }
        client.get("/getHoldersIdByCompany/0").apply {
            Assertions.assertEquals(HttpStatusCode.OK, status)
            Assertions.assertEquals("[0]", bodyAsText())
        }
        client.get("/getUserStocks/0/0").apply {
            Assertions.assertEquals(HttpStatusCode.OK, status)
            Assertions.assertEquals("1", bodyAsText())
        }
    }

    @Test
    fun sell() = testWithConfiguration {
        client.post("purchase/0/0/1/1").apply {
            Assertions.assertEquals(HttpStatusCode.OK, status)
            Assertions.assertEquals("true", bodyAsText())
        }
        client.post("sell/0/0/1/1").apply {
            Assertions.assertEquals(HttpStatusCode.OK, status)
            Assertions.assertEquals("true", bodyAsText())
        }
        client.get("/getUser/0").apply {
            Assertions.assertEquals(HttpStatusCode.OK, status)
            Assertions.assertEquals("""{"name":"User1","money":1000}""", bodyAsText())
        }
        client.get("/getCompany/0").apply {
            Assertions.assertEquals(HttpStatusCode.OK, status)
            Assertions.assertEquals("""{"name":"Company1","stocks":{"number":100,"price":1}}""", bodyAsText())
        }

        client.post("sell/0/0/1/2").apply {
            Assertions.assertEquals(HttpStatusCode.OK, status)
            Assertions.assertEquals("false", bodyAsText())
        }
        client.get("/getUser/0").apply {
            Assertions.assertEquals(HttpStatusCode.OK, status)
            Assertions.assertEquals("""{"name":"User1","money":1000}""", bodyAsText())
        }
        client.get("/getCompany/0").apply {
            Assertions.assertEquals(HttpStatusCode.OK, status)
            Assertions.assertEquals("""{"name":"Company1","stocks":{"number":100,"price":1}}""", bodyAsText())
        }

        client.post("sell/0/1/1/2").apply {
            Assertions.assertEquals(HttpStatusCode.OK, status)
            Assertions.assertEquals("false", bodyAsText())
        }

        client.get("/getCompaniesIdByUser/0").apply {
            Assertions.assertEquals(HttpStatusCode.OK, status)
            Assertions.assertEquals("[]", bodyAsText())
        }
        client.get("/getHoldersIdByCompany/0").apply {
            Assertions.assertEquals(HttpStatusCode.OK, status)
            Assertions.assertEquals("[]", bodyAsText())
        }
        client.get("/getUserStocks/0/0").apply {
            Assertions.assertEquals(HttpStatusCode.OK, status)
            Assertions.assertEquals("0", bodyAsText())
        }
    }
}