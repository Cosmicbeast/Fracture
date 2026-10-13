package com.paradoxlog.app.feature.consequence

import androidx.compose.runtime.Immutable

/**
 * Immutable presentation model representing a single variable shift resulting from a choice.
 */
@Immutable
data class StatDeltaUi(
    val statName: String,
    val delta: Int
)

/**
 * Presentation projection of authoritative consequence outcome.
 *
 * ARCHITECTURAL CONSTRAINTS:
 * 1. Contains only non-zero variable deltas and consequence feedback.
 * 2. Does NOT calculate values in Compose; populated strictly by the ViewModel/engine pipeline.
 * 3. Does NOT dictate what destination comes next.
 */
@Immutable
data class ConsequenceUiState(
    val choiceText: String = "",
    val statDeltas: List<StatDeltaUi> = emptyList(),
    val itemsAdded: List<String> = emptyList(),
    val itemsRemoved: List<String> = emptyList()
)
