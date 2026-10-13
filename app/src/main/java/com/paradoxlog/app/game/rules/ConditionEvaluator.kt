package com.paradoxlog.app.game.rules

import com.paradoxlog.app.domain.model.Choice
import com.paradoxlog.app.domain.model.ChoiceCondition
import com.paradoxlog.app.game.state.GameState

interface ConditionEvaluator {
    fun evaluate(condition: ChoiceCondition?, state: GameState): Boolean
    fun isChoiceAvailable(choice: Choice, state: GameState): Boolean
}

class DefaultConditionEvaluator : ConditionEvaluator {

    override fun evaluate(condition: ChoiceCondition?, state: GameState): Boolean {
        if (condition == null) return true

        // 1. Required flags must all be present
        if (condition.requiredFlags.isNotEmpty() && !state.flags.containsAll(condition.requiredFlags)) {
            return false
        }

        // 2. Forbidden flags must none be present
        if (condition.forbiddenFlags.isNotEmpty() && condition.forbiddenFlags.any { state.flags.contains(it) }) {
            return false
        }

        // 3. Tyrant Index bounds
        condition.minTyrantIndex?.let { if (state.tyrantIndex < it) return false }
        condition.maxTyrantIndex?.let { if (state.tyrantIndex > it) return false }

        // 4. Timeline Stability bounds
        condition.minTimelineStability?.let { if (state.timelineStability < it) return false }
        condition.maxTimelineStability?.let { if (state.timelineStability > it) return false }

        // 5. Self-Trust bounds
        condition.minSelfTrust?.let { if (state.selfTrust < it) return false }
        condition.maxSelfTrust?.let { if (state.selfTrust > it) return false }

        // 6. Required items must all be in inventory
        if (condition.requiredItems.isNotEmpty() && !state.inventory.itemIds.containsAll(condition.requiredItems)) {
            return false
        }

        return true
    }

    override fun isChoiceAvailable(choice: Choice, state: GameState): Boolean {
        return evaluate(choice.condition, state)
    }
}
