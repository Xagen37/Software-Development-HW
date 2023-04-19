package market

import kotlinx.serialization.Serializable

@Serializable
data class Company(val name: String, val stocks: Stocks)