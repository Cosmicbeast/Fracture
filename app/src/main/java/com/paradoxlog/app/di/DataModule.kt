package com.paradoxlog.app.di

import com.paradoxlog.app.data.content.parser.ContentParser
import com.paradoxlog.app.data.content.parser.JsonContentParser
import com.paradoxlog.app.data.repository.ContentDataSource
import com.paradoxlog.app.data.repository.ContentRepositoryImpl
import com.paradoxlog.app.domain.repository.ContentRepository

object DataModule {
    fun provideContentParser(): ContentParser = JsonContentParser()

    fun provideContentRepository(
        dataSource: ContentDataSource,
        parser: ContentParser = provideContentParser()
    ): ContentRepository {
        return ContentRepositoryImpl(dataSource, parser)
    }
}
