package com.paradoxlog.app.feature.story

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.paradoxlog.app.domain.model.Scene
import com.paradoxlog.app.domain.repository.ContentRepository
import com.paradoxlog.app.game.engine.GameEngine
import com.paradoxlog.app.game.engine.GameEngineResult
import com.paradoxlog.app.game.state.GameState
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

/**
 * GameViewModel orchestrates UI presentation and forwards user actions
 * to the authoritative GameEngine.
 *
 * ARCHITECTURAL CONSTRAINTS:
 * 1. GameEngine is the SINGLE source of truth. GameViewModel does NOT own GameState.
 * 2. GameUiState is strictly a derived presentation projection.
 * 3. GameViewModel contains ZERO choice condition evaluation, consequence calculation,
 *    ending rules, or narrative branching logic.
 * 4. Content is retrieved dynamically from ContentRepository—no hardcoded story text.
 */
class GameViewModel(
    private val gameEngine: GameEngine,
    private val contentRepository: ContentRepository
) : ViewModel() {

    private val _uiState = MutableStateFlow(GameUiState(isLoading = true))
    val uiState: StateFlow<GameUiState> = _uiState.asStateFlow()

    private val _effects = Channel<GameUiEffect>(Channel.BUFFERED)
    val effects: Flow<GameUiEffect> = _effects.receiveAsFlow()

    init {
        // Observe authoritative GameState emissions and project into presentation state
        viewModelScope.launch {
            gameEngine.state.collect { gameState ->
                projectGameStateToUi(gameState)
            }
        }
    }

    /**
     * Projects the authoritative GameState + canonical ContentRepository metadata
     * into a read-only GameUiState.
     */
    private suspend fun projectGameStateToUi(gameState: GameState) {
        if (gameState.currentActId.isBlank() || gameState.currentSceneId.isBlank()) {
            _uiState.update {
                it.copy(
                    isLoading = false,
                    actId = "",
                    sceneId = "",
                    dialogues = emptyList(),
                    choices = emptyList(),
                    tyrantIndex = gameState.tyrantIndex,
                    timelineStability = gameState.timelineStability,
                    selfTrust = gameState.selfTrust,
                    inventoryCount = gameState.inventory.itemIds.size
                )
            }
            return
        }

        val act = contentRepository.getAct(gameState.currentActId)
        val scene = contentRepository.getScene(gameState.currentActId, gameState.currentSceneId)

        if (scene == null) {
            _uiState.update {
                it.copy(
                    isLoading = false,
                    errorMessage = "Scene not found: ${gameState.currentSceneId}"
                )
            }
            return
        }

        // Evaluate choice availability through GameEngine, NOT UI
        val uiChoices = scene.choices.map { choice ->
            ChoiceUi(
                id = choice.id,
                text = choice.text,
                isAvailable = gameEngine.isChoiceAvailable(choice)
            )
        }

        _uiState.update {
            it.copy(
                isLoading = false,
                actId = gameState.currentActId,
                actTitle = act?.title ?: gameState.currentActId,
                sceneId = scene.id,
                sceneTitle = scene.title,
                dialogues = scene.dialogues,
                choices = uiChoices,
                tyrantIndex = gameState.tyrantIndex,
                timelineStability = gameState.timelineStability,
                selfTrust = gameState.selfTrust,
                inventoryCount = gameState.inventory.itemIds.size,
                isTimelineCollapsed = gameState.timelineStability <= 0,
                resolvedEndingId = gameState.ending.eligibleEnding,
                errorMessage = null
            )
        }
    }

    /**
     * Initializes a new playthrough via GameEngine.
     */
    fun startNewGame(initialActId: String) {
        viewModelScope.launch {
            _uiState.update { it.copy(isLoading = true) }
            when (val result = gameEngine.startNewGame(initialActId)) {
                is GameEngineResult.Success -> {
                    // Explicitly project new state in case StateFlow dedupes identical state
                    projectGameStateToUi(result.newState)
                }
                is GameEngineResult.ActNotFound -> {
                    _uiState.update { it.copy(isLoading = false) }
                    _effects.send(GameUiEffect.ShowMessage("Initial act not found: ${result.actId}"))
                }
                else -> {
                    _uiState.update { it.copy(isLoading = false) }
                }
            }
        }
    }

    /**
     * Forwards player choice selection to the GameEngine.
     */
    private var pendingEffect: GameUiEffect? = null
    private var pendingTimelineChange: com.paradoxlog.app.feature.timeline.TimelineChangeUiState? = null

    /**
     * Forwards player choice selection to the GameEngine and projects authoritative consequence data.
     */
    fun selectChoice(choiceId: String) {
        viewModelScope.launch {
            val currentState = gameEngine.state.value
            val scene = contentRepository.getScene(currentState.currentActId, currentState.currentSceneId)

            if (scene == null) {
                _effects.send(GameUiEffect.ShowMessage("Cannot select choice: Current scene not found"))
                return@launch
            }

            val selectedChoice = scene.choices.find { it.id == choiceId }

            when (val result = gameEngine.selectChoice(scene, choiceId)) {
                is GameEngineResult.Success -> {
                    val updatedState = result.newState
                    val timelineChange = com.paradoxlog.app.feature.timeline.TimelineChangeUiState(
                        actId = updatedState.currentActId,
                        sceneId = updatedState.currentSceneId,
                        recordedDecisionChoiceId = selectedChoice?.id,
                        timelineStability = updatedState.timelineStability,
                        totalVisitedScenesCount = updatedState.timeline.visitedScenes.size,
                        totalDecisionsCount = updatedState.timeline.decisionsMade.size
                    )
                    projectOutcome(selectedChoice, timelineChange, pending = null)
                }
                is GameEngineResult.TimelineCollapsed -> {
                    val collapseEffect = GameUiEffect.TriggerTimelineCollapse(
                        stability = result.state.timelineStability
                    )
                    val timelineChange = com.paradoxlog.app.feature.timeline.TimelineChangeUiState(
                        actId = result.state.currentActId,
                        sceneId = result.state.currentSceneId,
                        recordedDecisionChoiceId = selectedChoice?.id,
                        timelineStability = result.state.timelineStability,
                        totalVisitedScenesCount = result.state.timeline.visitedScenes.size,
                        totalDecisionsCount = result.state.timeline.decisionsMade.size
                    )
                    projectOutcome(selectedChoice, timelineChange, pending = collapseEffect)
                }
                is GameEngineResult.EndingReached -> {
                    val endingEffect = GameUiEffect.NavigateToEnding(
                        endingId = result.endingId
                    )
                    val timelineChange = com.paradoxlog.app.feature.timeline.TimelineChangeUiState(
                        actId = result.finalState.currentActId,
                        sceneId = result.finalState.currentSceneId,
                        recordedDecisionChoiceId = selectedChoice?.id,
                        timelineStability = result.finalState.timelineStability,
                        totalVisitedScenesCount = result.finalState.timeline.visitedScenes.size,
                        totalDecisionsCount = result.finalState.timeline.decisionsMade.size
                    )
                    projectOutcome(selectedChoice, timelineChange, pending = endingEffect)
                }
                is GameEngineResult.ChoiceUnavailable -> {
                    _effects.send(GameUiEffect.ShowMessage(result.reason))
                }
                is GameEngineResult.ChoiceNotFound -> {
                    _effects.send(GameUiEffect.ShowMessage("Choice not found: ${result.choiceId}"))
                }
                is GameEngineResult.SceneNotFound -> {
                    _effects.send(GameUiEffect.ShowMessage("Scene not found: ${result.sceneId}"))
                }
                is GameEngineResult.ActNotFound -> {
                    _effects.send(GameUiEffect.ShowMessage("Act not found: ${result.actId}"))
                }
            }
        }
    }

    private suspend fun projectOutcome(
        choice: com.paradoxlog.app.domain.model.Choice?,
        timelineChange: com.paradoxlog.app.feature.timeline.TimelineChangeUiState,
        pending: GameUiEffect?
    ) {
        val consequence = choice?.consequence
        if (consequence == null) {
            if (pending != null) {
                _effects.send(pending)
            } else {
                _uiState.update { it.copy(activeTimelineChange = timelineChange) }
            }
            return
        }

        val statDeltas = mutableListOf<com.paradoxlog.app.feature.consequence.StatDeltaUi>()
        if (consequence.timelineStabilityDelta != 0) {
            statDeltas.add(
                com.paradoxlog.app.feature.consequence.StatDeltaUi(
                    statName = com.paradoxlog.app.core.constants.UiLabels.LABEL_STABILITY,
                    delta = consequence.timelineStabilityDelta
                )
            )
        }
        if (consequence.selfTrustDelta != 0) {
            statDeltas.add(
                com.paradoxlog.app.feature.consequence.StatDeltaUi(
                    statName = com.paradoxlog.app.core.constants.UiLabels.LABEL_SELF_TRUST,
                    delta = consequence.selfTrustDelta
                )
            )
        }
        if (consequence.tyrantIndexDelta != 0) {
            statDeltas.add(
                com.paradoxlog.app.feature.consequence.StatDeltaUi(
                    statName = com.paradoxlog.app.core.constants.UiLabels.LABEL_TYRANT_INDEX,
                    delta = consequence.tyrantIndexDelta
                )
            )
        }

        val hasConsequenceFeedback = statDeltas.isNotEmpty() ||
                consequence.addedItems.isNotEmpty() ||
                consequence.removedItems.isNotEmpty()

        pendingEffect = pending

        if (hasConsequenceFeedback) {
            pendingTimelineChange = timelineChange
            _uiState.update {
                it.copy(
                    activeConsequence = com.paradoxlog.app.feature.consequence.ConsequenceUiState(
                        choiceText = choice.text,
                        statDeltas = statDeltas,
                        itemsAdded = consequence.addedItems.toList(),
                        itemsRemoved = consequence.removedItems.toList()
                    )
                )
            }
        } else {
            _uiState.update { it.copy(activeTimelineChange = timelineChange) }
        }
    }

    /**
     * Acknowledges the consequence presentation and reveals the Timeline Change stage.
     */
    fun acknowledgeConsequence() {
        val nextTimelineChange = pendingTimelineChange
        pendingTimelineChange = null
        _uiState.update {
            it.copy(
                activeConsequence = null,
                activeTimelineChange = nextTimelineChange
            )
        }
        if (nextTimelineChange == null) {
            dispatchPendingEffect()
        }
    }

    /**
     * Acknowledges the Timeline Change presentation and resumes the gameplay flow.
     */
    fun acknowledgeTimelineChange() {
        _uiState.update { it.copy(activeTimelineChange = null) }
        dispatchPendingEffect()
    }

    private fun dispatchPendingEffect() {
        pendingEffect?.let { effect ->
            viewModelScope.launch {
                _effects.send(effect)
            }
            pendingEffect = null
        }
    }

    /**
     * Forwards an act advance request to the GameEngine.
     */
    fun advanceAct(nextActId: String) {
        viewModelScope.launch {
            val previousActId = gameEngine.state.value.currentActId
            when (val result = gameEngine.advanceAct(nextActId)) {
                is GameEngineResult.Success -> {
                    _effects.send(
                        GameUiEffect.NavigateToActTransition(
                            completedActId = previousActId,
                            nextActId = nextActId
                        )
                    )
                }
                is GameEngineResult.ActNotFound -> {
                    _effects.send(GameUiEffect.ShowMessage("Next act not found: ${result.actId}"))
                }
                else -> Unit
            }
        }
    }

    /**
     * Dismisses any active error message in the UI state.
     */
    fun dismissError() {
        _uiState.update { it.copy(errorMessage = null) }
    }
}
