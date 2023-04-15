package main.kotlin.store

import com.fasterxml.jackson.databind.DeserializationFeature
import com.fasterxml.jackson.databind.ObjectMapper
import com.fasterxml.jackson.module.kotlin.convertValue
import de.undercouch.bson4jackson.BsonFactory
import main.kotlin.Client
import main.kotlin.SubEvent
import main.kotlin.report.ReportService
import main.kotlin.turnstile.Turnstile
import org.bson.Document
import java.time.Duration
import java.time.Instant
import java.time.ZoneId
import java.time.temporal.ChronoUnit

class EventStoreImpl(val reportService: ReportService) : Store {
    val DEFAULT_JACKSON_MAPPER = ObjectMapper(BsonFactory())
        .findAndRegisterModules()
        .apply {
            configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false)
        }
    fun parse(document: Document) : SubEvent {
        return when (SubEvent.SubType.valueOf(document.get("type", String::class.java))) {
            SubEvent.SubType.USE -> DEFAULT_JACKSON_MAPPER.convertValue<SubEvent.UseSubEvent>(document)
            SubEvent.SubType.CREATE -> DEFAULT_JACKSON_MAPPER.convertValue<SubEvent.CreateSubEvent>(document)
            SubEvent.SubType.PROLONG -> DEFAULT_JACKSON_MAPPER.convertValue<SubEvent.ProlongSubEvent>(document)
        }
    }

    val eventsCollection = reportService.getCollection()
    val clientsCollection: MutableMap<Long, Client> = run {
        eventsCollection.find()
            .toList().map { parse(it) }
            .groupBy { it.clientId }.mapValues {
                val lastEntry = it.value.mapNotNull { if (it is SubEvent.ProlongSubEvent) it else null }
                                        .maxByOrNull { it.moment.atZone(ZoneId.systemDefault()).toEpochSecond() }

                if (lastEntry == null) {
                    Client(it.key, null)
                } else {
                    Client(it.key, lastEntry.moment.plus(Duration.of(lastEntry.duration, ChronoUnit.MILLIS)))
                }
            }
            .toMutableMap()
    }

    override fun newClient(event: Instant): Long {
        val newId = clientsCollection.size.toLong()
        val subscriptionEvent = SubEvent.CreateSubEvent(newId, event)
        reportService.insert(subscriptionEvent)
        clientsCollection[newId] = Client(newId, null)
        return newId
    }

    override fun updateClient(clientId: Long, time: Long, event: Instant) {
        val subscriptionEvent = SubEvent.ProlongSubEvent(clientId, event, time)
        reportService.insert(subscriptionEvent)
        clientsCollection[clientId] = Client(clientId, event.plus(Duration.of(subscriptionEvent.duration, ChronoUnit.SECONDS)))

    }

    override fun getClient(clientId: Long): Client? {
        return clientsCollection[clientId]
    }

    override fun pass(clientId: Long, isEnter: Boolean, event: Instant) {
        val direction = if (isEnter) Turnstile.Direction.IN else Turnstile.Direction.OUT
        val useEvent = SubEvent.UseSubEvent(clientId, event, direction)
        reportService.insert(useEvent)
    }
}