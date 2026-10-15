package com.saoly.kasekorcalculator.domain.model

enum class YieldUnit(val kgPerUnit: Double) {
    KG(kgPerUnit = 1.0),
    TON(kgPerUnit = 1000.0);

    fun toKg(amount: Double): Double = amount * kgPerUnit

    fun fromKg(kg: Double): Double = kg / kgPerUnit

    // A price per kg expressed per this unit (e.g. 1,200 ៛/kg -> 1,200,000 ៛/ton)
    fun pricePerUnit(pricePerKg: Double): Double = pricePerKg * kgPerUnit

    fun pricePerKg(pricePerUnit: Double): Double = pricePerUnit / kgPerUnit

    companion object {
        fun fromCode(code: String): YieldUnit = entries.firstOrNull { it.name == code } ?: KG
    }
}
