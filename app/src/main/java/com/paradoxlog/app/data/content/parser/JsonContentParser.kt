package com.paradoxlog.app.data.content.parser

import com.paradoxlog.app.domain.model.Act
import com.paradoxlog.app.domain.model.Choice
import com.paradoxlog.app.domain.model.ChoiceCondition
import com.paradoxlog.app.domain.model.ChoiceConsequence
import com.paradoxlog.app.domain.model.Dialogue
import com.paradoxlog.app.domain.model.GameFlag
import com.paradoxlog.app.domain.model.Item
import com.paradoxlog.app.domain.model.Scene
import org.json.JSONArray
import org.json.JSONException
import org.json.JSONObject

class JsonContentParser : ContentParser {

    override fun parseAct(json: String): Act {
        return try {
            val obj = JSONObject(json)
            Act(
                id = obj.getString("id"),
                actNumber = obj.optInt("actNumber", 1),
                title = obj.getString("title"),
                description = obj.optString("description", ""),
                initialSceneId = obj.getString("initialSceneId"),
                sceneIds = obj.optJSONArray("sceneIds")?.toStringList() ?: emptyList()
            )
        } catch (e: JSONException) {
            throw ContentParsingException("Failed to parse Act JSON", e)
        }
    }

    override fun parseScene(json: String): Scene {
        return try {
            val obj = JSONObject(json)
            parseSceneObject(obj)
        } catch (e: JSONException) {
            throw ContentParsingException("Failed to parse Scene JSON", e)
        }
    }

    override fun parseScenes(json: String): List<Scene> {
        return try {
            val trimmed = json.trim()
            val array = if (trimmed.startsWith("[")) {
                JSONArray(trimmed)
            } else {
                val obj = JSONObject(trimmed)
                obj.optJSONArray("scenes") ?: JSONArray()
            }
            val scenes = mutableListOf<Scene>()
            for (i in 0 until array.length()) {
                val sceneObj = array.getJSONObject(i)
                scenes.add(parseSceneObject(sceneObj))
            }
            scenes
        } catch (e: JSONException) {
            throw ContentParsingException("Failed to parse Scenes JSON", e)
        }
    }

    override fun parseItems(json: String): List<Item> {
        return try {
            val trimmed = json.trim()
            val array = if (trimmed.startsWith("[")) {
                JSONArray(trimmed)
            } else {
                val obj = JSONObject(trimmed)
                obj.optJSONArray("items") ?: JSONArray()
            }
            val items = mutableListOf<Item>()
            for (i in 0 until array.length()) {
                val itemObj = array.getJSONObject(i)
                items.add(
                    Item(
                        id = itemObj.getString("id"),
                        name = itemObj.getString("name"),
                        description = itemObj.optString("description", "")
                    )
                )
            }
            items
        } catch (e: JSONException) {
            throw ContentParsingException("Failed to parse Items JSON", e)
        }
    }

    private fun parseSceneObject(obj: JSONObject): Scene {
        val dialoguesArray = obj.optJSONArray("dialogues")
        val dialogues = mutableListOf<Dialogue>()
        if (dialoguesArray != null) {
            for (i in 0 until dialoguesArray.length()) {
                val dObj = dialoguesArray.getJSONObject(i)
                dialogues.add(
                    Dialogue(
                        id = dObj.getString("id"),
                        speaker = dObj.getString("speaker"),
                        text = dObj.getString("text")
                    )
                )
            }
        }

        val choicesArray = obj.optJSONArray("choices")
        val choices = mutableListOf<Choice>()
        if (choicesArray != null) {
            for (i in 0 until choicesArray.length()) {
                val cObj = choicesArray.getJSONObject(i)
                choices.add(parseChoiceObject(cObj))
            }
        }

        return Scene(
            id = obj.getString("id"),
            actId = obj.getString("actId"),
            title = obj.optString("title", ""),
            dialogues = dialogues,
            choices = choices
        )
    }

    private fun parseChoiceObject(cObj: JSONObject): Choice {
        val conditionObj = cObj.optJSONObject("condition")
        val condition = conditionObj?.let {
            ChoiceCondition(
                requiredFlags = it.optJSONArray("requiredFlags")?.toGameFlagSet() ?: emptySet(),
                forbiddenFlags = it.optJSONArray("forbiddenFlags")?.toGameFlagSet() ?: emptySet(),
                minTyrantIndex = it.optNullableInt("minTyrantIndex"),
                maxTyrantIndex = it.optNullableInt("maxTyrantIndex"),
                minTimelineStability = it.optNullableInt("minTimelineStability"),
                maxTimelineStability = it.optNullableInt("maxTimelineStability"),
                minSelfTrust = it.optNullableInt("minSelfTrust"),
                maxSelfTrust = it.optNullableInt("maxSelfTrust"),
                requiredItems = it.optJSONArray("requiredItems")?.toStringSet() ?: emptySet()
            )
        }

        val consequenceObj = cObj.optJSONObject("consequence")
        val consequence = consequenceObj?.let {
            ChoiceConsequence(
                tyrantIndexDelta = it.optInt("tyrantIndexDelta", 0),
                timelineStabilityDelta = it.optInt("timelineStabilityDelta", 0),
                selfTrustDelta = it.optInt("selfTrustDelta", 0),
                setFlags = it.optJSONArray("setFlags")?.toGameFlagSet() ?: emptySet(),
                clearFlags = it.optJSONArray("clearFlags")?.toGameFlagSet() ?: emptySet(),
                addedItems = it.optJSONArray("addedItems")?.toStringSet() ?: emptySet(),
                removedItems = it.optJSONArray("removedItems")?.toStringSet() ?: emptySet(),
                nextActId = if (it.has("nextActId") && !it.isNull("nextActId")) it.getString("nextActId") else null
            )
        }

        return Choice(
            id = cObj.getString("id"),
            text = cObj.getString("text"),
            targetSceneId = cObj.getString("targetSceneId"),
            condition = condition,
            consequence = consequence
        )
    }

    private fun JSONArray.toStringList(): List<String> {
        val list = ArrayList<String>(length())
        for (i in 0 until length()) {
            list.add(getString(i))
        }
        return list
    }

    private fun JSONArray.toStringSet(): Set<String> {
        val set = HashSet<String>(length())
        for (i in 0 until length()) {
            set.add(getString(i))
        }
        return set
    }

    private fun JSONArray.toGameFlagSet(): Set<GameFlag> {
        val set = HashSet<GameFlag>(length())
        for (i in 0 until length()) {
            set.add(GameFlag(getString(i)))
        }
        return set
    }

    private fun JSONObject.optNullableInt(key: String): Int? {
        return if (has(key) && !isNull(key)) getInt(key) else null
    }
}
