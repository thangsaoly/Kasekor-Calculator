package com.saoly.kasekorcalculator.domain.model

enum class Currency(
    // Half of the smallest amount shown on screen; profits smaller than this display as break-even
    val breakEvenTolerance: Double
) {
    KHR(breakEvenTolerance = 0.5),
    USD(breakEvenTolerance = 0.005);

    companion object {
        fun fromCode(code: String): Currency = entries.firstOrNull { it.name == code } ?: KHR
    }
}
