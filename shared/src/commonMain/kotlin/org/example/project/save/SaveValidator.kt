package org.example.project.save

import org.example.project.util.ValidationUtils

object SaveValidator {
    fun isValid(slot: SaveSlot): Boolean {
        return slot.slotId.isNotBlank() &&
               ValidationUtils.validateGameState(slot.state)
    }
}
