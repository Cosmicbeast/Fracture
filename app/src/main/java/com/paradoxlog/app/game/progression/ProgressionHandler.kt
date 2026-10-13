package com.paradoxlog.app.game.progression

import com.paradoxlog.app.domain.model.Choice
import com.paradoxlog.app.game.state.GameState
import com.paradoxlog.app.game.state.TimelineProgress

interface ProgressionHandler {
    fun transitionScene(
        currentSceneId: String,
        choice: Choice,
        state: GameState
    ): GameState

    fun transitionAct(
        completedActId: String,
        nextActId: String,
        nextActInitialSceneId: String,
        state: GameState
    ): GameState
}

class DefaultProgressionHandler : ProgressionHandler {

    override fun transitionScene(
        currentSceneId: String,
        choice: Choice,
        state: GameState
    ): GameState {
        val updatedVisited = state.timeline.visitedScenes + currentSceneId + choice.targetSceneId
        val updatedDecisions = state.timeline.decisionsMade + (currentSceneId to choice.id)

        val updatedTimeline = state.timeline.copy(
            visitedScenes = updatedVisited,
            decisionsMade = updatedDecisions
        )

        return state.copy(
            currentSceneId = choice.targetSceneId,
            timeline = updatedTimeline
        )
    }

    override fun transitionAct(
        completedActId: String,
        nextActId: String,
        nextActInitialSceneId: String,
        state: GameState
    ): GameState {
        val updatedActsCompleted = if (state.timeline.actsCompleted.contains(completedActId)) {
            state.timeline.actsCompleted
        } else {
            state.timeline.actsCompleted + completedActId
        }

        val updatedVisited = state.timeline.visitedScenes + nextActInitialSceneId

        val updatedTimeline = state.timeline.copy(
            actsCompleted = updatedActsCompleted,
            visitedScenes = updatedVisited
        )

        return state.copy(
            currentActId = nextActId,
            currentSceneId = nextActInitialSceneId,
            timeline = updatedTimeline
        )
    }
}
