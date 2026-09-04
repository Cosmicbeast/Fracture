package org.example.project

import androidx.compose.runtime.Composable
import org.example.project.ui.GameScreen
import org.example.project.ui.theme.FractureTheme

@Composable
fun App() {
    FractureTheme {
        GameScreen()
    }
}