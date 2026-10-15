package com.saoly.kasekorcalculator.domain

import com.saoly.kasekorcalculator.domain.model.CropCycle
import com.saoly.kasekorcalculator.domain.model.CropCycleForm
import com.saoly.kasekorcalculator.domain.model.Currency
import com.saoly.kasekorcalculator.domain.model.YieldUnit
import org.junit.Assert.assertEquals
import org.junit.Assert.assertNull
import org.junit.Test

class CropCycleFormTest {

    private val form = CropCycleForm(
        name = "  Rice rainy 2026 ",
        cropType = "ស្រូវ 🌾",
        seedsCost = 100.0,
        fertilizerCost = 200.0,
        laborCost = 300.0,
        fuelCost = 400.0,
        expectedYield = 2.5,
        currency = Currency.KHR,
        yieldUnit = YieldUnit.TON
    )

    private val saved = CropCycle(
        id = 7,
        name = "old",
        cropType = "ស្រូវ/Rice",
        seedsCost = 1.0,
        fertilizerCost = 1.0,
        laborCost = 1.0,
        fuelCost = 1.0,
        expectedYield = 100.0,
        buyerPrice = 1200.0,
        currency = Currency.KHR,
        yieldUnit = YieldUnit.KG,
        createdAt = 1_000L
    )

    @Test
    fun `new cycle stores yield in kg and trims the name`() {
        val cycle = form.toCropCycle()
        assertNull(cycle.id)
        assertEquals("Rice rainy 2026", cycle.name)
        assertEquals(2500.0, cycle.expectedYield, 0.0)
        assertEquals(YieldUnit.TON, cycle.yieldUnit)
        assertEquals(1000.0, cycle.totalExpenses, 0.0)
        assertEquals(0.0, cycle.buyerPrice, 0.0)
    }

    @Test
    fun `edit keeps id, creation date and buyer price`() {
        val cycle = form.toCropCycle(saved)
        assertEquals(7, cycle.id)
        assertEquals(1_000L, cycle.createdAt)
        assertEquals(1200.0, cycle.buyerPrice, 0.0) // per kg, unaffected by the kg -> ton change
        assertEquals("ស្រូវ 🌾", cycle.cropType)
        assertEquals(2500.0, cycle.expectedYield, 0.0)
        assertEquals(1000.0, cycle.totalExpenses, 0.0)
    }

    @Test
    fun `changing the currency clears the saved buyer price`() {
        val cycle = form.copy(currency = Currency.USD).toCropCycle(saved)
        assertEquals(Currency.USD, cycle.currency)
        assertEquals(0.0, cycle.buyerPrice, 0.0)
        assertEquals(7, cycle.id)
    }
}
