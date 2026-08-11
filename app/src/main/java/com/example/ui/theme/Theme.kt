package com.example.ui.theme

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color

private val ShanksDarkColorScheme = darkColorScheme(
    primary = ShanksRedPrimary,
    onPrimary = Color.White,
    primaryContainer = ShanksRedDark,
    onPrimaryContainer = Color.White,
    secondary = ShanksGoldSecondary,
    onSecondary = Color.Black,
    secondaryContainer = ShanksAmber,
    onSecondaryContainer = Color.Black,
    background = ShanksDarkBg,
    onBackground = ShanksOnSurface,
    surface = ShanksDarkSurface,
    onSurface = ShanksOnSurface,
    surfaceVariant = ShanksDarkSurfaceVariant,
    onSurfaceVariant = ShanksOnSurfaceVariant,
    outline = ShanksCodeBorder
)

private val ShanksLightColorScheme = lightColorScheme(
    primary = ShanksRedDark,
    onPrimary = Color.White,
    primaryContainer = Color(0xFFFFDADA),
    onPrimaryContainer = ShanksRedDark,
    secondary = Color(0xFF7D5200),
    onSecondary = Color.White,
    background = Color(0xFFFAF8FB),
    onBackground = Color(0xFF1D1B20),
    surface = Color(0xFFFFFFFF),
    onSurface = Color(0xFF1D1B20)
)

@Composable
fun ShanksTheme(
    darkTheme: Boolean = true, // Default to dark theme for legendary Shanks vibe
    content: @Composable () -> Unit
) {
    val colorScheme = if (darkTheme) ShanksDarkColorScheme else ShanksLightColorScheme

    MaterialTheme(
        colorScheme = colorScheme,
        typography = Typography,
        content = content
    )
}

