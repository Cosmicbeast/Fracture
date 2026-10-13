package com.paradoxlog.app.game.save

import com.paradoxlog.app.domain.model.GameFlag
import com.paradoxlog.app.game.state.EndingState
import com.paradoxlog.app.game.state.GameState
import com.paradoxlog.app.game.state.GameVariables
import com.paradoxlog.app.game.state.InventoryState
import com.paradoxlog.app.game.state.TimelineProgress
import org.json.JSONArray
import org.json.JSONException
import org.json.JSONObject

class SaveStateException(message: String, cause: Throwable? = null) : Exception(message, cause)

interface GameSaveManager {
    @Throws(SaveStateException::class)
    fun serialize(state: GameState): String

    @Throws(SaveStateException::class)
    fun deserialize(json: String): GameState
}

class JsonGameSaveManager : GameSaveManager {

    companion object {
        const val CURRENT_SCHEMA_VERSION = 1
    }

    override fun serialize(state: GameState): String {
        return try {
            val root = JSONObject()
            root.put("version", state.version)
            root.put("currentActId", state.currentActId)
            root.put("currentSceneId", state.currentSceneId)

            val varsObj = JSONObject()
            varsObj.put("tyrantIndex", state.variables.tyrantIndex)
            varsObj.put("timelineStability", state.variables.timelineStability)
            varsObj.put("selfTrust", state.variables.selfTrust)
            root.put("variables", varsObj)

            val flagsArray = JSONArray()
            state.flags.forEach { flagsArray.put(it.value) }
            root.put("flags", flagsArray)

            val invObj = JSONObject()
            val itemsArray = JSONArray()
            state.inventory.itemIds.forEach { itemsArray.put(it) }
            invObj.put("itemIds", itemsArray)
            root.put("inventory", invObj)

            val timelineObj = JSONObject()
            val actsArray = JSONArray()
            state.timeline.actsCompleted.forEach { actsArray.put(it) }
            timelineObj.put("actsCompleted", actsArray)

            val scenesArray = JSONArray()
            state.timeline.visitedScenes.forEach { scenesArray.put(it) }
            timelineObj.put("visitedScenes", scenesArray)

            val decisionsObj = JSONObject()
            state.timeline.decisionsMade.forEach { (sceneId, choiceId) ->
                decisionsObj.put(sceneId, choiceId)
            }
            timelineObj.put("decisionsMade", decisionsObj)
            root.put("timeline", timelineObj)

            val endingObj = JSONObject()
            endingObj.put("eligibleEnding", state.ending.eligibleEnding ?: JSONObject.NULL)
            endingObj.put("isResolved", state.ending.isResolved)
            endingObj.put("endingSceneId", state.ending.endingSceneId ?: JSONObject.NULL)
            root.put("ending", endingObj)

            root.toString(2)
        } catch (e: JSONException) {
            throw SaveStateException("Failed to serialize GameState to JSON", e)
        }
    }

    override fun deserialize(json: String): GameState {
        return try {
            val root = JSONObject(json)
            val version = root.getInt("version")

            val currentActId = root.optString("currentActId", "")
            val currentSceneId = root.optString("currentSceneId", "")

            val varsObj = root.getJSONObject("variables")
            val variables = GameVariables(
                tyrantIndex = varsObj.getInt("tyrantIndex"),
                timelineStability = varsObj.getInt("timelineStability"),
                selfTrust = varsObj.getInt("selfTrust")
            )

            val flagsArray = root.getJSONArray("flags")
            val flags = mutableSetOf<GameFlag>()
            for (i in 0 until flagsArray.length()) {
                flags.add(GameFlag(flagsArray.getString(i)))
            }

            val invObj = root.getJSONObject("inventory")
            val itemsArray = invObj.getJSONArray("itemIds")
            val itemIds = mutableSetOf<String>()
            for (i in 0 until itemsArray.length()) {
                itemIds.add(itemsArray.getString(i))
            }
            val inventory = InventoryState(itemIds = itemIds)

            val timelineObj = root.getJSONObject("timeline")
            val actsArray = timelineObj.getJSONArray("actsCompleted")
            val actsCompleted = mutableListOf<String>()
            for (i in 0 until actsArray.length()) {
                actsCompleted.add(actsArray.getString(i))
            }

            val scenesArray = timelineObj.getJSONArray("visitedScenes")
            val visitedScenes = mutableSetOf<String>()
            for (i in 0 until scenesArray.length()) {
                visitedScenes.add(scenesArray.getString(i))
            }

            val decisionsObj = timelineObj.getJSONObject("decisionsMade")
            val decisionsMade = mutableMapOf<String, String>()
            decisionsObj.keys().forEach { sceneId ->
                decisionsMade[sceneId] = decisionsObj.getString(sceneId)
            }
            val timeline = TimelineProgress(
                actsCompleted = actsCompleted,
                visitedScenes = visitedScenes,
                decisionsMade = decisionsMade
            )

            val endingObj = root.getJSONObject("ending")
            val eligibleEnding = if (endingObj.isNull("eligibleEnding")) null else endingObj.optString("eligibleEnding")
            val isResolved = endingObj.optBoolean("isResolved", false)
            val endingSceneId = if (endingObj.isNull("endingSceneId")) null else endingObj.optString("endingSceneId")
            val ending = EndingState(
                eligibleEnding = eligibleEnding,
                isResolved = isResolved,
                endingSceneId = endingSceneId
            )

            GameState(
                version = version,
                currentActId = currentActId,
                currentSceneId = currentSceneId,
                variables = variables,
                flags = flags,
                inventory = inventory,
                timeline = timeline,
                ending = ending
            )
        } catch (e: JSONException) {
            throw SaveStateException("Failed to deserialize GameState from JSON", e)
        }
    }
}
