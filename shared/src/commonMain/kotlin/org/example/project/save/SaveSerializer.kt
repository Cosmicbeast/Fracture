package org.example.project.save

import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json
import org.example.project.core.GameState
import org.example.project.util.Logger

object SaveSerializer {

    private val json = Json {
        prettyPrint = false
        ignoreUnknownKeys = true
        encodeDefaults = true
    }

    fun serializeSlot(slot: SaveSlot): String {
        return json.encodeToString(slot)
    }

    fun deserializeSlot(jsonString: String): SaveSlot? {
        return try {
            json.decodeFromString<SaveSlot>(jsonString)
        } catch (e: Exception) {
            Logger.e("Failed to deserialize SaveSlot", e)
            null
        }
    }

    fun serializeState(state: GameState): String {
        return json.encodeToString(state)
    }

    fun deserializeState(jsonString: String): GameState? {
        return try {
            json.decodeFromString<GameState>(jsonString)
        } catch (e: Exception) {
            Logger.e("Failed to deserialize GameState", e)
            null
        }
    }
}
