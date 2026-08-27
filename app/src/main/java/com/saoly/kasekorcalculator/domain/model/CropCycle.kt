package com.saoly.kasekorcalculator.domain.model

data class CropCycle(
    val id: Int? = null,
    val name: String,
    val cropType: String,
    val seedsCost: Double,
    val fertilizerCost: Double,
    val laborCost: Double,
    val fuelCost: Double,
    val expectedYield: Double, // in kg
    val buyerPrice: Double = 0.0,
    val currency: String = "KHR",
    val createdAt: Long = System.currentTimeMillis()
) {
    val totalExpenses: Double
        get() = seedsCost + fertilizerCost + laborCost + fuelCost

    val breakEvenPrice: Double
        get() = if (expectedYield > 0) totalExpenses / expectedYield else 0.0

    fun calculateNetProfit(price: Double = buyerPrice): Double {
        return (price * expectedYield) - totalExpenses
    }
}
