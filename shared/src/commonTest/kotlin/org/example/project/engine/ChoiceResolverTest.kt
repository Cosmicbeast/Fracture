package org.example.project.engine

import org.example.project.core.Choice
import org.example.project.core.GameState
import org.example.project.core.enums.ToolType
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertFalse
import kotlin.test.assertTrue

class ChoiceResolverTest {

    @Test
    fun testChoiceResolutionAppliesStatDeltasWithClamping() {
        val initial = GameState(
            tyrantIndex = 95,
            timelineStability = 10,
            selfTrust = 50
        )

        val choice = Choice(
            id = "c_test",
            text = "Test choice",
            targetSceneId = "scene_next",
            tyrantIndexDelta = 10,  // Should clamp to 100
            stabilityDelta = -15,   // Should clamp to 0
            selfTrustDelta = 10
        )

        val result = ChoiceResolver.resolve(choice, initial)

        assertEquals(100, result.tyrantIndex)
        assertEquals(0, result.timelineStability)
        assertEquals(60, result.selfTrust)
        assertEquals("scene_next", result.currentSceneId)
        assertTrue(result.decisionHistory.contains("c_test"))
    }

    @Test
    fun testToolPrerequisiteFiltering() {
        val stateWithoutGauntlet = GameState(activeTools = setOf(ToolType.CHRONO_BLADE))
        val stateWithGauntlet = GameState(activeTools = setOf(ToolType.CHRONO_BLADE, ToolType.PARADOX_GAUNTLET))

        val gauntletChoice = Choice(
            id = "c_gauntlet",
            text = "Use Gauntlet",
            targetSceneId = "scene_next",
            requiredTool = ToolType.PARADOX_GAUNTLET
        )

        assertFalse(ChoiceResolver.isChoiceAvailable(gauntletChoice, stateWithoutGauntlet))
        assertTrue(ChoiceResolver.isChoiceAvailable(gauntletChoice, stateWithGauntlet))
    }

    @Test
    fun testForesightPreviewWhenChronoBladeEquipped() {
        val stateWithBlade = GameState(activeTools = setOf(ToolType.CHRONO_BLADE))
        val stateWithoutBlade = GameState(activeTools = emptySet())

        val choice = Choice(
            id = "c_foresight",
            text = "Examine",
            targetSceneId = "scene_next",
            foresightHint = "Catastrophic breach in sector 3"
        )

        assertEquals("Catastrophic breach in sector 3", ChoiceResolver.getForesightPreview(choice, stateWithBlade))
        assertEquals(null, ChoiceResolver.getForesightPreview(choice, stateWithoutBlade))
    }
}
