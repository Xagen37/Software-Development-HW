package main.kotlin

import main.kotlin.manager.ManagerImpl
import main.kotlin.report.ReportServiceImpl
import main.kotlin.store.EventStoreImpl
import main.kotlin.turnstile.Turnstile
import main.kotlin.turnstile.TurnstileImpl

fun main() {
    val ADMIN = "admin"
    val CREATE = "create"
    val IN = "enter"
    val OUT = "exit"
    val PROLONG = "prolong"
    val REPORT = "report"
    val TURNSTILE = "turnstile"

    val MONTH = 24L * 24 * 3600 * 1000

    val reportService = ReportServiceImpl("CQRS", "subscribes")
    val historyStore = EventStoreImpl(reportService)
    val turnstile = TurnstileImpl()
    turnstile.useStore(historyStore)
    val manager = ManagerImpl()
    manager.useStore(historyStore)


    var input = readLine()
    while(input != null) {
        val splitted = input.split(" ")
        when(splitted[0]) {
            TURNSTILE -> {
                val clientId = splitted[2].toLong()
                when(splitted[1]) {
                    IN -> {
                        val res = turnstile.pass(clientId, Turnstile.Direction.IN)
                        if (res) {
                            println("ENTERED!")
                        } else {
                            println("Not valid!")
                        }
                    }
                    OUT -> {
                        val res = turnstile.pass(clientId, Turnstile.Direction.OUT)
                        if (res) {
                            println("EXITED")
                        } else {
                            println("Something is not right...")
                        }
                    }
                }
            }
            ADMIN -> {
                when(splitted[1]) {
                    CREATE -> {
                        println(manager.newClient())
                    }
                    PROLONG -> {
                        manager.prolong(splitted[2].toLong(), MONTH)
                        println("PROLONGED")
                    }
                }
            }
            REPORT -> {
                println("Report: average visits per person: ${reportService.countAverageUsesPerPerson()}; average time in the GYM: ${reportService.countAverageVisitDuration()} seconds")
                println(reportService.getStats())
            }
        }
        input = readLine()
    }
}