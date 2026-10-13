package com.paradoxlog.app.core.designsystem

import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.ReadOnlyComposable
import androidx.compose.runtime.staticCompositionLocalOf

/**
 * Paradox Log - Dark Color Scheme
 * Strictly monochromatic baseline with restrained accents.
 */
private val ParadoxDarkColorScheme = darkColorScheme(
    background = ParadoxColorBackground,
    surface = ParadoxColorSurface,
    surfaceVariant = ParadoxColorSurfaceVariant,
    onBackground = ParadoxColorTextPrimary,
    onSurface = ParadoxColorTextPrimary,
    onSurfaceVariant = ParadoxColorTextSecondary,
    outline = ParadoxColorBorder,
    outlineVariant = ParadoxColorBorderFocused,
    primary = ParadoxColorStabilityCyan,
    onPrimary = ParadoxColorBackground,
    secondary = ParadoxColorSelfTrustAccent,
    onSecondary = ParadoxColorBackground,
    tertiary = ParadoxColorTyrantAccent,
    onTertiary = ParadoxColorBackground
)

val LocalParadoxColors = staticCompositionLocalOf { ParadoxCustomColors() }

/**
 * Convenience accessor for custom Paradox Log design tokens.
 */
object ParadoxTheme {
    val colors: ParadoxCustomColors
        @Composable
        @ReadOnlyComposable
        get() = LocalParadoxColors.current

    val spacing: ParadoxSpacing
        @Composable
        @ReadOnlyComposable
        get() = LocalParadoxSpacing.current

    val typography: androidx.compose.material3.Typography
        @Composable
        @ReadOnlyComposable
        get() = MaterialTheme.typography
}

/**
 * Root theme composable for Paradox Log.
 * Enforces an OLED-friendly dark foundation with typography-first priority.
 */
@Composable
fun ParadoxTheme(
    content: @Composable () -> Unit
) {
    val customColors = ParadoxCustomColors()
    val spacing = ParadoxSpacing()

    CompositionLocalProvider(
        LocalParadoxColors provides customColors,
        LocalParadoxSpacing provides spacing
    ) {
        MaterialTheme(
            colorScheme = ParadoxDarkColorScheme,
            typography = ParadoxTypography,
            content = content
        )
    }
}
