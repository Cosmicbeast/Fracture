package com.paradoxlog.app.game.endings

import com.paradoxlog.app.game.state.EndingState
import com.paradoxlog.app.game.state.GameState

enum class ParadoxEnding(val id: String, val title: String) {
    THE_TYRANT("THE_TYRANT", "The Tyrant"),
    THE_SACRIFICE("THE_SACRIFICE", "The Sacrifice"),
    THE_RESET("THE_RESET", "The Reset"),
    THE_FRACTURE("THE_FRACTURE", "The Fracture"),
    THE_RECONCILIATION("THE_RECONCILIATION", "The Reconciliation")
}

interface EndingEvaluator {
    fun evaluate(state: GameState): EndingState
    fun isEndingReached(state: GameState): Boolean
}

class DefaultEndingEvaluator : EndingEvaluator {

    override fun evaluate(state: GameState): EndingState {
        // If the state already has a resolved ending, retain it
        if (state.ending.isResolved) {
            return state.ending
        }

        // Dedicated ending resolution system.
        // Specific narrative thresholds and hidden conditions across all five endings
        // (The Tyrant, The Sacrifice, The Reset, The Fracture, The Reconciliation)
        // remain [TBD] per project design until Act VI narrative calibration.
        return state.ending
    }

    override fun isEndingReached(state: GameState): Boolean {
        return evaluate(state).isResolved
    }
}
