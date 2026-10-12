package org.example.project.core

import kotlinx.serialization.Serializable
import org.example.project.core.enums.Perspective
import org.example.project.core.enums.ToolType

@Serializable
data class GameState(
    val currentActNumber: Int = 1,
    val currentPerspective: Perspective = Perspective.FUTURE_SELF,
    val currentSceneId: String = "scene_01_sky",
    val tyrantIndex: Int = 20,
    val timelineStability: Int = 85,
    val selfTrust: Int = 50,
    val activeTools: Set<ToolType> = setOf(ToolType.CHRONO_BLADE),
    val flags: Map<String, Boolean> = emptyMap(),
    val decisionHistory: List<String> = emptyList()
) {
    fun hasTool(tool: ToolType): Boolean = activeTools.contains(tool)

    fun hasFlag(flagName: String): Boolean = flags[flagName] == true

    fun applyChoice(choice: Choice): GameState {
        val updatedTyrantIndex = (tyrantIndex + choice.tyrantIndexDelta).coerceIn(0, 100)
        val updatedStability = (timelineStability + choice.stabilityDelta).coerceIn(0, 100)
        val updatedSelfTrust = (selfTrust + choice.selfTrustDelta).coerceIn(0, 100)

        val updatedFlags = if (choice.setFlagOnChosen != null) {
            flags + (choice.setFlagOnChosen to true)
        } else {
            flags
        }

        return copy(
            currentSceneId = choice.targetSceneId,
            tyrantIndex = updatedTyrantIndex,
            timelineStability = updatedStability,
            selfTrust = updatedSelfTrust,
            flags = updatedFlags,
            decisionHistory = decisionHistory + choice.id
        )
    }

    fun transitionScene(newSceneId: String): GameState = copy(currentSceneId = newSceneId)

    fun addTool(tool: ToolType): GameState = copy(activeTools = activeTools + tool)

    fun removeTool(tool: ToolType): GameState = copy(activeTools = activeTools - tool)

    companion object {
        fun initial(): GameState = GameState()
    }
}
