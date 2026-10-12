package org.example.project.core

import kotlinx.serialization.Serializable
import org.example.project.core.enums.ToolType

@Serializable
data class Choice(
    val id: String,
    val text: String,
    val targetSceneId: String,
    val tyrantIndexDelta: Int = 0,
    val stabilityDelta: Int = 0,
    val selfTrustDelta: Int = 0,
    val requiredTool: ToolType? = null,
    val minTyrantIndex: Int? = null,
    val maxTyrantIndex: Int? = null,
    val minStability: Int? = null,
    val requiredFlag: String? = null,
    val setFlagOnChosen: String? = null,
    val foresightHint: String? = null,
    val stabilityHint: String? = null
)
