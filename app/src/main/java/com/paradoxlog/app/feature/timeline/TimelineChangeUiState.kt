package com.paradoxlog.app.feature.timeline

import androidx.compose.runtime.Immutable

/**
 * Presentation projection representing an authoritative timeline change
 * resulting from a player choice.
 *
 * ARCHITECTURAL CONSTRAINTS:
 * 1. Contains ONLY authoritative data from GameState.timeline and progression results.
 * 2. Does NOT invent narrative events, story descriptions, or speculative labels.
 * 3. Strictly immutable and read-only.
 */
@Immutable
data class TimelineChangeUiState(
    val actId: String = "",
    val sceneId: String = "",
    val recordedDecisionChoiceId: String? = null,
    val timelineStability: Int? = null,
    val totalVisitedScenesCount: Int = 0,
    val totalDecisionsCount: Int = 0
)
