package org.example.project.config

import org.example.project.core.GameState

object BalanceRules {
    fun isTimelineCritical(state: GameState): Boolean =
        state.timelineStability <= GameConstants.CRITICAL_STABILITY_THRESHOLD

    fun isTyrannyImminent(state: GameState): Boolean =
        state.tyrantIndex >= GameConstants.HIGH_TYRANT_THRESHOLD

    fun isTrustSevered(state: GameState): Boolean =
        state.selfTrust <= GameConstants.LOW_SELF_TRUST_THRESHOLD
}
