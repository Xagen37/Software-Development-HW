package model

import org.bson.Document

data class Product(val name: String, val desc: String, val priceRub: Double) {
    constructor(document:Document) : this(
        document.getString("name"),
        document.getString("desc"),
        document.getDouble("priceRub")
    )

    fun toTable() : Document {
        return Document("name", name).append("desc", desc).append("priceRub", priceRub)
    }
}