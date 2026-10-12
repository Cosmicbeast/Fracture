package org.example.project.core

import kotlinx.serialization.Serializable
import org.example.project.core.enums.AnomalyType

@Serializable
data class Scene(
    val id: String,
    val title: String,
    val backgroundDrawableName: String = "scene_1_sky",
    val prose: String,
    val choices: List<Choice> = emptyList(),
    val anomalyEncountered: AnomalyType? = null,
    val isTerminalScene: Boolean = false,
    val isActFinale: Boolean = false
)
