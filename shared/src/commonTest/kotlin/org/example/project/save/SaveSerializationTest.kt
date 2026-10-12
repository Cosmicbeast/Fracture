package org.example.project.save

import org.example.project.core.GameState
import org.example.project.core.enums.Perspective
import org.example.project.core.enums.ToolType
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertNotNull

class SaveSerializationTest {

    @Test
    fun testGameStateSerializationRoundTrip() {
        val original = GameState(
            currentActNumber = 1,
            currentPerspective = Perspective.FUTURE_SELF,
            currentSceneId = "scene_01_chamber",
            tyrantIndex = 45,
            timelineStability = 70,
            selfTrust = 60,
            activeTools = setOf(ToolType.CHRONO_BLADE, ToolType.AION_COMPASS),
            flags = mapOf("containment_sealed" to true),
            decisionHistory = listOf("c_01_examine_blade")
        )

        val json = SaveSerializer.serializeState(original)
        val deserialized = SaveSerializer.deserializeState(json)

        assertNotNull(deserialized)
        assertEquals(original, deserialized)
    }

    @Test
    fun testSaveSlotSerializationRoundTrip() {
        val state = GameState(
            currentActNumber = 1,
            currentPerspective = Perspective.FUTURE_SELF,
            currentSceneId = "scene_01_sky"
        )

        val originalSlot = SaveSlot(
            slotId = "slot_1",
            timestampMillis = 1000L,
            actNumber = 1,
            sceneTitle = "The Sky That Never Heals",
            stabilityPercentage = 85,
            tyrantIndex = 20,
            state = state
        )

        val json = SaveSerializer.serializeSlot(originalSlot)
        val deserializedSlot = SaveSerializer.deserializeSlot(json)

        assertNotNull(deserializedSlot)
        assertEquals(originalSlot, deserializedSlot)
    }
}
