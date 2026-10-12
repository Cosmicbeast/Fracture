package org.example.project.core

import kotlinx.serialization.Serializable
import org.example.project.core.enums.Perspective

@Serializable
data class Act(
    val actNumber: Int,
    val title: String,
    val subtitle: String,
    val perspective: Perspective,
    val startingSceneId: String,
    val scenes: Map<String, Scene>
)
