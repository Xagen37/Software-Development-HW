package main.kotlin.report

import com.fasterxml.jackson.databind.DeserializationFeature
import com.fasterxml.jackson.databind.ObjectMapper
import com.fasterxml.jackson.module.kotlin.convertValue
import com.mongodb.client.MongoCollection
import com.mongodb.client.model.Filters.eq
import de.undercouch.bson4jackson.BsonFactory
import main.kotlin.SubEvent
import main.kotlin.turnstile.Turnstile
import org.bson.Document
import org.litote.kmongo.KMongo
import org.litote.kmongo.and
import java.time.Duration
import java.time.Instant
import java.time.LocalDate
import java.time.ZoneId

class ReportServiceImpl(db: String, collection: String) : ReportService {
    private val DEFAULT_JACKSON_MAPPER = ObjectMapper(BsonFactory())
        .findAndRegisterModules()
        .apply {
            configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false)
        }

    private val database = KMongo.createClient()
    private val events = database.getDatabase(db)
    private val eventsCollection = events.getCollection(collection)

    private fun parse(document: Document) : SubEvent {
        return when (SubEvent.SubType.valueOf(document.get("type", String::class.java))) {
            SubEvent.SubType.USE -> DEFAULT_JACKSON_MAPPER.convertValue<SubEvent.UseSubEvent>(document)
            SubEvent.SubType.CREATE -> DEFAULT_JACKSON_MAPPER.convertValue<SubEvent.CreateSubEvent>(document)
            SubEvent.SubType.PROLONG -> DEFAULT_JACKSON_MAPPER.convertValue<SubEvent.ProlongSubEvent>(document)
        }
    }

    override fun getStats(): Map<LocalDate, Long> {
        return eventsCollection.find(
            and(
                eq("type", SubEvent.SubType.USE.toString()),
                eq("direction", "IN")
            )
        )
        .toList().map { parse(it) }
        .filterIsInstance<SubEvent.UseSubEvent>()
        .groupBy { LocalDate.ofInstant(it.moment, ZoneId.of("Europe/Moscow")) }
        .mapValues { it.value.size.toLong() }
    }

    override fun countAverageUsesPerPerson(): Double {
        return eventsCollection.find()
            .toList().map { parse(it) }
            .filterIsInstance<SubEvent.UseSubEvent>()
            .groupBy { it.clientId }.mapValues { entry ->
                var sum = 0L
                for (value in entry.value) {
                    if (value.direction == Turnstile.Direction.IN) {
                        sum++
                    }
                }
                sum
            }
            .values.let {
                if (it.isNotEmpty()) {
                    it.sum().toDouble() / it.size
                } else {
                    0.0
                }
            }
    }

    override fun countAverageVisitDuration(): Double {
        return eventsCollection.find()
            .toList().map { parse(it) }
            .filterIsInstance<SubEvent.UseSubEvent>()
            .groupBy { it.clientId }.mapValues { entry ->
                var timeSum = 0L
                var enterMoment: Instant? = null
                for (value in entry.value) {
                    if (value.direction == Turnstile.Direction.IN) {
                        if (enterMoment != null) {
                            System.err.println("Double enter detected!")
                        }
                        enterMoment = value.moment
                    } else {
                        if (enterMoment != null) {
                            timeSum += Duration.between(enterMoment, value.moment).toSeconds()
                            enterMoment = null
                        } else {
                            System.err.println("Exit without enter detected!")
                        }
                    }
                }
                if (enterMoment != null) {
                    timeSum += Duration.between(enterMoment, Instant.now()).toSeconds()
                }
                timeSum
            }
            .values.let {
                if (it.isNotEmpty()) {
                    it.sum().toDouble() / it.size
                } else {
                    0.0
                }
            }
    }

    override fun insert(event: SubEvent) {
        eventsCollection.insertOne(DEFAULT_JACKSON_MAPPER.convertValue(event))
    }

    override fun getCollection(): MongoCollection<Document> {
        return eventsCollection
    }
}