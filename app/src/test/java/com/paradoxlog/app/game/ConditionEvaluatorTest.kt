package com.paradoxlog.app.game

import com.paradoxlog.app.domain.model.Choice
import com.paradoxlog.app.domain.model.ChoiceCondition
import com.paradoxlog.app.domain.model.GameFlag
import com.paradoxlog.app.game.rules.DefaultConditionEvaluator
import com.paradoxlog.app.game.state.GameState
import com.paradoxlog.app.game.state.GameVariables
import com.paradoxlog.app.game.state.InventoryState
import org.junit.Assert.assertFalse
import org.junit.Assert.assertTrue
import org.junit.Test

class ConditionEvaluatorTest {

    private val evaluator = DefaultConditionEvaluator()

    @Test
    fun testNullConditionAlwaysAvailable() {
        val state = GameState()
        val choice = Choice(id = "c1", text = "Proceed", targetSceneId = "s2", condition = null)
        assertTrue(evaluator.evaluate(null, state))
        assertTrue(evaluator.isChoiceAvailable(choice, state))
    }

    @Test
    fun testRequiredFlagsCondition() {
        val condition = ChoiceCondition(requiredFlags = setOf(GameFlag("terminal_accessed")))
        val stateWithoutFlag = GameState()
        val stateWithFlag = GameState(flags = setOf(GameFlag("terminal_accessed")))

        assertFalse(evaluator.evaluate(condition, stateWithoutFlag))
        assertTrue(evaluator.evaluate(condition, stateWithFlag))
    }

    @Test
    fun testForbiddenFlagsCondition() {
        val condition = ChoiceCondition(forbiddenFlags = setOf(GameFlag("terminal_destroyed")))
        val cleanState = GameState()
        val forbiddenState = GameState(flags = setOf(GameFlag("terminal_destroyed")))

        assertTrue(evaluator.evaluate(condition, cleanState))
        assertFalse(evaluator.evaluate(condition, forbiddenState))
    }

    @Test
    fun testVariableThresholds() {
        val condition = ChoiceCondition(
            minTyrantIndex = 10,
            maxTyrantIndex = 50,
            minTimelineStability = 40,
            minSelfTrust = 30
        )

        // Below min tyrant
        val state1 = GameState(variables = GameVariables(tyrantIndex = 5, timelineStability = 50, selfTrust = 50))
        assertFalse(evaluator.evaluate(condition, state1))

        // Above max tyrant
        val state2 = GameState(variables = GameVariables(tyrantIndex = 60, timelineStability = 50, selfTrust = 50))
        assertFalse(evaluator.evaluate(condition, state2))

        // Below min stability
        val state3 = GameState(variables = GameVariables(tyrantIndex = 20, timelineStability = 30, selfTrust = 50))
        assertFalse(evaluator.evaluate(condition, state3))

        // Below min self-trust
        val state4 = GameState(variables = GameVariables(tyrantIndex = 20, timelineStability = 50, selfTrust = 20))
        assertFalse(evaluator.evaluate(condition, state4))

        // Satisfies all bounds
        val stateValid = GameState(variables = GameVariables(tyrantIndex = 25, timelineStability = 70, selfTrust = 55))
        assertTrue(evaluator.evaluate(condition, stateValid))
    }

    @Test
    fun testRequiredItemsCondition() {
        val condition = ChoiceCondition(requiredItems = setOf("chrono_blade", "aion_compass"))
        val statePartial = GameState(inventory = InventoryState(itemIds = setOf("chrono_blade")))
        val stateFull = GameState(inventory = InventoryState(itemIds = setOf("chrono_blade", "aion_compass")))

        assertFalse(evaluator.evaluate(condition, statePartial))
        assertTrue(evaluator.evaluate(condition, stateFull))
    }
}
