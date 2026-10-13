package com.paradoxlog.app.feature.story

import com.paradoxlog.app.domain.model.Dialogue
import org.junit.Assert.assertEquals
import org.junit.Assert.assertFalse
import org.junit.Assert.assertNull
import org.junit.Assert.assertTrue
import org.junit.Test

class StoryScreenStateTest {

    @Test
    fun `test StoryScreen uiState contains valid act and scene presentation values`() {
        val state = GameUiState(
            isLoading = false,
            actId = "act-01",
            actTitle = "Act I: Threshold",
            sceneId = "scene-01",
            sceneTitle = "The Archive Room",
            dialogues = listOf(
                Dialogue("d-1", "PROTAGONIST", "The terminal hums with residual power."),
                Dialogue("d-2", "NARRATOR", "Dust settles across the cracked glass.")
            ),
            choices = listOf(
                ChoiceUi("c-1", "Inspect terminal power cell", isAvailable = true),
                ChoiceUi("c-2", "Bypass temporal lock", isAvailable = false)
            )
        )

        assertFalse(state.isLoading)
        assertEquals("Act I: Threshold", state.actTitle)
        assertEquals("The Archive Room", state.sceneTitle)
        assertEquals(2, state.dialogues.size)
        assertEquals(2, state.choices.size)

        // Verify choices presentation projection
        val availableChoice = state.choices[0]
        val lockedChoice = state.choices[1]
        assertTrue(availableChoice.isAvailable)
        assertFalse(lockedChoice.isAvailable)
    }

    @Test
    fun `test loading state correctly reflects ongoing content fetch`() {
        val loadingState = GameUiState(isLoading = true)
        assertTrue(loadingState.isLoading)
        assertTrue(loadingState.dialogues.isEmpty())
        assertTrue(loadingState.choices.isEmpty())
    }

    @Test
    fun `test error state encapsulates errorMessage without corrupting other presentation fields`() {
        val errorState = GameUiState(
            isLoading = false,
            actTitle = "Act I",
            sceneTitle = "Scene 1",
            errorMessage = "Failed to load narrative asset"
        )

        assertEquals("Failed to load narrative asset", errorState.errorMessage)
        assertEquals("Act I", errorState.actTitle)
    }

    @Test
    fun `test dialogue speaker distinction logic`() {
        val characterDialogue = Dialogue("d-1", "THE STRANGER", "You're not supposed to remember me.")
        val narratorDialogue = Dialogue("d-2", "NARRATOR", "A chill passes through the chamber.")
        val emptySpeakerDialogue = Dialogue("d-3", "", "Footsteps echo.")

        assertTrue(characterDialogue.speaker.isNotBlank() && !characterDialogue.speaker.equals("NARRATOR", ignoreCase = true))
        assertFalse(narratorDialogue.speaker.isNotBlank() && !narratorDialogue.speaker.equals("NARRATOR", ignoreCase = true))
        assertFalse(emptySpeakerDialogue.speaker.isNotBlank() && !emptySpeakerDialogue.speaker.equals("NARRATOR", ignoreCase = true))
    }

    @Test
    fun `test missing act and scene metadata does not invent narrative fallback text`() {
        val blankState = GameUiState(
            actTitle = "",
            sceneTitle = "",
            dialogues = emptyList()
        )

        assertTrue(blankState.actTitle.isEmpty())
        assertTrue(blankState.sceneTitle.isEmpty())
        assertTrue(blankState.dialogues.isEmpty())
    }
}
