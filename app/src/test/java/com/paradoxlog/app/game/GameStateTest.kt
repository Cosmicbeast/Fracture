package com.paradoxlog.app.game

import com.paradoxlog.app.domain.model.GameFlag
import com.paradoxlog.app.game.state.EndingState
import com.paradoxlog.app.game.state.GameState
import com.paradoxlog.app.game.state.GameVariables
import com.paradoxlog.app.game.state.InventoryState
import com.paradoxlog.app.game.state.TimelineProgress
import org.junit.Assert.assertEquals
import org.junit.Assert.assertFalse
import org.junit.Assert.assertTrue
import org.junit.Test

class GameStateTest {

    @Test
    fun testDefaultGameStateValues() {
        val state = GameState()
        assertEquals(1, state.version)
        assertEquals("", state.currentActId)
        assertEquals("", state.currentSceneId)
        assertEquals(0, state.tyrantIndex)
        assertEquals(100, state.timelineStability)
        assertEquals(50, state.selfTrust)
        assertTrue(state.flags.isEmpty())
        assertTrue(state.inventory.itemIds.isEmpty())
        assertTrue(state.timeline.actsCompleted.isEmpty())
        assertFalse(state.ending.isResolved)
    }

    @Test
    fun testImmutableCopyUpdatesState() {
        val initial = GameState(
            version = 1,
            currentActId = "act-01",
            currentSceneId = "scene-01-01",
            variables = GameVariables(tyrantIndex = 10, timelineStability = 85, selfTrust = 60),
            flags = setOf(GameFlag("terminal_accessed")),
            inventory = InventoryState(itemIds = setOf("aion_compass"))
        )

        val updated = initial.copy(
            currentSceneId = "scene-01-02",
            variables = initial.variables.copy(tyrantIndex = 15),
            flags = initial.flags + GameFlag("anomaly_detected")
        )

        assertEquals("scene-01-01", initial.currentSceneId)
        assertEquals(10, initial.tyrantIndex)
        assertFalse(initial.hasFlag("anomaly_detected"))

        assertEquals("scene-01-02", updated.currentSceneId)
        assertEquals(15, updated.tyrantIndex)
        assertTrue(updated.hasFlag("terminal_accessed"))
        assertTrue(updated.hasFlag(GameFlag("terminal_accessed")))
        assertTrue(updated.hasFlag("anomaly_detected"))
        assertTrue(updated.hasItem("aion_compass"))
    }

    @Test
    fun testFlagAndItemAccessors() {
        val state = GameState(
            flags = setOf(GameFlag("terminal_accessed"), GameFlag("stranger_trust")),
            inventory = InventoryState(itemIds = setOf("chrono_blade"))
        )

        assertTrue(state.hasFlag("terminal_accessed"))
        assertTrue(state.hasFlag(GameFlag("stranger_trust")))
        assertFalse(state.hasFlag("terminal_destroyed"))
        assertFalse(state.hasFlag(GameFlag("terminal_destroyed")))

        assertTrue(state.hasItem("chrono_blade"))
        assertFalse(state.hasItem("singularity_core"))
    }

    @Test
    fun testTimelineProgressAndEndingState() {
        val progress = TimelineProgress(
            actsCompleted = listOf("act-01"),
            visitedScenes = setOf("scene-01-01", "scene-01-02"),
            decisionsMade = mapOf("scene-01-01" to "choice-01-a")
        )
        val ending = EndingState(
            eligibleEnding = "TIME_RESTORATION",
            isResolved = true,
            endingSceneId = "scene-ending-01"
        )
        val state = GameState(timeline = progress, ending = ending)

        assertEquals(1, state.timeline.actsCompleted.size)
        assertEquals("choice-01-a", state.timeline.decisionsMade["scene-01-01"])
        assertTrue(state.ending.isResolved)
        assertEquals("TIME_RESTORATION", state.ending.eligibleEnding)
    }
}
