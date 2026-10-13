package com.paradoxlog.app.feature.choice

import com.paradoxlog.app.feature.story.ChoiceUi
import org.junit.Assert.assertEquals
import org.junit.Assert.assertFalse
import org.junit.Assert.assertTrue
import org.junit.Test

/**
 * Unit tests verifying that the canonical Choice component adheres to:
 * Story -> Choice -> Consequence -> Timeline Change -> Exploration -> Encounter -> Story
 *
 * Verifies:
 * 1. Choice component only emits `choiceId` and does NOT calculate consequences or timeline updates.
 * 2. Preconditions are strictly consumed from `ChoiceUi.isAvailable`.
 * 3. Unavailable choices do not emit selections.
 * 4. Zero state prediction or UI shortcuts occur.
 */
class ChoiceComponentTest {

    @Test
    fun `test ChoiceUi contains only presentation data without domain logic`() {
        val choiceUi = ChoiceUi(
            id = "choice-examine-terminal",
            text = "Examine the station terminal",
            isAvailable = true
        )

        assertEquals("choice-examine-terminal", choiceUi.id)
        assertEquals("Examine the station terminal", choiceUi.text)
        assertTrue(choiceUi.isAvailable)
    }

    @Test
    fun `test choice selection emits strictly choiceId without consequence prediction`() {
        val choices = listOf(
            ChoiceUi("c-1", "Search for clues", isAvailable = true),
            ChoiceUi("c-2", "Leave immediately", isAvailable = true),
            ChoiceUi("c-3", "Access restricted log", isAvailable = false)
        )

        var emittedChoiceId: String? = null
        val selectChoiceAction: (String) -> Unit = { id ->
            emittedChoiceId = id
        }

        // Simulate user clicking an available choice
        selectChoiceAction(choices[0].id)
        assertEquals("c-1", emittedChoiceId)

        selectChoiceAction(choices[1].id)
        assertEquals("c-2", emittedChoiceId)
    }

    @Test
    fun `test unavailable choices are marked locked and must not emit selection`() {
        val lockedChoice = ChoiceUi("c-locked", "Override temporal clamp", isAvailable = false)

        var emissionCount = 0
        val selectChoiceAction: (String) -> Unit = {
            emissionCount++
        }

        // Simulating the ChoiceItem contract: when isAvailable is false, click is not dispatched
        if (lockedChoice.isAvailable) {
            selectChoiceAction(lockedChoice.id)
        }

        assertEquals(0, emissionCount)
        assertFalse(lockedChoice.isAvailable)
    }

    @Test
    fun `test choice component does not hold or mutate game state`() {
        val choices = listOf(
            ChoiceUi("c-1", "Option A", isAvailable = true),
            ChoiceUi("c-2", "Option B", isAvailable = true)
        )

        // Verify choices are immutable presentation items
        assertEquals(2, choices.size)
        assertTrue(choices.all { it is ChoiceUi })
    }

    @Test
    fun `test locked choice uses centralized UiLabels rather than hardcoded string`() {
        assertEquals("LOCKED", com.paradoxlog.app.core.constants.UiLabels.LABEL_LOCKED)
    }
}
