package market

import kotlinx.serialization.Serializable

@Serializable
data class Stocks(var number: Long, var price: Long)