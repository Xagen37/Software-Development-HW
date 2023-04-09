package model


enum class Currency {
    RUB, USD, EUR;

    fun fromRubles(num: Double): Double {
        return when(this) {
            USD -> num / 81.1
            EUR -> num / 89.17
            RUB -> num
        }
    }
}