package com.paradoxlog.app.feature.story

import com.paradoxlog.app.domain.model.Act
import com.paradoxlog.app.domain.model.Choice
import com.paradoxlog.app.domain.model.ChoiceConsequence
import com.paradoxlog.app.domain.model.Dialogue
import com.paradoxlog.app.domain.model.Item
import com.paradoxlog.app.domain.model.Scene
import com.paradoxlog.app.domain.repository.ContentRepository
import com.paradoxlog.app.game.engine.GameEngine
import com.paradoxlog.app.game.engine.GameEngineResult
import com.paradoxlog.app.game.state.EndingState
import com.paradoxlog.app.game.state.GameState
import com.paradoxlog.app.game.state.GameVariables
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking
import kotlinx.coroutines.test.StandardTestDispatcher
import kotlinx.coroutines.test.TestDispatcher
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.setMain
import org.junit.After
import org.junit.Assert.assertEquals
import org.junit.Assert.assertFalse
import org.junit.Assert.assertNotNull
import org.junit.Assert.assertNull
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Test

@OptIn(ExperimentalCoroutinesApi::class)
class GameViewModelTest {

    private val testDispatcher: TestDispatcher = StandardTestDispatcher()

    private class FakeContentRepository : ContentRepository {
        val acts = mutableMapOf<String, Act>()
        val scenes = mutableMapOf<String, Scene>()

        override suspend fun getAct(actId: String): Act? = acts[actId]
        override suspend fun getScenesForAct(actId: String): List<Scene> = scenes.values.filter { it.actId == actId }
        override suspend fun getScene(actId: String, sceneId: String): Scene? = scenes[sceneId]
        override suspend fun getItems(): List<Item> = emptyList()
    }

    private class FakeGameEngine(
        initialState: GameState = GameState()
    ) : GameEngine {
        private val _state = MutableStateFlow(initialState)
        override val state: StateFlow<GameState> = _state.asStateFlow()

        var selectChoiceCallCount = 0
        var lastSelectedChoiceId: String? = null
        var simulatedResult: GameEngineResult? = null
        val availableChoiceIds = mutableSetOf<String>()

        override suspend fun startNewGame(initialActId: String): GameEngineResult {
            val newState = GameState(currentActId = initialActId, currentSceneId = "scene-01")
            _state.value = newState
            return GameEngineResult.Success(newState)
        }

        override fun loadGameState(savedState: GameState) {
            _state.value = savedState
        }

        override fun isChoiceAvailable(choice: Choice): Boolean {
            return availableChoiceIds.contains(choice.id)
        }

        override suspend fun getAvailableChoicesForScene(scene: Scene): List<Choice> {
            return scene.choices.filter { availableChoiceIds.contains(it.id) }
        }

        override suspend fun selectChoice(scene: Scene, choiceId: String): GameEngineResult {
            selectChoiceCallCount++
            lastSelectedChoiceId = choiceId
            return simulatedResult ?: run {
                val nextState = _state.value.copy(currentSceneId = "scene-02")
                _state.value = nextState
                GameEngineResult.Success(nextState)
            }
        }

        override suspend fun advanceAct(nextActId: String): GameEngineResult {
            val nextState = _state.value.copy(currentActId = nextActId, currentSceneId = "scene-01")
            _state.value = nextState
            return GameEngineResult.Success(nextState)
        }

        fun updateState(newState: GameState) {
            _state.value = newState
        }
    }

    private lateinit var contentRepository: FakeContentRepository
    private lateinit var gameEngine: FakeGameEngine

