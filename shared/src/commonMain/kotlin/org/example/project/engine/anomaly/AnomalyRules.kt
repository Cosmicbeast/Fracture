package org.example.project.engine.anomaly

import org.example.project.core.GameState
import org.example.project.core.enums.AnomalyType
import org.example.project.core.enums.ToolType

object AnomalyRules {
    fun canSuppressAnomaly(type: AnomalyType, state: GameState): Boolean {
        return state.hasTool(ToolType.PARADOX_GAUNTLET) || state.tyrantIndex >= 50
    }
}
