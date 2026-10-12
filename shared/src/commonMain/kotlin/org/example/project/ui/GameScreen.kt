package org.example.project.ui

import androidx.compose.runtime.Composable
import org.example.project.core.GameState
import org.example.project.ui.screens.NarrativeScreen

@Composable
fun GameScreen() {
    NarrativeScreen(
        initialState = GameState.initial(),
        onBackToTitle = {}
    )
}
