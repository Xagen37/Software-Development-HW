package main.kotlin.report

import com.mongodb.client.MongoCollection
import main.kotlin.SubEvent
import org.bson.Document
import java.time.LocalDate

interface ReportService {
    fun getStats() : Map<LocalDate, Long>
    fun countAverageUsesPerPerson() : Double
    fun countAverageVisitDuration() : Double
    fun insert(event: SubEvent)
    fun getCollection(): MongoCollection<Document>
}