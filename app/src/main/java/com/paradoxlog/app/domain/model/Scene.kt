package com.paradoxlog.app.domain.model

data class Scene(
    val id: String,
    val actId: String,
    val title: String = "",
    val dialogues: List<Dialogue> = emptyList(),
    val choices: List<Choice> = emptyList()
)
