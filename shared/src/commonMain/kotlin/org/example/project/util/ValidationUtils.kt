package org.example.project.util

import org.example.project.core.GameState

object ValidationUtils {
    fun validateGameState(state: GameState): Boolean {
        return state.tyrantIndex in 0..100 &&
               state.timelineStability in 0..100 &&
               state.selfTrust in 0..100 &&
               state.currentSceneId.isNotBlank()
    }
}
