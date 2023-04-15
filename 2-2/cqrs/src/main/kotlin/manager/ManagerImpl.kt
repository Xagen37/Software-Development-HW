package main.kotlin.manager

import main.kotlin.Client
import main.kotlin.store.Store
import java.time.Instant

class ManagerImpl : Manager {
    private var historyStore: Store? = null
    fun useStore(store: Store) {
        historyStore = store
    }

    override fun getClient(clientId: Long): Client? {
        return historyStore?.getClient(clientId)
    }

    override fun newClient(event: Instant): Long? {
        return historyStore?.newClient(event)
    }

    override fun prolong(clientId: Long, time: Long, event: Instant) {
        historyStore!!.updateClient(clientId, time, event)
    }
}