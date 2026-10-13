package com.paradoxlog.app.game

import com.paradoxlog.app.game.endings.DefaultEndingEvaluator
import com.paradoxlog.app.game.endings.ParadoxEnding
import com.paradoxlog.app.game.state.EndingState
import com.paradoxlog.app.game.state.GameState
import com.paradoxlog.app.game.state.GameVariables
import org.junit.Assert.assertEquals
import org.junit.Assert.assertFalse
import org.junit.Assert.assertTrue
import org.junit.Test

class EndingEvaluatorTest {

    private val evaluator = DefaultEndingEvaluator()

    @Test
    fun testRetainsAlreadyResolvedEnding() {
        val resolvedState = GameState(
            variables = GameVariables(timelineStability = 75),
            ending = EndingState(
                eligibleEnding = ParadoxEnding.THE_RECONCILIATION.id,
                isResolved = true,
                endingSceneId = "scene-final-reconciliation"
            )
        )

        val result = evaluator.evaluate(resolvedState)
        assertTrue(result.isResolved)
        assertEquals(ParadoxEnding.THE_RECONCILIATION.id, result.eligibleEnding)
        assertEquals("scene-final-reconciliation", result.endingSceneId)
        assertTrue(evaluator.isEndingReached(resolvedState))
    }

    @Test
    fun testUnresolvedStateDuringGameplayDoesNotForceEnding() {
        val normalState = GameState(
            variables = GameVariables(timelineStability = 80, tyrantIndex = 15, selfTrust = 50)
        )

        val result = evaluator.evaluate(normalState)
        assertFalse(result.isResolved)
        assertEquals(null, result.eligibleEnding)
        assertFalse(evaluator.isEndingReached(normalState))
    }

    @Test
    fun testZeroStabilityDoesNotAutomaticallyResolveNarrativeEnding() {
        // Specifically tests that EndingEvaluator does not assume every collapse is a narrative ending
        val collapsedState = GameState(
            variables = GameVariables(timelineStability = 0),
            ending = EndingState(eligibleEnding = null, isResolved = false)
        )

        val result = evaluator.evaluate(collapsedState)
        // Narrative endings remain unhardcoded until Act VI rules calibration
        assertFalse(result.isResolved)
        assertEquals(null, result.eligibleEnding)
        assertFalse(evaluator.isEndingReached(collapsedState))
    }
}
