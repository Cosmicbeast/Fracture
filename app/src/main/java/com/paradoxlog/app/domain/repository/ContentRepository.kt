package com.paradoxlog.app.domain.repository

import com.paradoxlog.app.domain.model.Act
import com.paradoxlog.app.domain.model.Item
import com.paradoxlog.app.domain.model.Scene

interface ContentRepository {
    suspend fun getAct(actId: String): Act?
    suspend fun getScenesForAct(actId: String): List<Scene>
    suspend fun getScene(actId: String, sceneId: String): Scene?
    suspend fun getItems(): List<Item>
}
