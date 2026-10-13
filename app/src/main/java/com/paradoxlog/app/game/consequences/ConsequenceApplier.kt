package com.paradoxlog.app.game.consequences

import com.paradoxlog.app.domain.model.ChoiceConsequence
import com.paradoxlog.app.game.state.GameState
import com.paradoxlog.app.game.state.GameVariables
import com.paradoxlog.app.game.state.InventoryState

interface ConsequenceApplier {
    fun applyConsequence(consequence: ChoiceConsequence?, state: GameState): GameState
}

class DefaultConsequenceApplier : ConsequenceApplier {

    companion object {
        const val MIN_STABILITY = 0
        const val MAX_STABILITY = 100
        const val MIN_SELF_TRUST = 0
        const val MAX_SELF_TRUST = 100
        const val MIN_TYRANT_INDEX = 0
    }

    override fun applyConsequence(consequence: ChoiceConsequence?, state: GameState): GameState {
        if (consequence == null) return state

        // 1. Calculate variable deltas with boundary clamping
        val newTyrantIndex = (state.tyrantIndex + consequence.tyrantIndexDelta).coerceAtLeast(MIN_TYRANT_INDEX)
        val newTimelineStability = (state.timelineStability + consequence.timelineStabilityDelta).coerceIn(MIN_STABILITY, MAX_STABILITY)
        val newSelfTrust = (state.selfTrust + consequence.selfTrustDelta).coerceIn(MIN_SELF_TRUST, MAX_SELF_TRUST)

        val updatedVariables = GameVariables(
            tyrantIndex = newTyrantIndex,
            timelineStability = newTimelineStability,
            selfTrust = newSelfTrust
        )

        // 2. Update persistent flags
        val updatedFlags = (state.flags + consequence.setFlags) - consequence.clearFlags

        // 3. Update inventory items
        val updatedItemIds = (state.inventory.itemIds + consequence.addedItems) - consequence.removedItems
        val updatedInventory = InventoryState(itemIds = updatedItemIds)

        // Return updated state without modifying ending state (ending resolution is handled by EndingEvaluator)
        return state.copy(
            variables = updatedVariables,
            flags = updatedFlags,
            inventory = updatedInventory
        )
    }
}
