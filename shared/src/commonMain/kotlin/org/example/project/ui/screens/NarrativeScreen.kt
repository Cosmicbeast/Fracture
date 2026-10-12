package org.example.project.ui.screens

import androidx.compose.animation.*
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import fracture_thetyrantparadox.shared.generated.resources.Res
import fracture_thetyrantparadox.shared.generated.resources.scene_1_sky
import org.example.project.core.Choice
import org.example.project.core.GameState
import org.example.project.engine.ActManager
import org.example.project.engine.ChoiceResolver
import org.example.project.engine.DialogueEngine
import org.example.project.save.SaveManager
import org.example.project.ui.components.*
import org.example.project.ui.theme.*
import org.jetbrains.compose.resources.painterResource

@Composable
fun NarrativeScreen(
    initialState: GameState,
    onBackToTitle: () -> Unit,
    modifier: Modifier = Modifier
) {
    var gameState by remember { mutableStateOf(initialState) }
    var activeTab by remember { mutableStateOf(BottomTab.STORY) }

    val currentScene = remember(gameState.currentSceneId) {
        ActManager.getCurrentScene(gameState)
    }

    val narrativeProse = remember(currentScene, gameState) {
        DialogueEngine.renderSceneProse(currentScene, gameState)
    }

    var textFinishedRevealing by remember(currentScene.id) { mutableStateOf(false) }
    val scrollState = rememberScrollState()

    // Auto-save on entering new scene
    LaunchedEffect(currentScene.id) {
        SaveManager.autoSave(gameState, currentScene.title)
    }

    Box(
        modifier = modifier
            .fillMaxSize()
            .background(BackgroundDark)
    ) {
        // Atmospheric Backdrop Image
        Image(
            painter = painterResource(Res.drawable.scene_1_sky),
            contentDescription = "Atmospheric Backdrop",
            modifier = Modifier.fillMaxSize(),
            contentScale = ContentScale.Crop
        )

        // Dark Atmospheric Scrim Overlay
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(
                    Brush.verticalGradient(
                        colors = listOf(
                            BackgroundDark.copy(alpha = 0.85f),
                            BackgroundDark.copy(alpha = 0.90f),
                            BackgroundDark.copy(alpha = 0.95f)
                        )
                    )
                )
        )

        // Main Gameplay Column
        Column(
            modifier = Modifier.fillMaxSize()
        ) {
            // Persistent Top HUD
            TopHud(state = gameState)

            // Scrollable Story Content Area
            Box(
                modifier = Modifier
                    .weight(1f)
                    .fillMaxWidth()
                    .clickable {
                        // Tapping anywhere completes the typewriter reveal instantly
                        if (!textFinishedRevealing) {
                            textFinishedRevealing = true
                        }
                    }
            ) {
                Column(
                    modifier = Modifier
                        .fillMaxSize()
                        .verticalScroll(scrollState)
                        .padding(horizontal = 20.dp, vertical = 16.dp)
                ) {
                    // Scene Title
                    Text(
                        text = currentScene.title.uppercase(),
                        style = MaterialTheme.typography.titleLarge.copy(fontSize = 20.sp),
                        color = TextPrimary
                    )

                    Spacer(modifier = Modifier.height(16.dp))

                    // Typewriter Narrative Text
                    TypewriterText(
                        text = narrativeProse,
                        typingSpeedMs = 15L,
                        skipAnimation = textFinishedRevealing,
                        onTypingComplete = { textFinishedRevealing = true }
                    )

                    Spacer(modifier = Modifier.height(28.dp))

                    // Choices Section (Revealed once text finishes, or instantly if user tapped)
                    AnimatedVisibility(
                        visible = textFinishedRevealing,
                        enter = fadeIn()
                    ) {
                        ChoiceView(
                            choices = currentScene.choices,
                            state = gameState,
                            onChoiceSelected = { choice ->
                                val nextState = ChoiceResolver.resolve(choice, gameState)
                                gameState = nextState
                                textFinishedRevealing = false
                            }
                        )
                    }

                    Spacer(modifier = Modifier.height(32.dp))
                }
            }

            // Bottom Navigation Bar
            BottomNavBar(
                selectedTab = activeTab,
                onTabSelected = { tab -> activeTab = tab }
            )
        }

        // Modal Sheet Overlays
        when (activeTab) {
            BottomTab.CHARACTER -> {
                CharacterModal(
                    state = gameState,
                    onDismiss = { activeTab = BottomTab.STORY }
                )
            }
            BottomTab.TOOLS -> {
                ToolsModal(
                    state = gameState,
                    onDismiss = { activeTab = BottomTab.STORY }
                )
            }
            BottomTab.LOG -> {
                TimelineLogModal(
                    state = gameState,
                    onDismiss = { activeTab = BottomTab.STORY }
                )
            }
            BottomTab.STORY -> Unit
        }
    }
}
