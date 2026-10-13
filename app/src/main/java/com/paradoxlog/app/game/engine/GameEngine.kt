package com.paradoxlog.app.game.engine

import com.paradoxlog.app.domain.model.Choice
import com.paradoxlog.app.domain.model.Scene
import com.paradoxlog.app.domain.repository.ContentRepository
import com.paradoxlog.app.game.consequences.ConsequenceApplier
import com.paradoxlog.app.game.consequences.DefaultConsequenceApplier
import com.paradoxlog.app.game.endings.DefaultEndingEvaluator
import com.paradoxlog.app.game.endings.EndingEvaluator
import com.paradoxlog.app.game.progression.DefaultProgressionHandler
import com.paradoxlog.app.game.progression.ProgressionHandler
import com.paradoxlog.app.game.rules.ConditionEvaluator
import com.paradoxlog.app.game.rules.DefaultConditionEvaluator
import com.paradoxlog.app.game.state.GameState
import com.paradoxlog.app.game.state.TimelineProgress
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow

sealed class GameEngineResult {
    data class Success(val newState: GameState) : GameEngineResult()
    data class ChoiceUnavailable(val reason: String) : GameEngineResult()
    data class SceneNotFound(val sceneId: String) : GameEngineResult()
    data class ActNotFound(val actId: String) : GameEngineResult()
    data class ChoiceNotFound(val choiceId: String) : GameEngineResult()
    data class TimelineCollapsed(val state: GameState) : GameEngineResult()
    data class EndingReached(val endingId: String, val finalState: GameState) : GameEngineResult()
}

interface GameEngine {
    val state: StateFlow<GameState>

    suspend fun startNewGame(initialActId: String): GameEngineResult
    fun loadGameState(savedState: GameState)
    fun isChoiceAvailable(choice: Choice): Boolean
    suspend fun getAvailableChoicesForScene(scene: Scene): List<Choice>
    suspend fun selectChoice(scene: Scene, choiceId: String): GameEngineResult
    suspend fun advanceAct(nextActId: String): GameEngineResult
}

class DefaultGameEngine(
    private val contentRepository: ContentRepository,
    private val conditionEvaluator: ConditionEvaluator = DefaultConditionEvaluator(),
    private val consequenceApplier: ConsequenceApplier = DefaultConsequenceApplier(),
    private val progressionHandler: ProgressionHandler = DefaultProgressionHandler(),
    private val endingEvaluator: EndingEvaluator = DefaultEndingEvaluator(),
    initialState: GameState = GameState()
) : GameEngine {

    private val _state = MutableStateFlow(initialState)
    override val state: StateFlow<GameState> = _state.asStateFlow()

    override suspend fun startNewGame(initialActId: String): GameEngineResult {
        val act = contentRepository.getAct(initialActId)
            ?: return GameEngineResult.ActNotFound(initialActId)

        val freshState = GameState(
            currentActId = act.id,
            currentSceneId = act.initialSceneId,
            timeline = TimelineProgress(
                visitedScenes = setOf(act.initialSceneId)
            )
        )
        _state.value = freshState
        return GameEngineResult.Success(freshState)
    }

    override fun loadGameState(savedState: GameState) {
        _state.value = savedState
    }

    override fun isChoiceAvailable(choice: Choice): Boolean {
        return conditionEvaluator.isChoiceAvailable(choice, _state.value)
    }

    override suspend fun getAvailableChoicesForScene(scene: Scene): List<Choice> {
        val currentState = _state.value
        return scene.choices.filter { conditionEvaluator.isChoiceAvailable(it, currentState) }
    }

    override suspend fun selectChoice(scene: Scene, choiceId: String): GameEngineResult {
        val choice = scene.choices.find { it.id == choiceId }
            ?: return GameEngineResult.ChoiceNotFound(choiceId)

        val currentState = _state.value

        if (!conditionEvaluator.isChoiceAvailable(choice, currentState)) {
            return GameEngineResult.ChoiceUnavailable("Choice preconditions not met")
        }

        // 1. Apply consequences to generate updated state
        val stateWithConsequences = consequenceApplier.applyConsequence(choice.consequence, currentState)

        // 2. Timeline-collapse event check: stability reaching 0 is an independent engine-level event,
        // not an automatic narrative ending selection.
        if (stateWithConsequences.timelineStability <= 0) {
            _state.value = stateWithConsequences
            return GameEngineResult.TimelineCollapsed(stateWithConsequences)
        }

        // 3. Evaluate ending through dedicated ending evaluator
        val evaluatedEnding = endingEvaluator.evaluate(stateWithConsequences)
        val stateWithEnding = stateWithConsequences.copy(ending = evaluatedEnding)

        if (evaluatedEnding.isResolved) {
            _state.value = stateWithEnding
            return GameEngineResult.EndingReached(
                endingId = evaluatedEnding.eligibleEnding ?: "UNKNOWN",
                finalState = stateWithEnding
            )
        }

        // 4. Check if choice consequence triggers an immediate Act transition
        val nextActId = choice.consequence?.nextActId
        val finalState = if (nextActId != null && nextActId != stateWithEnding.currentActId) {
            val nextAct = contentRepository.getAct(nextActId)
                ?: return GameEngineResult.ActNotFound(nextActId)
            progressionHandler.transitionAct(
                completedActId = stateWithEnding.currentActId,
                nextActId = nextAct.id,
                nextActInitialSceneId = nextAct.initialSceneId,
                state = stateWithEnding
            )
        } else {
            progressionHandler.transitionScene(
                currentSceneId = scene.id,
                choice = choice,
                state = stateWithEnding
            )
        }

        _state.value = finalState
        return GameEngineResult.Success(finalState)
    }

    override suspend fun advanceAct(nextActId: String): GameEngineResult {
        val currentState = _state.value
        val nextAct = contentRepository.getAct(nextActId)
            ?: return GameEngineResult.ActNotFound(nextActId)

        val newState = progressionHandler.transitionAct(
            completedActId = currentState.currentActId,
            nextActId = nextAct.id,
            nextActInitialSceneId = nextAct.initialSceneId,
            state = currentState
        )
        _state.value = newState
        return GameEngineResult.Success(newState)
    }
}
