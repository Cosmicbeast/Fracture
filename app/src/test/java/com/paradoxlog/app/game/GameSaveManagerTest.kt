package com.paradoxlog.app.game

import com.paradoxlog.app.domain.model.GameFlag
import com.paradoxlog.app.game.save.JsonGameSaveManager
import com.paradoxlog.app.game.save.SaveStateException
import com.paradoxlog.app.game.state.EndingState
import com.paradoxlog.app.game.state.GameState
import com.paradoxlog.app.game.state.GameVariables
import com.paradoxlog.app.game.state.InventoryState
import com.paradoxlog.app.game.state.TimelineProgress
import org.junit.Assert.assertEquals
import org.junit.Assert.assertNotNull
import org.junit.Assert.assertTrue
import org.junit.Test

class GameSaveManagerTest {

    private val saveManager = JsonGameSaveManager()

    @Test
    fun testSerializationAndDeserializationRoundtrip() {
        val original = GameState(
            version = 1,
            currentActId = "act-01",
            currentSceneId = "scene-01-02",
            variables = GameVariables(tyrantIndex = 15, timelineStability = 80, selfTrust = 65),
            flags = setOf(GameFlag("terminal_accessed"), GameFlag("quarantine_enforced")),
            inventory = InventoryState(itemIds = setOf("chrono_blade")),
            timeline = TimelineProgress(
                actsCompleted = listOf("act-00"),
                visitedScenes = setOf("scene-01-01", "scene-01-02"),
                decisionsMade = mapOf("scene-01-01" to "choice-01-a")
            ),
            ending = EndingState(
                eligibleEnding = "THE_TYRANT",
                isResolved = false,
                endingSceneId = null
            )
        )

        val json = saveManager.serialize(original)
        assertNotNull(json)
        assertTrue(json.contains("\"version\": 1"))
        assertTrue(json.contains("\"tyrantIndex\": 15"))
        assertTrue(json.contains("\"terminal_accessed\""))

        val restored = saveManager.deserialize(json)
        assertEquals(original.version, restored.version)
        assertEquals(original.currentActId, restored.currentActId)
        assertEquals(original.currentSceneId, restored.currentSceneId)
        assertEquals(original.variables, restored.variables)
        assertEquals(original.flags, restored.flags)
        assertEquals(original.inventory, restored.inventory)
        assertEquals(original.timeline, restored.timeline)
        assertEquals(original.ending, restored.ending)
    }

    @Test(expected = SaveStateException::class)
    fun testCorruptJsonThrowsSaveStateException() {
        saveManager.deserialize("{ invalid json syntax }")
    }

    @Test(expected = SaveStateException::class)
    fun testMissingRequiredPropertiesThrowsSaveStateException() {
        // Missing variables and inventory objects
        saveManager.deserialize("{\"version\": 1, \"currentActId\": \"act-01\"}")
    }
}
