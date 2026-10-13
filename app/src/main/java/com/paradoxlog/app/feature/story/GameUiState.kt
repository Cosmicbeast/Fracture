package com.paradoxlog.app.feature.story

import androidx.compose.runtime.Immutable
import com.paradoxlog.app.domain.model.Dialogue

/**
 * Immutable presentation model representing a selectable choice.
 *
 * Precondition availability is evaluated by the game engine, NOT the UI.
 */
@Immutable
data class ChoiceUi(
    val id: String,
    val text: String,
    val isAvailable: Boolean
)

/**
 * Presentation projection of the active game state for Compose consumption.
 *
 * NOTE: This is strictly a read-only presentation projection derived from
 * GameEngine.state and ContentRepository. It is NEVER an authoritative state holder.
 */
@Immutable
data class GameUiState(
    val isLoading: Boolean = false,
    val actId: String = "",
    val actTitle: String = "",
    val sceneId: String = "",
    val sceneTitle: String = "",
    val dialogues: List<Dialogue> = emptyList(),
    val choices: List<ChoiceUi> = emptyList(),
    val tyrantIndex: Int = 0,
    val timelineStability: Int = 100,
    val selfTrust: Int = 50,
    val inventoryCount: Int = 0,
    val isTimelineCollapsed: Boolean = false,
    val resolvedEndingId: String? = null,
    val activeConsequence: com.paradoxlog.app.feature.consequence.ConsequenceUiState? = null,
    val activeTimelineChange: com.paradoxlog.app.feature.timeline.TimelineChangeUiState? = null,
    val errorMessage: String? = null
)
