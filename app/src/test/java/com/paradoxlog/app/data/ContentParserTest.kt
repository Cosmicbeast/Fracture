package com.paradoxlog.app.data

import com.paradoxlog.app.data.content.parser.JsonContentParser
import com.paradoxlog.app.domain.model.GameFlag
import org.junit.Assert.assertEquals
import org.junit.Assert.assertNotNull
import org.junit.Test

class ContentParserTest {

    private val parser = JsonContentParser()

    @Test
    fun testParseActJson() {
        val json = """
            {
                "id": "act-01",
                "actNumber": 1,
                "title": "Act One: Echoes",
                "description": "The initial loop.",
                "initialSceneId": "scene-01-01",
                "sceneIds": ["scene-01-01", "scene-01-02"]
            }
        """.trimIndent()

        val act = parser.parseAct(json)
        assertEquals("act-01", act.id)
        assertEquals(1, act.actNumber)
        assertEquals("Act One: Echoes", act.title)
        assertEquals("scene-01-01", act.initialSceneId)
        assertEquals(2, act.sceneIds.size)
    }

    @Test
    fun testParseSceneJsonWithChoiceAndConsequence() {
        val json = """
            {
                "id": "scene-01-01",
                "actId": "act-01",
                "title": "Airlock Breach",
                "dialogues": [
                    {
                        "id": "dlg-1",
                        "speaker": "Observer",
                        "text": "The timeline is fracturing."
                    }
                ],
                "choices": [
                    {
                        "id": "choice-1",
                        "text": "Seal the bulkhead.",
                        "targetSceneId": "scene-01-02",
                        "condition": {
                            "requiredFlags": ["seal_code_found"],
                            "minTimelineStability": 30
                        },
                        "consequence": {
                            "tyrantIndexDelta": -2,
                            "timelineStabilityDelta": 10,
                            "selfTrustDelta": 5,
                            "setFlags": ["bulkhead_sealed"],
                            "addedItems": ["chrono_blade"]
                        }
                    }
                ]
            }
        """.trimIndent()

        val scene = parser.parseScene(json)
        assertEquals("scene-01-01", scene.id)
        assertEquals("act-01", scene.actId)
        assertEquals(1, scene.dialogues.size)
        assertEquals(1, scene.choices.size)

        val choice = scene.choices[0]
        assertEquals("choice-1", choice.id)
        assertEquals("scene-01-02", choice.targetSceneId)
        assertEquals(setOf(GameFlag("seal_code_found")), choice.condition?.requiredFlags)
        assertEquals(10, choice.consequence?.timelineStabilityDelta)
        assertEquals(setOf(GameFlag("bulkhead_sealed")), choice.consequence?.setFlags)
        assertEquals(setOf("chrono_blade"), choice.consequence?.addedItems)
    }

    @Test
    fun testParseItemsJson() {
        val json = """
            [
                {
                    "id": "chrono_blade",
                    "name": "Chrono Blade",
                    "description": "Tempered temporal blade."
                }
            ]
        """.trimIndent()

        val items = parser.parseItems(json)
        assertEquals(1, items.size)
        assertEquals("chrono_blade", items[0].id)
        assertEquals("Chrono Blade", items[0].name)
    }
}