    @Before
    fun setUp() {
        Dispatchers.setMain(testDispatcher)
        contentRepository = FakeContentRepository()

        contentRepository.acts["act-01"] = Act(
            id = "act-01",
            actNumber = 1,
            title = "Act I: Threshold",
            initialSceneId = "scene-01",
            sceneIds = listOf("scene-01", "scene-02")
        )

        contentRepository.scenes["scene-01"] = Scene(
            id = "scene-01",
            actId = "act-01",
            title = "The Archive Room",
            dialogues = listOf(
                Dialogue("d-1", "PROTAGONIST", "The terminal hums.")
            ),
            choices = listOf(
                Choice("c-1", "Inspect terminal", "scene-02"),
                Choice("c-2", "Bypass security", "scene-03")
            )
        )

        gameEngine = FakeGameEngine(
            initialState = GameState(
                currentActId = "act-01",
                currentSceneId = "scene-01",
                variables = GameVariables(tyrantIndex = 10, timelineStability = 85, selfTrust = 60)
            )
        )
        gameEngine.availableChoiceIds.add("c-1") // c-2 is locked
    }

    @After
    fun tearDown() {
        Dispatchers.resetMain()
    }

    @Test
    fun `test initial state projects engine state without holding second GameState`() = runBlocking {
        val viewModel = GameViewModel(gameEngine, contentRepository)
        testDispatcher.scheduler.advanceUntilIdle()

        val uiState = viewModel.uiState.value
        assertEquals("act-01", uiState.actId)
        assertEquals("Act I: Threshold", uiState.actTitle)
        assertEquals("scene-01", uiState.sceneId)
        assertEquals("The Archive Room", uiState.sceneTitle)
        assertEquals(85, uiState.timelineStability)
        assertEquals(10, uiState.tyrantIndex)
        assertEquals(60, uiState.selfTrust)
        assertEquals(2, uiState.choices.size)

        // Availability evaluated through engine, not UI
        val choice1 = uiState.choices.first { it.id == "c-1" }
        val choice2 = uiState.choices.first { it.id == "c-2" }
        assertTrue(choice1.isAvailable)
        assertFalse(choice2.isAvailable)
    }

    @Test
    fun `test selecting choice forwards directly to engine without UI-side calculations`() = runBlocking {
        val viewModel = GameViewModel(gameEngine, contentRepository)
        testDispatcher.scheduler.advanceUntilIdle()

        viewModel.selectChoice("c-1")
        testDispatcher.scheduler.advanceUntilIdle()

        assertEquals(1, gameEngine.selectChoiceCallCount)
        assertEquals("c-1", gameEngine.lastSelectedChoiceId)
    }

    @Test
    fun `test timeline collapse result translates to TriggerTimelineCollapse effect`() = runBlocking {
        val collapsedState = gameEngine.state.value.copy(
            variables = GameVariables(tyrantIndex = 20, timelineStability = 0, selfTrust = 30)
        )
        gameEngine.simulatedResult = GameEngineResult.TimelineCollapsed(collapsedState)

        val viewModel = GameViewModel(gameEngine, contentRepository)
        testDispatcher.scheduler.advanceUntilIdle()

        var receivedEffect: GameUiEffect? = null
        val job = launch {
            receivedEffect = viewModel.effects.first()
        }

        viewModel.selectChoice("c-1")
        testDispatcher.scheduler.advanceUntilIdle()

        assertNotNull(receivedEffect)
        assertTrue(receivedEffect is GameUiEffect.TriggerTimelineCollapse)
        assertEquals(0, (receivedEffect as GameUiEffect.TriggerTimelineCollapse).stability)
        job.cancel()
    }

    @Test
    fun `test ending reached result translates to NavigateToEnding effect`() = runBlocking {
        val endingState = gameEngine.state.value.copy(
            ending = EndingState(eligibleEnding = "THE_SACRIFICE", isResolved = true)
        )
        gameEngine.simulatedResult = GameEngineResult.EndingReached("THE_SACRIFICE", endingState)

        val viewModel = GameViewModel(gameEngine, contentRepository)
        testDispatcher.scheduler.advanceUntilIdle()

        var receivedEffect: GameUiEffect? = null
        val job = launch {
            receivedEffect = viewModel.effects.first()
        }

        viewModel.selectChoice("c-1")
        testDispatcher.scheduler.advanceUntilIdle()

        assertNotNull(receivedEffect)
        assertTrue(receivedEffect is GameUiEffect.NavigateToEnding)
        assertEquals("THE_SACRIFICE", (receivedEffect as GameUiEffect.NavigateToEnding).endingId)
        job.cancel()
    }

