package com.paradoxlog.app.core.designsystem

import androidx.compose.material3.Typography
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.sp

/**
 * Paradox Log - Typography System
 *
 * Implements the typography hierarchy specified in docs/design/design-system.md:
 * - Display / Act Title: Wide-tracked, light/medium headline
 * - Headline / Scene: Restrained, uppercase tracking
 * - Body Large / Prose: Primary narrative exposition with generous 1.6 line height
 * - Body Medium / Dialogue: Spoken lines
 * - Label Large / Choice: High-contrast, deliberate decision prompts
 * - Label Small / Metadata: Monospace status readouts and telemetry
 */
val ParadoxTypography = Typography(
    // Act Introductions and Major Milestones
    displayLarge = TextStyle(
        fontFamily = FontFamily.SansSerif,
        fontWeight = FontWeight.Light,
        fontSize = 32.sp,
        lineHeight = 40.sp,
        letterSpacing = 2.sp,
        color = ParadoxColorTextPrimary
    ),

    // Scene titles & locations
    headlineMedium = TextStyle(
        fontFamily = FontFamily.SansSerif,
        fontWeight = FontWeight.SemiBold,
        fontSize = 18.sp,
        lineHeight = 26.sp,
        letterSpacing = 1.2.sp,
        color = ParadoxColorTextPrimary
    ),

    // Subheaders and Section Markers
    titleMedium = TextStyle(
        fontFamily = FontFamily.SansSerif,
        fontWeight = FontWeight.Medium,
        fontSize = 16.sp,
        lineHeight = 24.sp,
        letterSpacing = 0.5.sp,
        color = ParadoxColorTextSecondary
    ),

    // Primary Narrative Prose (Generous 1.6 line-height for immersion)
    bodyLarge = TextStyle(
        fontFamily = FontFamily.SansSerif,
        fontWeight = FontWeight.Normal,
        fontSize = 17.sp,
        lineHeight = 28.sp, // ~1.65 line-height ratio
        letterSpacing = 0.2.sp,
        color = ParadoxColorTextPrimary
    ),

    // Spoken Dialogue Lines
    bodyMedium = TextStyle(
        fontFamily = FontFamily.SansSerif,
        fontWeight = FontWeight.Normal,
        fontSize = 15.sp,
        lineHeight = 24.sp,
        letterSpacing = 0.15.sp,
        color = ParadoxColorTextPrimary
    ),

    // Interactive Decision Prompts (Choices)
    labelLarge = TextStyle(
        fontFamily = FontFamily.SansSerif,
        fontWeight = FontWeight.Medium,
        fontSize = 15.sp,
        lineHeight = 22.sp,
        letterSpacing = 0.3.sp,
        color = ParadoxColorTextPrimary
    ),

    // Metrics, Telemetry & Status Readouts
    labelSmall = TextStyle(
        fontFamily = FontFamily.Monospace,
        fontWeight = FontWeight.Normal,
        fontSize = 12.sp,
        lineHeight = 16.sp,
        letterSpacing = 0.5.sp,
        color = ParadoxColorTextSecondary
    )
)
