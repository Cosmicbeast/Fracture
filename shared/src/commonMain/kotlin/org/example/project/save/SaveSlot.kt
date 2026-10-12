package org.example.project.save

import kotlinx.serialization.Serializable
import org.example.project.core.GameState

@Serializable
data class SaveSlot(
    val slotId: String,
    val timestampMillis: Long,
    val actNumber: Int,
    val sceneTitle: String,
    val stabilityPercentage: Int,
    val tyrantIndex: Int,
    val state: GameState
)
