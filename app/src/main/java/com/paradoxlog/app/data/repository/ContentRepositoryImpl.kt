package com.paradoxlog.app.data.repository

import com.paradoxlog.app.data.content.parser.ContentParser
import com.paradoxlog.app.domain.model.Act
import com.paradoxlog.app.domain.model.Item
import com.paradoxlog.app.domain.model.Scene
import com.paradoxlog.app.domain.repository.ContentRepository

interface ContentDataSource {
    suspend fun getActJson(actId: String): String?
    suspend fun getScenesJson(actId: String): String?
    suspend fun getItemsJson(): String?
}

class ContentRepositoryImpl(
    private val dataSource: ContentDataSource,
    private val parser: ContentParser
) : ContentRepository {

    override suspend fun getAct(actId: String): Act? {
        val json = dataSource.getActJson(actId) ?: return null
        return parser.parseAct(json)
    }

    override suspend fun getScenesForAct(actId: String): List<Scene> {
        val json = dataSource.getScenesJson(actId) ?: return emptyList()
        return parser.parseScenes(json)
    }

    override suspend fun getScene(actId: String, sceneId: String): Scene? {
        val scenes = getScenesForAct(actId)
        return scenes.find { it.id == sceneId }
    }

    override suspend fun getItems(): List<Item> {
        val json = dataSource.getItemsJson() ?: return emptyList()
        return parser.parseItems(json)
    }
}
