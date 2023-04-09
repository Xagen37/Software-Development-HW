package model

import org.bson.Document

data class User(val login: String, val wallets: Currency) {
    constructor(document: Document) : this (
        document.getString("login"),
        Currency.valueOf(document.getString("wallets"))
    )

    fun toTable(): Document {
        return Document("login", login).append("wallets", wallets)
    }
}