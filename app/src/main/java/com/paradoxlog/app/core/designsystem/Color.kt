package com.paradoxlog.app.core.designsystem

import androidx.compose.runtime.Immutable
import androidx.compose.ui.graphics.Color

/**
 * Paradox Log - Canonical Color Palette
 *
 * Designed according to docs/design/design-system.md:
 * - Minimalist & OLED-friendly monochromatic foundation
 * - Restrained, atmospheric reality status accents
 * - Zero visual clutter or neon distractions
 */

// Monochromatic Foundation
val ParadoxColorBackground = Color(0xFF0B0C0E)       // Deep black canvas (OLED baseline)
val ParadoxColorSurface = Color(0xFF14171A)          // Minimal elevation distinction for choice containers
val ParadoxColorSurfaceVariant = Color(0xFF1C2024)   // Elevated cards/modals
val ParadoxColorBorder = Color(0xFF2D333B)           // Hairline subtle divider
val ParadoxColorBorderFocused = Color(0xFF484F58)    // Subtle highlight border

// Typographic Contrast Hierarchy
val ParadoxColorTextPrimary = Color(0xFFE6EDF3)      // Crisp off-white prose and dialogue
val ParadoxColorTextSecondary = Color(0xFF8B949E)    // Subdued slate gray for metadata/speakers
val ParadoxColorTextMuted = Color(0xFF57606A)        // Faint timestamps and disabled choices

// Reality & Narrative Indicator Accents
val ParadoxColorStabilityCyan = Color(0xFF38BDF8)    // Reality stability normal (Cool Cyan)
val ParadoxColorStabilityAmber = Color(0xFFF59E0B)   // Reality stability critical warning (Amber)
val ParadoxColorStabilityCrimson = Color(0xFFEF4444) // Reality fracture / zero stability (Crimson)
val ParadoxColorTyrantAccent = Color(0xFFE11D48)     // Authoritarian index indicator (Crimson)
val ParadoxColorSelfTrustAccent = Color(0xFFA855F7)  // Psychological resonance indicator (Violet)

/**
 * Immutable holder for game-specific state accents
 * exposed via CompositionLocal.
 */
@Immutable
data class ParadoxCustomColors(
    val background: Color = ParadoxColorBackground,
    val surface: Color = ParadoxColorSurface,
    val surfaceVariant: Color = ParadoxColorSurfaceVariant,
    val border: Color = ParadoxColorBorder,
    val borderFocused: Color = ParadoxColorBorderFocused,
    val textPrimary: Color = ParadoxColorTextPrimary,
    val textSecondary: Color = ParadoxColorTextSecondary,
    val textMuted: Color = ParadoxColorTextMuted,
    val stabilityNormal: Color = ParadoxColorStabilityCyan,
    val stabilityWarning: Color = ParadoxColorStabilityAmber,
    val stabilityCritical: Color = ParadoxColorStabilityCrimson,
    val tyrantAccent: Color = ParadoxColorTyrantAccent,
    val selfTrustAccent: Color = ParadoxColorSelfTrustAccent
)
