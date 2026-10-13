package com.paradoxlog.app.domain.model

data class ChoiceConsequence(
    val tyrantIndexDelta: Int = 0,
    val timelineStabilityDelta: Int = 0,
    val selfTrustDelta: Int = 0,
    val setFlags: Set<GameFlag> = emptySet(),
    val clearFlags: Set<GameFlag> = emptySet(),
    val addedItems: Set<String> = emptySet(),
    val removedItems: Set<String> = emptySet(),
    val nextActId: String? = null
)
