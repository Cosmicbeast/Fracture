package com.paradoxlog.app.domain.model

data class ChoiceCondition(
    val requiredFlags: Set<GameFlag> = emptySet(),
    val forbiddenFlags: Set<GameFlag> = emptySet(),
    val minTyrantIndex: Int? = null,
    val maxTyrantIndex: Int? = null,
    val minTimelineStability: Int? = null,
    val maxTimelineStability: Int? = null,
    val minSelfTrust: Int? = null,
    val maxSelfTrust: Int? = null,
    val requiredItems: Set<String> = emptySet()
)
