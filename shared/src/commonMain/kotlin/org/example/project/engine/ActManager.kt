package org.example.project.engine

import org.example.project.core.Act
import org.example.project.core.GameState
import org.example.project.core.Scene
import org.example.project.data.acts.act01_future.Act01Narrative
import org.example.project.util.Logger

object ActManager {

    private val acts: Map<Int, Act> = mapOf(
        1 to Act01Narrative.act
    )

    fun getAct(actNumber: Int): Act? = acts[actNumber]

    fun getScene(actNumber: Int, sceneId: String): Scene? {
        val act = getAct(actNumber) ?: return null
        return act.scenes[sceneId]
    }

    fun getCurrentScene(state: GameState): Scene {
        val scene = getScene(state.currentActNumber, state.currentSceneId)
        if (scene == null) {
            Logger.w("Scene '${state.currentSceneId}' not found in Act ${state.currentActNumber}. Falling back to default.")
            return Act01Narrative.act.scenes.values.first()
        }
        return scene
    }

    fun getStartingStateForAct(actNumber: Int): GameState {
        val act = getAct(actNumber) ?: Act01Narrative.act
        return GameState(
            currentActNumber = act.actNumber,
            currentPerspective = act.perspective,
            currentSceneId = act.startingSceneId
        )
    }
}
