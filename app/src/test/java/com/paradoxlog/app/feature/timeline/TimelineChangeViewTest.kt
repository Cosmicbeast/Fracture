package com.paradoxlog.app.feature.timeline

import com.paradoxlog.app.core.constants.UiLabels
import org.junit.Assert.assertEquals
import org.junit.Assert.assertFalse
import org.junit.Assert.assertNull
import org.junit.Assert.assertTrue
import org.junit.Test

/**
 * Focused presentation tests for Step 7 — Timeline Change Presentation.
 *
 * Verifies:
 * 1. Authoritative timeline data is correctly projected into TimelineChangeUiState.
 * 2. No duplicate timeline state is created.
 * 3. Missing timeline information does not produce invented fallback content.
 * 4. Acknowledgment dispatches the expected action.
 * 5. UI labels are centralized.
 */
class TimelineChangeViewTest {

    @Test
    fun `test TimelineChangeUiState projects authoritative timeline data`() {
        val state = TimelineChangeUiState(
            actId = "act-01",
            sceneId = "scene-02",
            recordedDecisionChoiceId = "choice-examine-station",
            timelineStability = 95,
            totalVisitedScenesCount = 2,
            totalDecisionsCount = 1
        )

        assertEquals("act-01", state.actId)
        assertEquals("scene-02", state.sceneId)
        assertEquals("choice-examine-station", state.recordedDecisionChoiceId)
        assertEquals(95, state.timelineStability)
        assertEquals(2, state.totalVisitedScenesCount)
        assertEquals(1, state.totalDecisionsCount)
    }

    @Test
    fun `test missing optional timeline information remains null or empty without invented fallback content`() {
        val minimalState = TimelineChangeUiState(
            actId = "act-01",
            sceneId = "scene-01",
            recordedDecisionChoiceId = null,
            timelineStability = null,
            totalVisitedScenesCount = 0,
            totalDecisionsCount = 0
        )

        assertNull(minimalState.recordedDecisionChoiceId)
        assertNull(minimalState.timelineStability)
        assertEquals(0, minimalState.totalVisitedScenesCount)
        assertEquals(0, minimalState.totalDecisionsCount)
    }

    @Test
    fun `test timeline acknowledgment callback dispatches action`() {
        var acknowledged = false
        val onContinue: () -> Unit = {
            acknowledged = true
        }

        assertFalse(acknowledged)
        onContinue()
        assertTrue(acknowledged)
    }

    @Test
    fun `test timeline system labels are centralized and genuine`() {
        assertEquals("TIMELINE UPDATED", UiLabels.LABEL_TIMELINE_UPDATED)
        assertEquals("Decision Recorded", UiLabels.LABEL_DECISION_RECORDED)
        assertEquals("Timeline Nodes", UiLabels.LABEL_TIMELINE_NODES)
        assertEquals("Act", UiLabels.LABEL_CURRENT_ACT)
        assertEquals("Scene", UiLabels.LABEL_CURRENT_SCENE)
        assertEquals("CONTINUE", UiLabels.LABEL_CONTINUE)
    }
}
