package com.paradoxlog.app.game

import com.paradoxlog.app.domain.model.Act
import com.paradoxlog.app.domain.model.Choice
import com.paradoxlog.app.domain.model.ChoiceCondition
import com.paradoxlog.app.domain.model.ChoiceConsequence
import com.paradoxlog.app.domain.model.GameFlag
import com.paradoxlog.app.domain.model.Item
import com.paradoxlog.app.domain.model.Scene
import com.paradoxlog.app.domain.repository.ContentRepository
import com.paradoxlog.app.game.endings.EndingEvaluator
import com.paradoxlog.app.game.engine.DefaultGameEngine
import com.paradoxlog.app.game.engine.GameEngineResult
import com.paradoxlog.app.game.state.EndingState
import com.paradoxlog.app.game.state.GameState
import com.paradoxlog.app.game.state.GameVariables
import kotlinx.coroutines.runBlocking
import org.junit.Assert.assertEquals
import org.junit.Assert.assertFalse
import org.junit.Assert.assertNull
import org.junit.Assert.assertTrue
import org.junit.Test

class GameEngineTest {

    private class FakeContentRepository : ContentRepository {
        val acts = mutableMapOf<String, Act>()
        val scenes = mutableMapOf<String, Scene>()

        override suspend fun getAct(actId: String): Act? = acts[actId]
        override suspend fun getScenesForAct(actId: String): List<Scene> = scenes.values.filter { it.actId == actId }
        override suspend fun getScene(actId: String, sceneId: String): Scene? = scenes[sceneId]
        override suspend fun getItems(): List<Item> = emptyList()
    }

    private fun setupRepository(): FakeContentRepository {
        val repo = FakeContentRepository()

        repo.acts["act-01"] = Act(
            id = "act-01",
            actNumber = 1,
            title = "Act 1: The Breach",
            initialSceneId = "scene-01-01",
            sceneIds = listOf("scene-01-01", "scene-01-02", "scene-01-03")
        )

        repo.acts["act-02"] = Act(
            id = "act-02",
            actNumber = 2,
            title = "Act 2: The Remnant",
            initialSceneId = "scene-02-01",
            sceneIds = listOf("scene-02-01")
        )

        val choiceAvailable = Choice(
            id = "c1",
            text = "Proceed safely",
            targetSceneId = "scene-01-02",
            consequence = ChoiceConsequence(tyrantIndexDelta = 5, setFlags = setOf(GameFlag("safe_path")))
        )

        val choiceLocked = Choice(
            id = "c2",
            text = "Override core (Locked)",
            targetSceneId = "scene-01-02",
            condition = ChoiceCondition(requiredFlags = setOf(GameFlag("core_key")))
        )

        val choiceFatal = Choice(
            id = "c3_fatal",
            text = "Drain stability completely",
            targetSceneId = "scene-01-02",
            consequence = ChoiceConsequence(timelineStabilityDelta = -150)
        )

        val choiceActTransition = Choice(
            id = "c4_transition",
            text = "Enter temporal bridge",
            targetSceneId = "scene-02-01",
            consequence = ChoiceConsequence(nextActId = "act-02")
        )

        val choiceBranchA = Choice(
            id = "c_branch_a",
            text = "Take route A to convergence",
            targetSceneId = "scene-01-03"
        )

        repo.scenes["scene-01-01"] = Scene(
            id = "scene-01-01",
            actId = "act-01",
            title = "Observation Room",
            choices = listOf(choiceAvailable, choiceLocked, choiceFatal, choiceActTransition, choiceBranchA)
        )

        repo.scenes["scene-01-02"] = Scene(
            id = "scene-01-02",
            actId = "act-01",
            title = "Corridor",
            choices = listOf(
                Choice(id = "c_branch_b", text = "Take route B to convergence", targetSceneId = "scene-01-03")
            )
        )

        repo.scenes["scene-01-03"] = Scene(
            id = "scene-01-03",
            actId = "act-01",
            title = "Convergence Point"
        )

        repo.scenes["scene-02-01"] = Scene(
            id = "scene-02-01",
            actId = "act-02",
            title = "The Citadel Gates"
        )

        return repo
    }

    @Test
    fun testStartNewGameInitializesState() = runBlocking {
        val repo = setupRepository()
        val engine = DefaultGameEngine(repo)

        val result = engine.startNewGame("act-01")
        assertTrue(result is GameEngineResult.Success)

        val state = engine.state.value
        assertEquals("act-01", state.currentActId)
        assertEquals("scene-01-01", state.currentSceneId)
        assertTrue(state.timeline.visitedScenes.contains("scene-01-01"))
    }

    @Test
    fun testGetAvailableChoicesFiltersLocked() = runBlocking {
        val repo = setupRepository()
        val engine = DefaultGameEngine(repo)
        engine.startNewGame("act-01")

        val scene = repo.scenes["scene-01-01"]!!
        val available = engine.getAvailableChoicesForScene(scene)

        // c2 is locked because core_key flag is missing
        assertTrue(available.any { it.id == "c1" })
        assertTrue(available.none { it.id == "c2" })
    }

    @Test
    fun testSelectChoiceExecutesConsequencesAndProducesExactlyOneStateTransition() = runBlocking {
        val repo = setupRepository()
        val engine = DefaultGameEngine(repo)
        engine.startNewGame("act-01")

        val scene = repo.scenes["scene-01-01"]!!
        val result = engine.selectChoice(scene, "c1")

        assertTrue(result is GameEngineResult.Success)
        val state = engine.state.value
        assertEquals("scene-01-02", state.currentSceneId)
        assertEquals(5, state.tyrantIndex)
        assertTrue(state.hasFlag("safe_path"))
        assertEquals("c1", state.timeline.decisionsMade["scene-01-01"])
    }

