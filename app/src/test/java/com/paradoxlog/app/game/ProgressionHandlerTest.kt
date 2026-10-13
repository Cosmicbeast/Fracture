package com.paradoxlog.app.game

import com.paradoxlog.app.domain.model.Choice
import com.paradoxlog.app.game.progression.DefaultProgressionHandler
import com.paradoxlog.app.game.state.GameState
import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Test

class ProgressionHandlerTest {

    private val handler = DefaultProgressionHandler()

    @Test
    fun testTransitionSceneUpdatesVisitedAndDecisions() {
        val initial = GameState(
            currentActId = "act-01",
            currentSceneId = "scene-01"
        )
        val choice = Choice(id = "choice-a", text = "Go to scene 2", targetSceneId = "scene-02")

        val updated = handler.transitionScene(initial.currentSceneId, choice, initial)

        assertEquals("scene-02", updated.currentSceneId)
        assertTrue(updated.timeline.visitedScenes.contains("scene-01"))
        assertTrue(updated.timeline.visitedScenes.contains("scene-02"))
        assertEquals("choice-a", updated.timeline.decisionsMade["scene-01"])
    }

    @Test
    fun testBranchingAndConvergenceSupport() {
        // Path 1: scene-01 -> choice-b -> scene-03
        val initial1 = GameState(currentActId = "act-01", currentSceneId = "scene-01")
        val choice1 = Choice(id = "choice-b", text = "Branch 1", targetSceneId = "scene-03")
        val stateAfterBranch1 = handler.transitionScene("scene-01", choice1, initial1)

        // Path 2: from scene-02 -> choice-c -> scene-03 (Convergence to scene-03)
        val choice2 = Choice(id = "choice-c", text = "Branch 2", targetSceneId = "scene-03")
        val stateAfterConvergence = handler.transitionScene("scene-02", choice2, stateAfterBranch1)

        assertEquals("scene-03", stateAfterConvergence.currentSceneId)
        assertTrue(stateAfterConvergence.timeline.visitedScenes.contains("scene-01"))
        assertTrue(stateAfterConvergence.timeline.visitedScenes.contains("scene-02"))
        assertTrue(stateAfterConvergence.timeline.visitedScenes.contains("scene-03"))
        assertEquals("choice-b", stateAfterConvergence.timeline.decisionsMade["scene-01"])
        assertEquals("choice-c", stateAfterConvergence.timeline.decisionsMade["scene-02"])
    }

    @Test
    fun testTransitionActUpdatesCompletedActsAndInitialScene() {
        val initial = GameState(
            currentActId = "act-01",
            currentSceneId = "scene-01-final"
        )

        val updated = handler.transitionAct(
            completedActId = "act-01",
            nextActId = "act-02",
            nextActInitialSceneId = "scene-02-01",
            state = initial
        )

        assertEquals("act-02", updated.currentActId)
        assertEquals("scene-02-01", updated.currentSceneId)
        assertEquals(listOf("act-01"), updated.timeline.actsCompleted)
        assertTrue(updated.timeline.visitedScenes.contains("scene-02-01"))
    }
}
