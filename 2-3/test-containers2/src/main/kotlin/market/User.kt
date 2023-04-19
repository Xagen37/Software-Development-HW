package market

import kotlinx.serialization.Serializable

@Serializable
data class User(val name: String, var money: Long)