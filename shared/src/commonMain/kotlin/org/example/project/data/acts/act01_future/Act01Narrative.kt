package org.example.project.data.acts.act01_future

import org.example.project.core.Act
import org.example.project.core.Choice
import org.example.project.core.Scene
import org.example.project.core.enums.AnomalyType
import org.example.project.core.enums.Perspective
import org.example.project.core.enums.ToolType

object Act01Narrative {

    val act: Act by lazy {
        Act(
            actNumber = 1,
            title = "Act I: The Sky That Never Heals",
            subtitle = "The Burden of the Future Self",
            perspective = Perspective.FUTURE_SELF,
            startingSceneId = "scene_01_sky",
            scenes = listOf(
                scene01Sky,
                scene01Chronoscope,
                scene01Array,
                scene01Chamber,
                scene01Echo,
                scene01Threshold
            ).associateBy { it.id }
        )
    }

    private val scene01Sky = Scene(
        id = "scene_01_sky",
        title = "The Sky That Never Heals",
        backgroundDrawableName = "scene_1_sky",
        prose = """
            The tear across the horizon does not bleed. It pulses with cold, inverted light—a wound in the causal continuum that has remained open for seven years.

            Below the Citadel spire, the capital sleeps beneath artificial atmospheric dampeners. They do not know how fragile tomorrow is. They do not remember the morning when the sky broke.

            You do.

            You remember the decision that tore it open. It was not made by an enemy. It was made by your own hand, years ago, when you were young enough to believe that every sacrifice could be rewritten.
        """.trimIndent(),
        choices = listOf(
            Choice(
                id = "c_01_examine_blade",
                text = "Unsheathe the Chrono Blade and read the causal resonance.",
                targetSceneId = "scene_01_chronoscope",
                tyrantIndexDelta = 0,
                stabilityDelta = 5,
                selfTrustDelta = 10,
                requiredTool = ToolType.CHRONO_BLADE,
                foresightHint = "Reveals the exact coordinates of your Past Self's first breach."
            ),
            Choice(
                id = "c_01_enforce_pulse",
                text = "Channel the Citadel Array to forcefully suppress the atmospheric tremor.",
                targetSceneId = "scene_01_array",
                tyrantIndexDelta = 10,
                stabilityDelta = 10,
                selfTrustDelta = -5,
                stabilityHint = "Stabilizes current sector, but strains civilian power relays."
            ),
            Choice(
                id = "c_01_observe_rift",
                text = "Lower your hands and watch the fracture drift.",
                targetSceneId = "scene_01_chamber",
                tyrantIndexDelta = -5,
                stabilityDelta = -10,
                selfTrustDelta = 5,
                foresightHint = "Instability will seep into the lower research containment rings."
            )
        )
    )

    private val scene01Chronoscope = Scene(
        id = "scene_01_chronoscope",
        title = "The Chrono Blade's Echo",
        backgroundDrawableName = "scene_1_sky",
        prose = """
            The crystallised edge of the Chrono Blade rings with harmonic dissonance as you draw it into the cold air.

            The weapon does not cut flesh. It slices through probability. In its translucent blue sheen, a ghost reflection appears: your younger self, standing in the lower vaults seven years prior, reaching toward the sealed Aion Gate.

            You remember what you whispered back then: 'I can fix this.'

            The blade whispers the reply you learned too late: 'Nothing is fixed. Everything is traded.'

            An alert klaxon interrupts the reflection. A containment ring in Sector 4 has begun to buckle.
        """.trimIndent(),
        choices = listOf(
            Choice(
                id = "c_01_rush_chamber_prepared",
                text = "Seal the chronoscope and descend to Sector 4.",
                targetSceneId = "scene_01_chamber",
                tyrantIndexDelta = 5,
                stabilityDelta = 5,
                selfTrustDelta = 5
            )
        )
    )

    private val scene01Array = Scene(
        id = "scene_01_array",
        title = "The Iron Hand of Necessity",
        backgroundDrawableName = "scene_1_sky",
        prose = """
            You engage the primary relays. Deep within the core, massive dampening pylons lock into position with seismic thuds.

            Above you, the rift shudders. Its jagged light is clamped down by electromagnetic harmonics. For a brief moment, the sky resembles peace.

            On your monitor, civilian distress flags flare: three residential sub-districts have suffered brownouts as power was diverted to feed the array. 

            Order has a ledger. You sign your name to it every single night.
        """.trimIndent(),
        choices = listOf(
            Choice(
                id = "c_01_proceed_from_array",
                text = "Ignore the civilian protests and inspect the containment vault.",
                targetSceneId = "scene_01_chamber",
                tyrantIndexDelta = 10,
                stabilityDelta = 5,
                selfTrustDelta = -5
            )
        )
    )