    @Test
    fun testUnavailableChoiceCannotModifyState() = runBlocking {
        val repo = setupRepository()
        val engine = DefaultGameEngine(repo)
        engine.startNewGame("act-01")

        val stateBefore = engine.state.value
        val scene = repo.scenes["scene-01-01"]!!
        val result = engine.selectChoice(scene, "c2")

        assertTrue(result is GameEngineResult.ChoiceUnavailable)
        // Verify state remains strictly identical
        assertEquals(stateBefore, engine.state.value)
    }

    @Test
    fun testStabilityReachingZeroProducesTimelineCollapsedEventWithoutForcingNarrativeEnding() = runBlocking {
        val repo = setupRepository()
        val engine = DefaultGameEngine(repo)
        engine.startNewGame("act-01")

        val scene = repo.scenes["scene-01-01"]!!
        val result = engine.selectChoice(scene, "c3_fatal")

        // Must produce TimelineCollapsed event
        assertTrue(result is GameEngineResult.TimelineCollapsed)
        val state = engine.state.value
        assertEquals(0, state.timelineStability)
        // Must NOT assume or force an ending into EndingState
        assertFalse(state.ending.isResolved)
        assertNull(state.ending.eligibleEnding)
    }

    @Test
    fun testBranchingAndConvergencePreservesSingleStateOwnership() = runBlocking {
        val repo = setupRepository()
        val engine = DefaultGameEngine(repo)
        engine.startNewGame("act-01")

        // Branch 1: select c1 (scene 1 -> scene 2)
        val scene1 = repo.scenes["scene-01-01"]!!
        engine.selectChoice(scene1, "c1")
        assertEquals("scene-01-02", engine.state.value.currentSceneId)

        // Converge: select c_branch_b (scene 2 -> scene 3)
        val scene2 = repo.scenes["scene-01-02"]!!
        val convergeResult = engine.selectChoice(scene2, "c_branch_b")
        assertTrue(convergeResult is GameEngineResult.Success)

        val finalState = engine.state.value
        assertEquals("scene-01-03", finalState.currentSceneId)
        assertTrue(finalState.timeline.visitedScenes.contains("scene-01-01"))
        assertTrue(finalState.timeline.visitedScenes.contains("scene-01-02"))
        assertTrue(finalState.timeline.visitedScenes.contains("scene-01-03"))
        assertEquals("c1", finalState.timeline.decisionsMade["scene-01-01"])
        assertEquals("c_branch_b", finalState.timeline.decisionsMade["scene-01-02"])
    }

    @Test
    fun testChoiceWithNextActIdTransitionsAct() = runBlocking {
        val repo = setupRepository()
        val engine = DefaultGameEngine(repo)
        engine.startNewGame("act-01")

        val scene = repo.scenes["scene-01-01"]!!
        val result = engine.selectChoice(scene, "c4_transition")

        assertTrue(result is GameEngineResult.Success)
        val state = engine.state.value
        assertEquals("act-02", state.currentActId)
        assertEquals("scene-02-01", state.currentSceneId)
        assertEquals(listOf("act-01"), state.timeline.actsCompleted)
    }

    @Test
    fun testManualAdvanceAct() = runBlocking {
        val repo = setupRepository()
        val engine = DefaultGameEngine(repo)
        engine.startNewGame("act-01")

        val result = engine.advanceAct("act-02")
        assertTrue(result is GameEngineResult.Success)

        val state = engine.state.value
        assertEquals("act-02", state.currentActId)
        assertEquals("scene-02-01", state.currentSceneId)
        assertEquals(listOf("act-01"), state.timeline.actsCompleted)
    }

    @Test
    fun testLoadGameStateUpdatesStateFlow() {
        val repo = setupRepository()
        val engine = DefaultGameEngine(repo)

        val customState = GameState(
            currentActId = "act-02",
            currentSceneId = "scene-02-01",
            variables = GameVariables(tyrantIndex = 42)
        )
        engine.loadGameState(customState)

        assertEquals(customState, engine.state.value)
    }

    @Test
    fun testEndingReachedWhenDedicatedEndingEvaluatorResolvesEnding() = runBlocking {
        val repo = setupRepository()
        val customEndingEvaluator = object : EndingEvaluator {
            override fun evaluate(state: GameState): EndingState {
                return if (state.hasFlag("trigger_reconciliation")) {
                    EndingState(
                        eligibleEnding = "THE_RECONCILIATION",
                        isResolved = true,
                        endingSceneId = state.currentSceneId
                    )
                } else state.ending
            }
            override fun isEndingReached(state: GameState): Boolean = evaluate(state).isResolved
        }

        val engine = DefaultGameEngine(
            contentRepository = repo,
            endingEvaluator = customEndingEvaluator
        )
        engine.startNewGame("act-01")

        val endingChoice = Choice(
            id = "c_ending",
            text = "Reconcile timeline",
            targetSceneId = "scene-01-02",
            consequence = ChoiceConsequence(setFlags = setOf(GameFlag("trigger_reconciliation")))
        )
        val sceneWithEnding = Scene(
            id = "scene-01-01",
            actId = "act-01",
            choices = listOf(endingChoice)
        )

        val result = engine.selectChoice(sceneWithEnding, "c_ending")
        assertTrue(result is GameEngineResult.EndingReached)
        assertEquals("THE_RECONCILIATION", (result as GameEngineResult.EndingReached).endingId)
        assertTrue(result.finalState.ending.isResolved)
    }
}
