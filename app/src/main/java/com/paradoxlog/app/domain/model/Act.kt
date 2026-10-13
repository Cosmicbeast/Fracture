package com.paradoxlog.app.domain.model

data class Act(
    val id: String,
    val actNumber: Int,
    val title: String,
    val description: String = "",
    val initialSceneId: String,
    val sceneIds: List<String> = emptyList()
)
