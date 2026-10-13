package com.paradoxlog.app.di

import com.paradoxlog.app.domain.repository.ContentRepository
import com.paradoxlog.app.game.consequences.ConsequenceApplier
import com.paradoxlog.app.game.consequences.DefaultConsequenceApplier
import com.paradoxlog.app.game.endings.DefaultEndingEvaluator
import com.paradoxlog.app.game.endings.EndingEvaluator
import com.paradoxlog.app.game.engine.DefaultGameEngine
import com.paradoxlog.app.game.engine.GameEngine
import com.paradoxlog.app.game.progression.DefaultProgressionHandler
import com.paradoxlog.app.game.progression.ProgressionHandler
import com.paradoxlog.app.game.rules.ConditionEvaluator
import com.paradoxlog.app.game.rules.DefaultConditionEvaluator
import com.paradoxlog.app.game.save.GameSaveManager
import com.paradoxlog.app.game.save.JsonGameSaveManager

object GameModule {
    fun provideConditionEvaluator(): ConditionEvaluator = DefaultConditionEvaluator()
    fun provideConsequenceApplier(): ConsequenceApplier = DefaultConsequenceApplier()
    fun provideProgressionHandler(): ProgressionHandler = DefaultProgressionHandler()
    fun provideEndingEvaluator(): EndingEvaluator = DefaultEndingEvaluator()
    fun provideGameSaveManager(): GameSaveManager = JsonGameSaveManager()

    fun provideGameEngine(contentRepository: ContentRepository): GameEngine {
        return DefaultGameEngine(
            contentRepository = contentRepository,
            conditionEvaluator = provideConditionEvaluator(),
            consequenceApplier = provideConsequenceApplier(),
            progressionHandler = provideProgressionHandler(),
            endingEvaluator = provideEndingEvaluator()
        )
    }
}
