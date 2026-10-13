package com.paradoxlog.app.core.designsystem

import androidx.compose.runtime.Immutable
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp

/**
 * Paradox Log - Spacing & Layout Metrics
 *
 * Implements an 8dp spatial grid with generous negative space
 * as defined in docs/design/design-system.md.
 */
@Immutable
data class ParadoxSpacing(
    val none: Dp = 0.dp,
    val extraSmall: Dp = 4.dp,
    val small: Dp = 8.dp,
    val medium: Dp = 16.dp,
    val large: Dp = 24.dp,        // Standard screen edge padding
    val extraLarge: Dp = 32.dp,   // Section vertical breathing room
    val huge: Dp = 48.dp          // Major narrative act separation
)

val LocalParadoxSpacing = androidx.compose.runtime.staticCompositionLocalOf { ParadoxSpacing() }
