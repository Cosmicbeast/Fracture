package com.paradoxlog.app.data.content.parser

import com.paradoxlog.app.domain.model.Act
import com.paradoxlog.app.domain.model.Item
import com.paradoxlog.app.domain.model.Scene

class ContentParsingException(message: String, cause: Throwable? = null) : Exception(message, cause)

interface ContentParser {
    @Throws(ContentParsingException::class)
    fun parseAct(json: String): Act

    @Throws(ContentParsingException::class)
    fun parseScene(json: String): Scene

    @Throws(ContentParsingException::class)
    fun parseScenes(json: String): List<Scene>

    @Throws(ContentParsingException::class)
    fun parseItems(json: String): List<Item>
}
