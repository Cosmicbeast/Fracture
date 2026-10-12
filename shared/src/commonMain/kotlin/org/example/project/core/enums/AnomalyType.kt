package org.example.project.core.enums

import kotlinx.serialization.Serializable

@Serializable
enum class AnomalyType(val displayName: String, val description: String) {
    TEMPORAL_LOOP(
        displayName = "Temporal Loop",
        description = "Events recur in cyclical causality, trapping moments in an infinite echo."
    ),
    REVERSE_CAUSALITY(
        displayName = "Reverse Causality",
        description = "Consequences manifest prior to the inciting choice."
    ),
    MEMORY_DESYNC(
        displayName = "Memory Desynchronization",
        description = "Discordance between what occurred and what the protagonist remembers."
    ),
    AGING_SLIP(
        displayName = "Aging Slip",
        description = "Biological decay accelerating or reversing under localized dilation."
    ),
    TIMELINE_DECAY(
        displayName = "Timeline Decay",
        description = "Structural collapse of reality strands due to excessive paradox debt."
    )
}
