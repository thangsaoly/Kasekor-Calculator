package com.saoly.kasekorcalculator.ui.common

import androidx.annotation.StringRes
import com.saoly.kasekorcalculator.R
import com.saoly.kasekorcalculator.domain.model.Currency
import com.saoly.kasekorcalculator.domain.model.YieldUnit
import java.math.BigDecimal
import java.math.RoundingMode
import java.util.Locale
import kotlin.math.abs

// Totals and costs: whole riel, or dollars with cents
fun formatMoney(amount: Double, currency: Currency): String = when (currency) {
    Currency.KHR -> String.format(Locale.US, "%,.0f រៀល", amount)
    Currency.USD -> formatDollars(amount, decimals = 2)
}

// Prices per kg/ton: dollar prices under $1 get a third decimal so $0.275 doesn't show as $0.28
fun formatUnitPrice(amount: Double, currency: Currency): String = when (currency) {
    Currency.KHR -> formatMoney(amount, currency)
    Currency.USD -> formatDollars(amount, decimals = if (abs(amount) < 1.0) 3 else 2)
}

// Yields: up to 3 decimals (e.g. 2.5 tons), no trailing zeros
fun formatQuantity(amount: Double): String {
    val rounded = BigDecimal(amount).setScale(3, RoundingMode.HALF_UP).stripTrailingZeros()
    val decimals = rounded.scale().coerceAtLeast(0)
    return String.format(Locale.US, "%,.${decimals}f", rounded)
}

// Plain number for prefilling an input field, e.g. 1250.5 or 300 (no grouping, no trailing zeros)
fun formatInput(amount: Double): String =
    BigDecimal(amount).setScale(6, RoundingMode.HALF_UP).stripTrailingZeros().toPlainString()

private fun formatDollars(amount: Double, decimals: Int): String {
    val sign = if (amount < 0) "-" else ""
    return sign + String.format(Locale.US, "$%,.${decimals}f", abs(amount))
}

@get:StringRes
val Currency.labelRes: Int
    get() = when (this) {
        Currency.KHR -> R.string.currency_khr
        Currency.USD -> R.string.currency_usd
    }

@get:StringRes
val YieldUnit.labelRes: Int
    get() = when (this) {
        YieldUnit.KG -> R.string.unit_kg
        YieldUnit.TON -> R.string.unit_ton
    }
