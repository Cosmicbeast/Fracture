package org.example.project

import androidx.compose.runtime.*
import org.example.project.core.GameState
import org.example.project.save.SaveManager
import org.example.project.ui.screens.NarrativeScreen
import org.example.project.ui.screens.TitleScreen
import org.example.project.ui.theme.FractureTheme

enum class ScreenState {
    TITLE,
    NARRATIVE
}

@Composable
fun App() {
    var currentScreen by remember { mutableStateOf(ScreenState.TITLE) }
    var activeGameState by remember { mutableStateOf(GameState.initial()) }
    var hasSaveGame by remember { mutableStateOf(SaveManager.hasAutoSave()) }

    FractureTheme {
        when (currentScreen) {
            ScreenState.TITLE -> {
                TitleScreen(
                    hasSaveGame = hasSaveGame,
                    onContinue = {
                        val loadedState = SaveManager.loadAutoSave() ?: GameState.initial()
                        activeGameState = loadedState
                        currentScreen = ScreenState.NARRATIVE
                    },
                    onNewGame = {
                        activeGameState = GameState.initial()
                        currentScreen = ScreenState.NARRATIVE
                    }
                )
            }
            ScreenState.NARRATIVE -> {
                NarrativeScreen(
                    initialState = activeGameState,
                    onBackToTitle = {
                        hasSaveGame = SaveManager.hasAutoSave()
                        currentScreen = ScreenState.TITLE
                    }
                )
            }
        }
    }
}