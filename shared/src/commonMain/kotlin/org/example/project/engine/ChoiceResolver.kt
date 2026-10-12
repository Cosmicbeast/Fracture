package org.example.project.engine

import org.example.project.core.Choice
import org.example.project.core.GameState
import org.example.project.core.enums.ToolType
import org.example.project.util.Logger

object ChoiceResolver {

    fun isChoiceAvailable(choice: Choice, state: GameState): Boolean {
        if (choice.requiredTool != null && !state.hasTool(choice.requiredTool)) {
            return false
        }
        if (choice.minTyrantIndex != null && state.tyrantIndex < choice.minTyrantIndex) {
            return false
        }
        if (choice.maxTyrantIndex != null && state.tyrantIndex > choice.maxTyrantIndex) {
            return false
        }
        if (choice.minStability != null && state.timelineStability < choice.minStability) {
            return false
        }
        if (choice.requiredFlag != null && !state.hasFlag(choice.requiredFlag)) {
            return false
        }
        return true
    }

    fun filterAvailableChoices(choices: List<Choice>, state: GameState): List<Choice> {
        return choices.filter { isChoiceAvailable(it, state) }
    }

    fun resolve(choice: Choice, state: GameState): GameState {
        Logger.i("Player selected choice [${choice.id}]: '${choice.text}'")
        return state.applyChoice(choice)
    }

    fun getForesightPreview(choice: Choice, state: GameState): String? {
        if (state.hasTool(ToolType.CHRONO_BLADE) && choice.foresightHint != null) {
            return choice.foresightHint
        }
        return null
    }

    fun getStabilityPreview(choice: Choice, state: GameState): String? {
        if (state.hasTool(ToolType.AION_COMPASS) && choice.stabilityHint != null) {
            return choice.stabilityHint
        }
        return null
    }
}
