package org.example.project.engine

import org.example.project.config.BalanceRules
import org.example.project.core.GameState
import org.example.project.core.Scene

object DialogueEngine {

    fun renderSceneProse(scene: Scene, state: GameState): String {
        val baseProse = scene.prose.trimIndent()
        val builder = StringBuilder(baseProse)

        if (BalanceRules.isTimelineCritical(state)) {
            builder.append("\n\n[TIMELINE INSTABILITY CRITICAL: The surrounding architecture wavers like heat over glass. Strands of unchosen futures unravel at the periphery of vision.]")
        }

        if (BalanceRules.isTyrannyImminent(state)) {
            builder.append("\n\n[THE BURDEN OF COMMAND: Empathy has become a luxury this fractured timeline cannot afford. Every variable must be disciplined.]")
        }

        return builder.toString()
    }
}
