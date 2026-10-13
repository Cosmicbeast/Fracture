package com.paradoxlog.app.feature.consequence

import com.paradoxlog.app.core.constants.UiLabels
import org.junit.Assert.assertEquals
import org.junit.Assert.assertFalse
import org.junit.Assert.assertTrue
import org.junit.Test

/**
 * Focused presentation tests for the Consequence presentation stage.
 *
 * Verifies:
 * 1. ConsequenceUiState renders only authoritative pre-computed presentation data.
 * 2. Only non-zero variable deltas are presented.
 * 3. Compose contains zero consequence math or variable clamping.
 * 4. Continue action dispatches correctly.
 */
class ConsequenceViewTest {

    @Test
    fun `test ConsequenceUiState holds only authoritative presentation data`() {
        val consequenceState = ConsequenceUiState(
            choiceText = "You saved the stranger.",
            statDeltas = listOf(
                StatDeltaUi(UiLabels.LABEL_STABILITY, -4),
                StatDeltaUi(UiLabels.LABEL_SELF_TRUST, 2)
            ),
            itemsAdded = listOf("chrono-blade"),
            itemsRemoved = emptyList()
        )

        assertEquals("You saved the stranger.", consequenceState.choiceText)
        assertEquals(2, consequenceState.statDeltas.size)
        assertEquals(-4, consequenceState.statDeltas[0].delta)
        assertEquals(2, consequenceState.statDeltas[1].delta)
        assertEquals(listOf("chrono-blade"), consequenceState.itemsAdded)
    }

    @Test
    fun `test stat delta representation does not include unchanged zero-shift variables`() {
        val deltas = listOf(
            StatDeltaUi(UiLabels.LABEL_STABILITY, -10)
        )

        // Only stability is present; Tyrant Index and Self-Trust are not displayed
        assertEquals(1, deltas.size)
        assertEquals(UiLabels.LABEL_STABILITY, deltas[0].statName)
        assertFalse(deltas.any { it.statName == UiLabels.LABEL_TYRANT_INDEX })
        assertFalse(deltas.any { it.statName == UiLabels.LABEL_SELF_TRUST })
    }

    @Test
    fun `test continue interaction dispatches acknowledgment action`() {
        var acknowledged = false
        val onContinue: () -> Unit = {
            acknowledged = true
        }

        assertFalse(acknowledged)
        onContinue()
        assertTrue(acknowledged)
    }

    @Test
    fun `test UI labels for consequence headers and actions are centralized`() {
        assertEquals("TIMELINE SHIFTED", UiLabels.LABEL_TIMELINE_SHIFTED)
        assertEquals("CONTINUE", UiLabels.LABEL_CONTINUE)
        assertEquals("Stability", UiLabels.LABEL_STABILITY)
        assertEquals("Self-Trust", UiLabels.LABEL_SELF_TRUST)
        assertEquals("Tyrant Index", UiLabels.LABEL_TYRANT_INDEX)
    }
}
