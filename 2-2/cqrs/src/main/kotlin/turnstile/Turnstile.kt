package main.kotlin.turnstile

import java.time.Instant

interface Turnstile {
    enum class Direction {
        IN, OUT
    }

    fun isValid(clientId: Long, event: Instant = Instant.now()): Boolean
    fun pass(clientId: Long, direction: Direction, event: Instant = Instant.now()): Boolean
}