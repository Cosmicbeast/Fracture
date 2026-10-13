package com.paradoxlog.app.domain

import com.paradoxlog.app.domain.model.Act
import com.paradoxlog.app.domain.model.Choice
import com.paradoxlog.app.domain.model.ChoiceCondition
import com.paradoxlog.app.domain.model.ChoiceConsequence
import com.paradoxlog.app.domain.model.Dialogue
import com.paradoxlog.app.domain.model.GameFlag
import com.paradoxlog.app.domain.model.Item
import com.paradoxlog.app.domain.model.Scene
import org.junit.Assert.assertEquals
import org.junit.Assert.assertNotNull
import org.junit.Test

class ModelConstructionTest {

    @Test
    fun testActConstruction() {
        val act = Act(
            id = "act-01",
            actNumber = 1,
            title = "The Shattered Loop",
            description = "Initial timeline breach.",
            initialSceneId = "scene-01-01",
            sceneIds = listOf("scene-01-01", "scene-01-02")
        )
        assertEquals("act-01", act.id)
        assertEquals(1, act.actNumber)
        assertEquals("The Shattered Loop", act.title)
        assertEquals("scene-01-01", act.initialSceneId)
        assertEquals(2, act.sceneIds.size)
    }

    @Test
    fun testSceneHierarchyConstruction() {
        val dialogue = Dialogue(
            id = "dlg-01",
            speaker = "Unknown Voice",
            text = "Step away from the chronometer."
        )
        val condition = ChoiceCondition(
            requiredFlags = setOf(GameFlag("terminal_accessed")),
            minTimelineStability = 40
        )
        val consequence = ChoiceConsequence(
            tyrantIndexDelta = 5,
            timelineStabilityDelta = -10,
            selfTrustDelta = -5,
            setFlags = setOf(GameFlag("stranger_distrusted"))
        )
        val choice = Choice(
            id = "choice-01",
            text = "Overload the core.",
            targetSceneId = "scene-01-02",
            condition = condition,
            consequence = consequence
        )
        val scene = Scene(
            id = "scene-01-01",
            actId = "act-01",
            title = "Observation Deck",
            dialogues = listOf(dialogue),
            choices = listOf(choice)
        )

        assertEquals("scene-01-01", scene.id)
        assertEquals(1, scene.dialogues.size)
        assertEquals(1, scene.choices.size)
        assertEquals("choice-01", scene.choices.first().id)
        assertEquals(5, scene.choices.first().consequence?.tyrantIndexDelta)
        assertEquals(-10, scene.choices.first().consequence?.timelineStabilityDelta)
        assertEquals(setOf(GameFlag("stranger_distrusted")), scene.choices.first().consequence?.setFlags)
    }

    @Test
    fun testItemConstruction() {
        val item = Item(
            id = "chrono_blade",
            name = "Chrono Blade",
            description = "A tempered temporal blade."
        )
        assertEquals("chrono_blade", item.id)
        assertEquals("Chrono Blade", item.name)
    }

    @Test
    fun testGameFlagValueClass() {
        val flag = GameFlag("terminal_accessed")
        assertEquals("terminal_accessed", flag.value)
    }

    @Test(expected = IllegalArgumentException::class)
    fun testBlankGameFlagThrows() {
        GameFlag("   ")
    }
}