    private val scene01Chamber = Scene(
        id = "scene_01_chamber",
        title = "The Anomaly Chamber",
        backgroundDrawableName = "scene_1_sky",
        prose = """
            In Sector 4, reality has ceased to behave in linear sequence.

            Before you, a research technician is caught inside a localized temporal loop. Every six seconds, he drops a containment canister, watches it shatter, covers his mouth in horror, and then snaps backward into the doorway to drop it again.

            His eyes are wide with the unbearable lucidity of someone who knows he has died forty thousand times today.

            If the loop expands by another three meters, it will engulf the main power conduit.
        """.trimIndent(),
        anomalyEncountered = AnomalyType.TEMPORAL_LOOP,
        choices = listOf(
            Choice(
                id = "c_01_purge_loop",
                text = "Initiate emergency containment purge. Obliterate the loop and the room.",
                targetSceneId = "scene_01_echo",
                tyrantIndexDelta = 15,
                stabilityDelta = 10,
                selfTrustDelta = -15,
                foresightHint = "Instant stability. The technician's existence will be erased from history."
            ),
            Choice(
                id = "c_01_enter_loop",
                text = "Step into the radius with the Chrono Blade to manually de-phase the technician.",
                targetSceneId = "scene_01_echo",
                tyrantIndexDelta = -5,
                stabilityDelta = -15,
                selfTrustDelta = 15,
                requiredTool = ToolType.CHRONO_BLADE,
                foresightHint = "Spares the man, but your own timeline stability will suffer acute decay."
            )
        )
    )

    private val scene01Echo = Scene(
        id = "scene_01_echo",
        title = "The Transmission from the Past",
        backgroundDrawableName = "scene_1_sky",
        prose = """
            As the chamber settles, the emergency comm terminal on your console sparks to life.

            The frequency is impossible: an encrypted channel from the deepest subterranean bunker, carrying a timestamp from seven years ago.

            A voice breathes through the static. It is your own voice, younger, trembling with desperate hope:

            'Command... is anyone receiving this? We're standing at the threshold of the Aion Gate. Everyone says we shouldn't open it. But if we don't, the city starves. If you're out there... tell me what to do.'
        """.trimIndent(),
        choices = listOf(
            Choice(
                id = "c_01_command_past",
                text = "Command them with absolute coldness: 'Walk away. If you touch that gate, you become me.'",
                targetSceneId = "scene_01_threshold",
                tyrantIndexDelta = 15,
                stabilityDelta = 5,
                selfTrustDelta = -15,
                foresightHint = "Your past self will resent the tyranny, sowing seeds of defiance in Act II."
            ),
            Choice(
                id = "c_01_comfort_past",
                text = "Speak with raw honesty: 'I remember this night. You will make the choice anyway. Forgive yourself.'",
                targetSceneId = "scene_01_threshold",
                tyrantIndexDelta = -10,
                stabilityDelta = -10,
                selfTrustDelta = 20,
                foresightHint = "Reinforces self-trust, but leaves the timeline volatile and uncertain."
            )
        )
    )

    private val scene01Threshold = Scene(
        id = "scene_01_threshold",
        title = "The Threshold of Intervention",
        backgroundDrawableName = "scene_1_sky",
        prose = """
            The static fades. The past transmission terminates.

            On your primary monitor, the timeline registers the causal divergence. The future has not been saved, but the conduit is now open.

            Soon, the perspective will shift. You will not stand here as the ruler at the end of time. You will awaken as the idealist who believed the world could be saved without cost.

            The paradox log is open. The choices are recorded.
        """.trimIndent(),
        isActFinale = true,
        choices = listOf(
            Choice(
                id = "c_01_finish_act",
                text = "Record final log entry and seal the timeline state.",
                targetSceneId = "scene_01_sky",
                tyrantIndexDelta = 0,
                stabilityDelta = 0,
                selfTrustDelta = 0
            )
        )
    )
}
