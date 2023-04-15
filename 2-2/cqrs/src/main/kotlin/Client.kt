package main.kotlin

import java.time.Instant

data class Client(val clientId: Long, val validUntil: Instant? = null)