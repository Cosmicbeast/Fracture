package com.paradoxlog.app.game.state

import com.paradoxlog.app.domain.model.GameFlag

data class GameVariables(
    val tyrantIndex: Int = 0,
    val timelineStability: Int = 100,
    val selfTrust: Int = 50
)

data class InventoryState(
    val itemIds: Set<String> = emptySet()
)

data class TimelineProgress(
    val actsCompleted: List<String> = emptyList(),
    val visitedScenes: Set<String> = emptySet(),
    val decisionsMade: Map<String, String> = emptyMap()
)

data class EndingState(
    val eligibleEnding: String? = null,
    val isResolved: Boolean = false,
    val endingSceneId: String? = null
)

data class GameState(
    val version: Int = 1,
    val currentActId: String = "",
    val currentSceneId: String = "",
    val variables: GameVariables = GameVariables(),
    val flags: Set<GameFlag> = emptySet(),
    val inventory: InventoryState = InventoryState(),
    val timeline: TimelineProgress = TimelineProgress(),
    val ending: EndingState = EndingState()
) {
    val tyrantIndex: Int get() = variables.tyrantIndex
    val timelineStability: Int get() = variables.timelineStability
    val selfTrust: Int get() = variables.selfTrust

    fun hasFlag(flag: GameFlag): Boolean = flags.contains(flag)
    fun hasFlag(flagId: String): Boolean = flags.contains(GameFlag(flagId))
    fun hasItem(itemId: String): Boolean = inventory.itemIds.contains(itemId)
}
