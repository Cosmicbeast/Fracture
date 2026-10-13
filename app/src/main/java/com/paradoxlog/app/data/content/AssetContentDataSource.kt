package com.paradoxlog.app.data.content

import android.content.res.AssetManager
import com.paradoxlog.app.data.repository.ContentDataSource
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

/**
 * Production implementation of ContentDataSource reading canonical JSON
 * manifests from packaged application assets.
 */
class AssetContentDataSource(
    private val assetManager: AssetManager
) : ContentDataSource {

    override suspend fun getActJson(actId: String): String? = withContext(Dispatchers.IO) {
        readAssetFile("acts/$actId/metadata.json")
    }

    override suspend fun getScenesJson(actId: String): String? = withContext(Dispatchers.IO) {
        readAssetFile("acts/$actId/scenes.json")
    }

    override suspend fun getItemsJson(): String? = withContext(Dispatchers.IO) {
        readAssetFile("items/items.json")
    }

    private fun readAssetFile(path: String): String? {
        return try {
            assetManager.open(path).bufferedReader().use { it.readText() }
        } catch (_: Exception) {
            try {
                assetManager.open("content/$path").bufferedReader().use { it.readText() }
            } catch (_: Exception) {
                null
            }
        }
    }
}