    @Test
    fun `test unavailable choice rejection translates to ShowMessage effect`() = runBlocking {
        gameEngine.simulatedResult = GameEngineResult.ChoiceUnavailable("Requires higher stability")

        val viewModel = GameViewModel(gameEngine, contentRepository)
        testDispatcher.scheduler.advanceUntilIdle()

        var receivedEffect: GameUiEffect? = null
        val job = launch {
            receivedEffect = viewModel.effects.first()
        }

        viewModel.selectChoice("c-2")
        testDispatcher.scheduler.advanceUntilIdle()

        assertNotNull(receivedEffect)
        assertTrue(receivedEffect is GameUiEffect.ShowMessage)
        assertEquals("Requires higher stability", (receivedEffect as GameUiEffect.ShowMessage).message)
        job.cancel()
    }

    @Test
    fun `test dismissError clears active error message from UI projection`() = runBlocking {
        val viewModel = GameViewModel(gameEngine, contentRepository)
        testDispatcher.scheduler.advanceUntilIdle()

        // Transition engine to an invalid scene to induce an error
        gameEngine.updateState(gameEngine.state.value.copy(currentSceneId = "non-existent-scene"))
        testDispatcher.scheduler.advanceUntilIdle()

        assertNotNull(viewModel.uiState.value.errorMessage)

        viewModel.dismissError()
        testDispatcher.scheduler.advanceUntilIdle()

        assertNull(viewModel.uiState.value.errorMessage)
    }

    @Test
    fun `test choice with consequence projects activeConsequence and acknowledging clears it`() = runBlocking {
        val choiceWithConsequence = Choice(
            id = "c-consequence",
            text = "Sacrifice stability",
            targetSceneId = "scene-02",
            consequence = ChoiceConsequence(
                timelineStabilityDelta = -5,
                selfTrustDelta = 2
            )
        )
        contentRepository.scenes["scene-01"] = contentRepository.scenes["scene-01"]!!.copy(
            choices = listOf(choiceWithConsequence)
        )
        gameEngine.availableChoiceIds.add("c-consequence")

        val viewModel = GameViewModel(gameEngine, contentRepository)
        testDispatcher.scheduler.advanceUntilIdle()

        viewModel.selectChoice("c-consequence")
        testDispatcher.scheduler.advanceUntilIdle()

        val consequence = viewModel.uiState.value.activeConsequence
        assertNotNull(consequence)
        assertEquals("Sacrifice stability", consequence?.choiceText)
        assertEquals(2, consequence?.statDeltas?.size)

        viewModel.acknowledgeConsequence()
        testDispatcher.scheduler.advanceUntilIdle()

        assertNull(viewModel.uiState.value.activeConsequence)
        assertNotNull(viewModel.uiState.value.activeTimelineChange)

        viewModel.acknowledgeTimelineChange()
        testDispatcher.scheduler.advanceUntilIdle()

        assertNull(viewModel.uiState.value.activeTimelineChange)
    }

    @Test
    fun `test choice without consequence immediately projects activeTimelineChange and acknowledging clears it`() = runBlocking {
        val viewModel = GameViewModel(gameEngine, contentRepository)
        testDispatcher.scheduler.advanceUntilIdle()

        // c-1 has consequence = null
        viewModel.selectChoice("c-1")
        testDispatcher.scheduler.advanceUntilIdle()

        assertNull(viewModel.uiState.value.activeConsequence)
        val timelineChange = viewModel.uiState.value.activeTimelineChange
        assertNotNull(timelineChange)
        assertEquals("c-1", timelineChange?.recordedDecisionChoiceId)

        viewModel.acknowledgeTimelineChange()
        testDispatcher.scheduler.advanceUntilIdle()

        assertNull(viewModel.uiState.value.activeTimelineChange)
    }
}
