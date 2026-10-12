package org.example.project.engine

import org.example.project.core.Ending
import org.example.project.core.GameState

object EndingResolver {

    val allEndings: List<Ending> = listOf(
        Ending(
            id = "ending_iron_sovereign",
            title = "The Iron Sovereign",
            summary = "The timeline was preserved through absolute totalitarian discipline.",
            finalReflection = "Order was secured. But the person who saved the world is no longer someone worth saving.",
            minTyrantIndex = 70,
            maxTyrantIndex = 100,
            minStability = 50,
            maxStability = 100
        ),
        Ending(
            id = "ending_shattered_horizon",
            title = "The Shattered Horizon",
            summary = "In refusing to exert tyranny, the weight of paradox tore reality apart.",
            finalReflection = "You retained your morality, but there was nowhere left to stand.",
            minTyrantIndex = 0,
            maxTyrantIndex = 40,
            minStability = 0,
            maxStability = 30
        ),
        Ending(
            id = "ending_symmetrical_reconciliation",
            title = "Symmetrical Reconciliation",
            summary = "Against all odds, the Past Self and Future Self found harmonic convergence.",
            finalReflection = "The paradox was neither crushed nor ignored; it was accepted.",
            minTyrantIndex = 30,
            maxTyrantIndex = 60,
            minStability = 60,
            maxStability = 100
        )
    )

    fun resolveEnding(state: GameState): Ending {
        return allEndings.firstOrNull { ending ->
            state.tyrantIndex in ending.minTyrantIndex..ending.maxTyrantIndex &&
            state.timelineStability in ending.minStability..ending.maxStability
        } ?: allEndings.first()
    }
}
