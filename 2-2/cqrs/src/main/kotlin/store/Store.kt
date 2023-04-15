package main.kotlin.store

import main.kotlin.Client
import java.time.Instant

interface Store {
    fun newClient(event : Instant = Instant.now()) : Long
    fun updateClient(clientId: Long, time: Long, event: Instant = Instant.now())
    fun getClient(clientId: Long): Client?
    fun pass(clientId: Long, isEnter: Boolean, event: Instant = Instant.now())
}