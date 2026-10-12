package org.example.project.engine.anomaly

import org.example.project.core.GameState
import org.example.project.core.enums.AnomalyType
import org.example.project.util.Logger

object AnomalyProcessor {
    fun processAnomalyEncounter(anomaly: AnomalyType, state: GameState): GameState {
        Logger.i("Processing anomaly encounter: ${anomaly.displayName}")
        val stabilityImpact = when (anomaly) {
            AnomalyType.TEMPORAL_LOOP -> -10
            AnomalyType.REVERSE_CAUSALITY -> -15
            AnomalyType.MEMORY_DESYNC -> -5
            AnomalyType.AGING_SLIP -> -12
            AnomalyType.TIMELINE_DECAY -> -20
        }
        val newStability = (state.timelineStability + stabilityImpact).coerceIn(0, 100)
        return state.copy(timelineStability = newStability)
    }
}
