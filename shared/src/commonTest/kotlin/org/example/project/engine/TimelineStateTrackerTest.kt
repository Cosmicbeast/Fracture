package org.example.project.engine

import org.example.project.core.GameState
import kotlin.test.Test
import kotlin.test.assertEquals

class TimelineStateTrackerTest {

    @Test
    fun testStabilityThresholds() {
        val pristineState = GameState(timelineStability = 95)
        val stableState = GameState(timelineStability = 70)
        val strainedState = GameState(timelineStability = 50)
        val decayingState = GameState(timelineStability = 30)
        val criticalState = GameState(timelineStability = 15)

        assertEquals(TimelineStateTracker.StabilityStatus.PRISTINE, TimelineStateTracker.getStabilityStatus(pristineState))
        assertEquals(TimelineStateTracker.StabilityStatus.STABLE, TimelineStateTracker.getStabilityStatus(stableState))
        assertEquals(TimelineStateTracker.StabilityStatus.STRAINED, TimelineStateTracker.getStabilityStatus(strainedState))
        assertEquals(TimelineStateTracker.StabilityStatus.DECAYING, TimelineStateTracker.getStabilityStatus(decayingState))
        assertEquals(TimelineStateTracker.StabilityStatus.CRITICAL, TimelineStateTracker.getStabilityStatus(criticalState))
    }

    @Test
    fun testTyrantPostures() {
        val reluctant = GameState(tyrantIndex = 15)
        val tyrant = GameState(tyrantIndex = 85)

        assertEquals(TimelineStateTracker.TyrantPosture.RELUCTANT, TimelineStateTracker.getTyrantPosture(reluctant))
        assertEquals(TimelineStateTracker.TyrantPosture.TYRANT, TimelineStateTracker.getTyrantPosture(tyrant))
    }
}
