package com.paradoxlog.app.data

import com.paradoxlog.app.data.content.parser.ContentParser
import com.paradoxlog.app.data.repository.ContentDataSource
import com.paradoxlog.app.data.repository.ContentRepositoryImpl
import com.paradoxlog.app.domain.model.Act
import com.paradoxlog.app.domain.model.Item
import com.paradoxlog.app.domain.model.Scene
import kotlinx.coroutines.runBlocking
import org.junit.Assert.assertEquals
import org.junit.Assert.assertNotNull
import org.junit.Assert.assertNull
import org.junit.Test

class ContentRepositoryTest {

    private class FakeContentDataSource : ContentDataSource {
        var actJson: String? = "{}"
        var scenesJson: String? = "[]"
        var itemsJson: String? = "[]"

        override suspend fun getActJson(actId: String): String? = actJson
        override suspend fun getScenesJson(actId: String): String? = scenesJson
        override suspend fun getItemsJson(): String? = itemsJson
    }

    private class FakeContentParser : ContentParser {
        override fun parseAct(json: String): Act = Act(
            id = "act-01",
            actNumber = 1,
            title = "Test Act",
            initialSceneId = "scene-01"
        )

        override fun parseScene(json: String): Scene = Scene(
            id = "scene-01",
            actId = "act-01"
        )

        override fun parseScenes(json: String): List<Scene> = listOf(
            Scene(id = "scene-01", actId = "act-01"),
            Scene(id = "scene-02", actId = "act-01")
        )

        override fun parseItems(json: String): List<Item> = listOf(
            Item(id = "chrono_blade", name = "Chrono Blade")
        )
    }

    @Test
    fun testGetActReturnsParsedAct() = runBlocking {
        val dataSource = FakeContentDataSource()
        val parser = FakeContentParser()
        val repo = ContentRepositoryImpl(dataSource, parser)

        val act = repo.getAct("act-01")
        assertNotNull(act)
        assertEquals("act-01", act?.id)
        assertEquals(1, act?.actNumber)
    }

    @Test
    fun testGetActReturnsNullWhenSourceNull() = runBlocking {
        val dataSource = FakeContentDataSource().apply { actJson = null }
        val parser = FakeContentParser()
        val repo = ContentRepositoryImpl(dataSource, parser)

        val act = repo.getAct("act-01")
        assertNull(act)
    }

    @Test
    fun testGetScenesForAct() = runBlocking {
        val dataSource = FakeContentDataSource()
        val parser = FakeContentParser()
        val repo = ContentRepositoryImpl(dataSource, parser)

        val scenes = repo.getScenesForAct("act-01")
        assertEquals(2, scenes.size)
        assertEquals("scene-01", scenes[0].id)
    }

    @Test
    fun testGetSceneById() = runBlocking {
        val dataSource = FakeContentDataSource()
        val parser = FakeContentParser()
        val repo = ContentRepositoryImpl(dataSource, parser)

        val scene = repo.getScene("act-01", "scene-02")
        assertNotNull(scene)
        assertEquals("scene-02", scene?.id)

        val missing = repo.getScene("act-01", "nonexistent")
        assertNull(missing)
    }

    @Test
    fun testGetItems() = runBlocking {
        val dataSource = FakeContentDataSource()
        val parser = FakeContentParser()
        val repo = ContentRepositoryImpl(dataSource, parser)

        val items = repo.getItems()
        assertEquals(1, items.size)
        assertEquals("chrono_blade", items[0].id)
    }
}
