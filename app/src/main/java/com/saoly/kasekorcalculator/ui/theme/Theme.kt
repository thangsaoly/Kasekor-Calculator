package com.saoly.kasekorcalculator.ui.theme

import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable

private val LightColorScheme = lightColorScheme(
    primary = GreenPrimary,
    onPrimary = CardBg,
    primaryContainer = GreenLight,
    secondary = AmberAlert,
    background = BgOffWhite,
    surface = CardBg,
    error = RedAlert
)

@Composable
fun KasekorCalculatorTheme(
    content: @Composable () -> Unit
) {
    MaterialTheme(
        colorScheme = LightColorScheme,
        typography = Typography,
        content = content
    )
}
