package org.example.project.config

object GameConstants {
    const val APP_NAME = "Fracture: The Tyrant Paradox"
    const val WORKING_TITLE = "Paradox Log"

    // Stat Ranges
    const val STAT_MIN = 0
    const val STAT_MAX = 100

    // Initial Defaults for Act I (Future Self)
    const val INITIAL_TYRANT_INDEX = 20
    const val INITIAL_TIMELINE_STABILITY = 85
    const val INITIAL_SELF_TRUST = 50

    // Danger Thresholds
    const val CRITICAL_STABILITY_THRESHOLD = 30
    const val HIGH_TYRANT_THRESHOLD = 70
    const val LOW_SELF_TRUST_THRESHOLD = 30

    // UI Configuration
    const val TYPEWRITER_CHAR_DELAY_MS = 18L
    const val AUTO_SAVE_SLOT = "autosave"
}
