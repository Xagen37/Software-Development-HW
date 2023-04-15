package main.kotlin.manager

import main.kotlin.Client
import java.time.Instant

interface Manager {
    fun getClient(clientId : Long) : Client?
    fun newClient(event : Instant = Instant.now()) : Long?
    fun prolong(clientId : Long, time:Long, event : Instant = Instant.now())
}