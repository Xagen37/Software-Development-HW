package main.kotlin

import main.kotlin.turnstile.Turnstile
import java.time.Instant

open class SubEvent(val clientId: Long, val type: SubType) {
    enum class SubType {
        CREATE, PROLONG, USE
    }

    class CreateSubEvent(clientId: Long, val moment: Instant) : SubEvent(clientId, SubType.CREATE)
    class ProlongSubEvent(clientId: Long, val moment: Instant, val duration: Long) : SubEvent(clientId, SubType.PROLONG)
    class UseSubEvent(clientId: Long, val moment: Instant, val direction: Turnstile.Direction) : SubEvent(clientId, SubType.USE)
}