package test.kotlin

import com.mongodb.client.model.Filters
import main.kotlin.SubEvent
import main.kotlin.manager.Manager
import main.kotlin.manager.ManagerImpl
import main.kotlin.report.ReportService
import main.kotlin.report.ReportServiceImpl
import main.kotlin.store.EventStoreImpl
import main.kotlin.store.Store
import main.kotlin.turnstile.Turnstile
import main.kotlin.turnstile.TurnstileImpl
import org.junit.jupiter.api.Assertions
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.litote.kmongo.KMongo
import org.litote.kmongo.getCollection
import java.time.Duration
import java.time.Instant
import java.time.temporal.ChronoUnit

class Test {
    data class CurrentState(
        val reportService: ReportService,
        val historyStore: Store,
        val turnstiles: List<Turnstile>,
        val admins: List<Manager>
    )

    fun getState(turn_n: Int, admin_n: Int): CurrentState {
        val reportService = ReportServiceImpl("CQRS-test", "subscribes-test")
        val historyStore = EventStoreImpl(reportService)
        val turnList = (1..turn_n).map {
            val turnstile = TurnstileImpl()
            turnstile.useStore(historyStore)
            turnstile
        }
        val adminList = (1..admin_n).map {
            val admin = ManagerImpl()
            admin.useStore(historyStore)
            admin
        }
        return CurrentState(reportService, historyStore, turnList, adminList)
    }

    @BeforeEach
    fun cleanup() {
        val database = KMongo.createClient()
        val events = database.getDatabase("CQRS-test")
        val collection = events.getCollection<SubEvent>("subscribes-test")
        collection.deleteMany(Filters.empty())
    }

    @Test
    fun empty() {
        getState(1, 1)
    }

    @Test
    fun createClient() {
        val state = getState(1, 1)
        Assertions.assertEquals(
            0,
            state.admins.first().newClient()
        )
    }

    @Test
    fun createAndEnter() {
        val begin = Instant.now()
        val state = getState(1, 1)
        val clientId = state.admins.first().newClient(begin)
        Assertions.assertNotNull(clientId)
        Assertions.assertEquals(0, clientId)
        Assertions.assertFalse(state.turnstiles.first().isValid(clientId!!, begin.plus(Duration.of(1, ChronoUnit.SECONDS))))
        state.admins.first().prolong(
            clientId, 10, begin.plus(Duration.of(2, ChronoUnit.SECONDS))
        )
        Assertions.assertTrue(state.turnstiles.first().isValid(clientId, begin.plus(Duration.of(3, ChronoUnit.SECONDS))))
        Assertions.assertFalse(state.turnstiles.first().isValid(clientId, begin.plus(Duration.of(100, ChronoUnit.SECONDS))))
    }

    @Test
    fun twentyMinutes() {
        val begin = Instant.now()
        val end = begin.plus(Duration.of(20, ChronoUnit.MINUTES))
        val state = getState(1, 1)

        val rick = state.admins.first().newClient(begin)
        val morty = state.admins.first().newClient(begin)
        Assertions.assertNotNull(rick)
        Assertions.assertNotNull(morty)
        Assertions.assertEquals(0, rick)
        Assertions.assertEquals(1, morty)

        state.admins.first().prolong(rick!!, 20 * 60, begin)
        state.admins.first().prolong(morty!!, 20 * 60, begin)
        Assertions.assertTrue(state.turnstiles.first().pass(rick, Turnstile.Direction.IN, begin))
        Assertions.assertTrue(state.turnstiles.first().pass(morty, Turnstile.Direction.IN, begin))

        Assertions.assertTrue(state.turnstiles.first().pass(rick, Turnstile.Direction.OUT, end))
        Assertions.assertTrue(state.turnstiles.first().pass(morty, Turnstile.Direction.OUT, end))

        Assertions.assertEquals(1.0, state.reportService.countAverageUsesPerPerson())
        Assertions.assertEquals(1200.0, state.reportService.countAverageVisitDuration())
    }
}