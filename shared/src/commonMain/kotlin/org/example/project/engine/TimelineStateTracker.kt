package org.example.project.engine

import org.example.project.core.GameState

object TimelineStateTracker {

    enum class StabilityStatus(val label: String, val description: String) {
        PRISTINE("Pristine", "The chronological weave is intact."),
        STABLE("Stable", "Minor fluctuations, but causality holds."),
        STRAINED("Strained", "Micro-fractures ripple through the causal continuum."),
        DECAYING("Decaying", "Structural reality collapse is underway."),
        CRITICAL("Catastrophic", "Timeline dissolution is imminent.")
    }

    enum class TyrantPosture(val label: String, val description: String) {
        RELUCTANT("Reluctant Guardian", "Actions driven by necessity rather than control."),
        PRAGMATIC("Pragmatic Overseer", "Willing to enforce outcomes to secure the future."),
        AUTHORITARIAN("Cold Sovereign", "Personal agency has been subordinated to order."),
        TYRANT("Tyrant In Ascension", "No divergence from the chosen path is tolerated.")
    }

    enum class SelfTrustStatus(val label: String, val description: String) {
        ALIGNED("Synchronized", "Past and Future selves act in mutual belief."),
        SKEPTICAL("Skeptical Accord", "Doubts exist, but cooperation remains viable."),
        DIVERGENT("Divergent Ideals", "The Past Self resists the Future Self's authority."),
        SEVERED("Severed Identity", "The two selves view each other as adversaries.")
    }

    fun getStabilityStatus(state: GameState): StabilityStatus = when {
        state.timelineStability >= 90 -> StabilityStatus.PRISTINE
        state.timelineStability >= 65 -> StabilityStatus.STABLE
        state.timelineStability >= 45 -> StabilityStatus.STRAINED
        state.timelineStability >= 25 -> StabilityStatus.DECAYING
        else -> StabilityStatus.CRITICAL
    }

    fun getTyrantPosture(state: GameState): TyrantPosture = when {
        state.tyrantIndex < 30 -> TyrantPosture.RELUCTANT
        state.tyrantIndex < 55 -> TyrantPosture.PRAGMATIC
        state.tyrantIndex < 75 -> TyrantPosture.AUTHORITARIAN
        else -> TyrantPosture.TYRANT
    }

    fun getSelfTrustStatus(state: GameState): SelfTrustStatus = when {
        state.selfTrust >= 75 -> SelfTrustStatus.ALIGNED
        state.selfTrust >= 50 -> SelfTrustStatus.SKEPTICAL
        state.selfTrust >= 25 -> SelfTrustStatus.DIVERGENT
        else -> SelfTrustStatus.SEVERED
    }
}
