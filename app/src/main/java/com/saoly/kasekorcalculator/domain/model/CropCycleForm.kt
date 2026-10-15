package com.saoly.kasekorcalculator.domain.model

/**
 * Validated values from the calculator form. Costs are in [currency]; [expectedYield] is in [yieldUnit].
 */
data class CropCycleForm(
    val name: String,
    val cropType: String,
    val seedsCost: Double,
    val fertilizerCost: Double,
    val laborCost: Double,
    val fuelCost: Double,
    val expectedYield: Double,
    val currency: Currency,
    val yieldUnit: YieldUnit
) {
    /**
     * A new crop cycle, or [original] with these values applied. Editing keeps the id and creation
     * date; the saved buyer price is cleared when the currency changes, since it no longer applies.
     */
    fun toCropCycle(original: CropCycle? = null): CropCycle {
        val expectedYieldKg = yieldUnit.toKg(expectedYield)
        return original?.copy(
            name = name.trim(),
            cropType = cropType,
            seedsCost = seedsCost,
            fertilizerCost = fertilizerCost,
            laborCost = laborCost,
            fuelCost = fuelCost,
            expectedYield = expectedYieldKg,
            buyerPrice = if (currency == original.currency) original.buyerPrice else 0.0,
            currency = currency,
            yieldUnit = yieldUnit
        ) ?: CropCycle(
            name = name.trim(),
            cropType = cropType,
            seedsCost = seedsCost,
            fertilizerCost = fertilizerCost,
            laborCost = laborCost,
            fuelCost = fuelCost,
            expectedYield = expectedYieldKg,
            currency = currency,
            yieldUnit = yieldUnit
        )
    }
}
