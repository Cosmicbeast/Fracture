package org.example.project.save

import org.example.project.config.GameConstants
import org.example.project.core.GameState
import org.example.project.util.Logger

object SaveManager {

    private val storage: StorageBackend by lazy { getPlatformStorage() }

    fun autoSave(state: GameState, sceneTitle: String) {
        saveSlot(
            slotId = GameConstants.AUTO_SAVE_SLOT,
            state = state,
            sceneTitle = sceneTitle
        )
    }

    fun loadAutoSave(): GameState? = loadSlot(GameConstants.AUTO_SAVE_SLOT)

    fun hasAutoSave(): Boolean = hasSlot(GameConstants.AUTO_SAVE_SLOT)

    fun saveSlot(slotId: String, state: GameState, sceneTitle: String): Boolean {
        return try {
            val slot = SaveSlot(
                slotId = slotId,
                timestampMillis = 1L, // Abstract timestamp indicator
                actNumber = state.currentActNumber,
                sceneTitle = sceneTitle,
                stabilityPercentage = state.timelineStability,
                tyrantIndex = state.tyrantIndex,
                state = state
            )
            val serialized = SaveSerializer.serializeSlot(slot)
            storage.setString(keyForSlot(slotId), serialized)
            Logger.i("Saved game successfully to slot '$slotId'")
            true
        } catch (e: Exception) {
            Logger.e("Failed to save slot '$slotId'", e)
            false
        }
    }

    fun loadSlot(slotId: String): GameState? {
        val serialized = storage.getString(keyForSlot(slotId)) ?: return null
        val slot = SaveSerializer.deserializeSlot(serialized)
        return slot?.state
    }

    fun getSlotInfo(slotId: String): SaveSlot? {
        val serialized = storage.getString(keyForSlot(slotId)) ?: return null
        return SaveSerializer.deserializeSlot(serialized)
    }

    fun hasSlot(slotId: String): Boolean {
        return storage.getString(keyForSlot(slotId)) != null
    }

    fun deleteSlot(slotId: String) {
        storage.remove(keyForSlot(slotId))
    }

    private fun keyForSlot(slotId: String): String = "fracture_save_slot_$slotId"
}
