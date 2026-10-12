package org.example.project.core.enums

import kotlinx.serialization.Serializable

@Serializable
enum class ToolType(
    val displayName: String,
    val concept: String,
    val description: String
) {
    CHRONO_BLADE(
        displayName = "Chrono Blade",
        concept = "Awareness",
        description = "A weapon forged from crystallised consequence. Reveals unseen causal repercussions in dialogue options."
    ),
    AION_COMPASS(
        displayName = "Aion Compass",
        concept = "Control",
        description = "A gyroscopic navigational tool that illuminates choices that preserve Timeline Stability."
    ),
    PARADOX_GAUNTLET(
        displayName = "Paradox Gauntlet",
        concept = "Power",
        description = "A heavy gauntlet that forcefully absorbs or suppresses temporal anomalies at great personal cost."
    ),
    CODEX_OF_REGRESSION(
        displayName = "Codex of Regression",
        concept = "Regret",
        description = "An inscribed archive permitting controlled rewrites of irreversible decisions."
    ),
    SINGULARITY_CORE(
        displayName = "Singularity Core",
        concept = "Acceptance",
        description = "The ultimate focal mechanism capable of merging or releasing divergent timeline branches."
    )
}
