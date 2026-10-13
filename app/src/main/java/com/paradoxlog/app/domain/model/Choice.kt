package com.paradoxlog.app.domain.model

data class Choice(
    val id: String,
    val text: String,
    val targetSceneId: String,
    val condition: ChoiceCondition? = null,
    val consequence: ChoiceConsequence? = null
)
