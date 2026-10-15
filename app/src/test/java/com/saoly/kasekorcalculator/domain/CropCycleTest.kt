package com.saoly.kasekorcalculator.domain

import com.saoly.kasekorcalculator.domain.model.CropCycle
import com.saoly.kasekorcalculator.domain.model.Currency
import com.saoly.kasekorcalculator.domain.model.YieldUnit
import org.junit.Assert.assertEquals
import org.junit.Test

class CropCycleTest {

    private fun cycle(
        totalCost: Double,
        yieldKg: Double,
        currency: Currency = Currency.KHR,
        unit: YieldUnit = YieldUnit.KG
    ) = CropCycle(
        name = "test",
        cropType = "rice",
        seedsCost = totalCost,
        fertilizerCost = 0.0,
        laborCost = 0.0,
        fuelCost = 0.0,
        expectedYield = yieldKg,
        currency = currency,
        yieldUnit = unit
    )

    @Test
    fun `yield unit converts between kg and tons`() {
        assertEquals(2500.0, YieldUnit.TON.toKg(2.5), 0.0)
        assertEquals(2.5, YieldUnit.TON.fromKg(2500.0), 0.0)
        assertEquals(1_200_000.0, YieldUnit.TON.pricePerUnit(1200.0), 0.0)
        assertEquals(1200.0, YieldUnit.TON.pricePerKg(1_200_000.0), 0.0)
        assertEquals(42.0, YieldUnit.KG.toKg(42.0), 0.0)
    }

    @Test
    fun `break-even per ton is break-even per kg times 1000`() {
        val c = cycle(totalCost = 3_000_000.0, yieldKg = 2500.0, unit = YieldUnit.TON)
        assertEquals(1200.0, c.breakEvenPrice, 1e-9)
        assertEquals(1_200_000.0, c.breakEvenPricePerUnit, 1e-6)
        assertEquals(2.5, c.expectedYieldInUnit, 0.0)
    }

    @Test
    fun `net profit uses price per display unit`() {
        val c = cycle(totalCost = 3_000_000.0, yieldKg = 2500.0, unit = YieldUnit.TON)
        assertEquals(500_000.0, c.netProfitForUnitPrice(1_400_000.0), 1e-6)
        assertEquals(-250_000.0, c.netProfitForUnitPrice(1_100_000.0), 1e-6)
    }

    @Test
    fun `profit within half the smallest displayed amount counts as break-even`() {
        // $1000 over 3 kg: break-even is $333.333.../kg
        val usd = cycle(totalCost = 1000.0, yieldKg = 3.0, currency = Currency.USD)
        assertEquals(0.0, usd.netProfitForUnitPrice(333.3334), 0.0) // +$0.0002
        assertEquals(-0.031, usd.netProfitForUnitPrice(333.323), 1e-9)

        // KHR: 1 riel short is a real loss
        val khr = cycle(totalCost = 1_000_000.0, yieldKg = 3.0)
        assertEquals(-1.0, khr.netProfitForUnitPrice(333_333.0), 1e-6)
    }

    @Test
    fun `unknown stored codes fall back to defaults`() {
        assertEquals(Currency.KHR, Currency.fromCode("EUR"))
        assertEquals(YieldUnit.KG, YieldUnit.fromCode(""))
        assertEquals(YieldUnit.TON, YieldUnit.fromCode("TON"))
    }
}
