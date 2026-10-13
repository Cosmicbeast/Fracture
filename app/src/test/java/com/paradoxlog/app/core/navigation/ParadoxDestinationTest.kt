package com.paradoxlog.app.core.navigation

import org.junit.Assert.assertEquals
import org.junit.Assert.assertFalse
import org.junit.Assert.assertTrue
import org.junit.Test

class ParadoxDestinationTest {

    @Test
    fun `test canonical routes are correctly configured`() {
        assertEquals("story", ParadoxDestination.Story.route)
        assertEquals("timeline_collapse", ParadoxDestination.TimelineCollapse.route)
        assertEquals("timeline", ParadoxDestination.Timeline.route)
        assertEquals("inventory", ParadoxDestination.Inventory.route)
        assertEquals("settings", ParadoxDestination.Settings.route)
        assertEquals("act_transition/{completedActId}/{nextActId}", ParadoxDestination.ActTransition.route)
        assertEquals("ending/{endingId}", ParadoxDestination.Ending.route)
    }

    @Test
    fun `test route generation with stable primitive identifiers`() {
        val actRoute = ParadoxDestination.ActTransition.createRoute(
            completedActId = "act-01",
            nextActId = "act-02"
        )
        assertEquals("act_transition/act-01/act-02", actRoute)

        val endingRoute = ParadoxDestination.Ending.createRoute("THE_SACRIFICE")
        assertEquals("ending/THE_SACRIFICE", endingRoute)
    }

    @Test
    fun `test navigation routes do not contain serialized domain or game state objects`() {
        // Assert that all defined routes only use string identifiers and never embed complex objects
        val allDestinations = listOf(
            ParadoxDestination.Story.route,
            ParadoxDestination.TimelineCollapse.route,
            ParadoxDestination.Timeline.route,
            ParadoxDestination.Inventory.route,
            ParadoxDestination.Settings.route,
            ParadoxDestination.ActTransition.route,
            ParadoxDestination.Ending.route
        )

        allDestinations.forEach { route ->
            assertFalse("Route should not mention state: $route", route.contains("gameState", ignoreCase = true))
            assertFalse("Route should not mention scene: $route", route.contains("sceneObject", ignoreCase = true))
            assertFalse("Route should not mention choice: $route", route.contains("choiceObject", ignoreCase = true))
            assertFalse("Route should not mention variables: $route", route.contains("variables", ignoreCase = true))
        }
    }

    @Test
    fun `test argument keys are consistent with route pattern`() {
        assertTrue(
            ParadoxDestination.ActTransition.route.contains("{${ParadoxDestination.ActTransition.ARG_COMPLETED_ACT}}")
        )
        assertTrue(
            ParadoxDestination.ActTransition.route.contains("{${ParadoxDestination.ActTransition.ARG_NEXT_ACT}}")
        )
        assertTrue(
            ParadoxDestination.Ending.route.contains("{${ParadoxDestination.Ending.ARG_ENDING_ID}}")
        )
    }
}
