package com.paradoxlog.app.game

import com.paradoxlog.app.domain.model.ChoiceConsequence
import com.paradoxlog.app.domain.model.GameFlag
import com.paradoxlog.app.game.consequences.DefaultConsequenceApplier
import com.paradoxlog.app.game.state.EndingState
import com.paradoxlog.app.game.state.GameState
import com.paradoxlog.app.game.state.GameVariables
import com.paradoxlog.app.game.state.InventoryState
import org.junit.Assert.assertEquals
import org.junit.Assert.assertFalse
import org.junit.Assert.assertNull
import org.junit.Assert.assertTrue
import org.junit.Test

class ConsequenceApplierTest {

    private val applier = DefaultConsequenceApplier()

    @Test
    fun testNullConsequenceReturnsSameState() {
        val original = GameState()
        val result = applier.applyConsequence(null, original)
        assertEquals(original, result)
    }

    @Test
    fun testVariableDeltasAndClamping() {
        val initial = GameState(
            variables = GameVariables(tyrantIndex = 5, timelineStability = 50, selfTrust = 50)
        )

        val consequence = ChoiceConsequence(
            tyrantIndexDelta = 10,
            timelineStabilityDelta = -20,
            selfTrustDelta = 15
        )

        val updated = applier.applyConsequence(consequence, initial)
        assertEquals(15, updated.tyrantIndex)
        assertEquals(30, updated.timelineStability)
        assertEquals(65, updated.selfTrust)

        // Verify boundary clamping (min tyrant 0, stability max 100, selfTrust 0..100)
        val extremeConsequence = ChoiceConsequence(
            tyrantIndexDelta = -50,
            timelineStabilityDelta = 200,
            selfTrustDelta = -100
        )
        val clamped = applier.applyConsequence(extremeConsequence, updated)
        assertEquals(0, clamped.tyrantIndex)
        assertEquals(100, clamped.timelineStability)
        assertEquals(0, clamped.selfTrust)
    }

    @Test
    fun testFlagUpdates() {
        val initial = GameState(
            flags = setOf(GameFlag("old_flag"), GameFlag("to_clear"))
        )

        val consequence = ChoiceConsequence(
            setFlags = setOf(GameFlag("new_flag")),
            clearFlags = setOf(GameFlag("to_clear"))
        )

        val updated = applier.applyConsequence(consequence, initial)
        assertTrue(updated.hasFlag("old_flag"))
        assertTrue(updated.hasFlag("new_flag"))
        assertFalse(updated.hasFlag("to_clear"))
    }

    @Test
    fun testInventoryUpdates() {
        val initial = GameState(
            inventory = InventoryState(itemIds = setOf("temporary_tool"))
        )

        val consequence = ChoiceConsequence(
            addedItems = setOf("chrono_blade"),
            removedItems = setOf("temporary_tool")
        )

        val updated = applier.applyConsequence(consequence, initial)
        assertTrue(updated.hasItem("chrono_blade"))
        assertFalse(updated.hasItem("temporary_tool"))
    }

    @Test
    fun testStabilityReachingZeroDoesNotAutomaticallySelectEnding() {
        // Specifically tests requirement 1: Stability reaching 0 does NOT hardcode an ending into EndingState
        val initial = GameState(
            variables = GameVariables(timelineStability = 10),
            ending = EndingState(eligibleEnding = null, isResolved = false)
        )

        val severeConsequence = ChoiceConsequence(
            timelineStabilityDelta = -25
        )

        val updated = applier.applyConsequence(severeConsequence, initial)
        assertEquals(0, updated.timelineStability)
        // EndingState must remain unmodified by ConsequenceApplier
        assertFalse(updated.ending.isResolved)
        assertNull(updated.ending.eligibleEnding)
    }

    @Test
    fun testEndingEvaluationIsSeparateFromConsequenceApplication() {
        // ConsequenceApplier must preserve existing ending state without evaluation
        val initialWithCustomEnding = GameState(
            ending = EndingState(eligibleEnding = "CUSTOM_ENDING", isResolved = true)
        )
        val consequence = ChoiceConsequence(tyrantIndexDelta = 5)

        val updated = applier.applyConsequence(consequence, initialWithCustomEnding)
        assertEquals(initialWithCustomEnding.ending, updated.ending)
    }

    @Test
    fun testOriginalStateRemainsUnmodified() {
        val initial = GameState(
            variables = GameVariables(tyrantIndex = 0),
            flags = setOf(GameFlag("initial_flag")),
            inventory = InventoryState(itemIds = setOf("initial_item"))
        )
        val consequence = ChoiceConsequence(
            tyrantIndexDelta = 5,
            setFlags = setOf(GameFlag("added_flag")),
            addedItems = setOf("added_item")
        )

        val updated = applier.applyConsequence(consequence, initial)

        // Initial state must be completely untouched
        assertEquals(0, initial.tyrantIndex)
        assertEquals(setOf(GameFlag("initial_flag")), initial.flags)
        assertEquals(setOf("initial_item"), initial.inventory.itemIds)

        // Updated state has new values
        assertEquals(5, updated.tyrantIndex)
        assertTrue(updated.hasFlag("added_flag"))
        assertTrue(updated.hasItem("added_item"))
    }
}
