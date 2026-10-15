package com.saoly.kasekorcalculator.ui

import com.saoly.kasekorcalculator.domain.model.Currency
import com.saoly.kasekorcalculator.ui.common.formatInput
import com.saoly.kasekorcalculator.ui.common.formatMoney
import com.saoly.kasekorcalculator.ui.common.formatQuantity
import com.saoly.kasekorcalculator.ui.common.formatUnitPrice
import org.junit.Assert.assertEquals
import org.junit.Test

class FormattersTest {

    @Test
    fun `money is whole riel or dollars with cents`() {
        assertEquals("1,500,000 រៀល", formatMoney(1_500_000.0, Currency.KHR))
        assertEquals("$1,234.50", formatMoney(1234.5, Currency.USD))
        assertEquals("-$5.00", formatMoney(-5.0, Currency.USD))
    }

    @Test
    fun `dollar unit prices under one dollar keep a third decimal`() {
        assertEquals("$0.275", formatUnitPrice(0.275, Currency.USD))
        assertEquals("$312.50", formatUnitPrice(312.5, Currency.USD))
        assertEquals("1,200 រៀល", formatUnitPrice(1200.0, Currency.KHR))
    }

    @Test
    fun `quantities drop trailing zeros`() {
        assertEquals("2.5", formatQuantity(2.5))
        assertEquals("3,000", formatQuantity(3000.0))
        assertEquals("0.333", formatQuantity(1.0 / 3))
    }

    @Test
    fun `input prefill survives kg to ton round trip`() {
        // $0.3/kg shown per ton must not become 300.00000000000006
        assertEquals("300", formatInput(0.3 * 1000))
        assertEquals("1250.5", formatInput(1250.5))
    }
}
