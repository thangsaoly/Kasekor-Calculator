package com.saoly.kasekorcalculator.domain.model

data class CropCycle(
    val id: Int? = null,
    val name: String,
    val cropType: String,
    val seedsCost: Double,
    val fertilizerCost: Double,
    val laborCost: Double,
    val fuelCost: Double,
    val expectedYield: Double, // always stored in kg; yieldUnit only controls display
    val buyerPrice: Double = 0.0, // per kg, in currency
    val currency: Currency = Currency.KHR,
    val yieldUnit: YieldUnit = YieldUnit.KG,
    val createdAt: Long = System.currentTimeMillis()
) {
    val totalExpenses: Double
        get() = seedsCost + fertilizerCost + laborCost + fuelCost

    // Per kg
    val breakEvenPrice: Double
        get() = if (expectedYield > 0) totalExpenses / expectedYield else 0.0

    val breakEvenPricePerUnit: Double
        get() = yieldUnit.pricePerUnit(breakEvenPrice)

    val expectedYieldInUnit: Double
        get() = yieldUnit.fromKg(expectedYield)

    fun calculateNetProfit(price: Double = buyerPrice): Double {
        return (price * expectedYield) - totalExpenses
    }

    // Net profit for a price per yieldUnit, snapped to 0 when it rounds to nothing on screen
    fun netProfitForUnitPrice(pricePerUnit: Double): Double {
        val profit = calculateNetProfit(yieldUnit.pricePerKg(pricePerUnit))
        return if (kotlin.math.abs(profit) < currency.breakEvenTolerance) 0.0 else profit
    }
}
