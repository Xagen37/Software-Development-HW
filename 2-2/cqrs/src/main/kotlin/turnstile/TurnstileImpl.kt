package main.kotlin.turnstile

import main.kotlin.store.Store
import java.time.Instant

class TurnstileImpl : Turnstile {
    private var historyStore: Store? = null
    fun useStore(store: Store) {
        historyStore = store
    }

    override fun isValid(clientId: Long, event: Instant): Boolean {
        val sub = historyStore?.getClient(clientId) ?: return false
        val validUntil = sub.validUntil?: return false
        return validUntil >= event
    }

    override fun pass(clientId: Long, direction: Turnstile.Direction, event: Instant): Boolean {
        val sub = historyStore?.getClient(clientId) ?: return false

        if (direction == Turnstile.Direction.OUT) {
            historyStore?.pass(clientId, false, event)
            return true
        }

        val validUntil = sub.validUntil?: return false
        return if (validUntil < event) {
            false
        } else {
            historyStore?.pass(clientId, true, event)
            true
        }
    }
}