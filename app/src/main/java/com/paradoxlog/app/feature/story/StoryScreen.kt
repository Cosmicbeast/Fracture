package com.paradoxlog.app.feature.story

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.paradoxlog.app.core.designsystem.ParadoxColorBackground
import com.paradoxlog.app.core.designsystem.ParadoxColorStabilityCyan
import com.paradoxlog.app.feature.story.components.ChoiceList
import com.paradoxlog.app.feature.story.components.NarrativeContent
import com.paradoxlog.app.feature.story.components.StoryHeader

/**
 * Primary Story Viewport for Paradox Log.
 *
 * Implements the typography-first, minimalist interactive reading canvas:
 * 1. StoryHeader: Tracked-out Act and Scene context
 * 2. NarrativeContent: Dialogue and prose blocks with generous line heights
 * 3. ChoiceList: Clean, restrained choice decision containers
 *
 * ARCHITECTURAL CONSTRAINTS:
 * - Purely declarative presentation.
 * - Single source of truth: derives strictly from GameViewModel.uiState.
 * - Contains zero gameplay rules, conditions, consequence math, or branching logic.
 */
@Composable
fun StoryScreen(
    viewModel: GameViewModel,
    modifier: Modifier = Modifier
) {
    val uiState by viewModel.uiState.collectAsState()

    StoryScreen(
        uiState = uiState,
        onChoiceSelected = viewModel::selectChoice,
        onAcknowledgeConsequence = viewModel::acknowledgeConsequence,
        onAcknowledgeTimelineChange = viewModel::acknowledgeTimelineChange,
        onDismissError = viewModel::dismissError,
        modifier = modifier
    )
}

/**
 * Stateless StoryScreen overload for preview and isolated testing.
 */
@Composable
fun StoryScreen(
    uiState: GameUiState,
    onChoiceSelected: (String) -> Unit,
    onAcknowledgeConsequence: () -> Unit = {},
    onAcknowledgeTimelineChange: () -> Unit = {},
    onDismissError: () -> Unit = {},
    modifier: Modifier = Modifier
) {
    val scrollState = rememberScrollState()
    val snackbarHostState = remember { SnackbarHostState() }

    LaunchedEffect(uiState.errorMessage) {
        val error = uiState.errorMessage
        if (error != null) {
            snackbarHostState.showSnackbar(message = error)
            onDismissError()
        }
    }

    Box(
        modifier = modifier
            .fillMaxSize()
            .background(ParadoxColorBackground)
            .statusBarsPadding()
    ) {
        if (uiState.isLoading) {
            Box(
                modifier = Modifier.fillMaxSize(),
                contentAlignment = Alignment.Center
            ) {
                CircularProgressIndicator(
                    color = ParadoxColorStabilityCyan,
                    strokeWidth = 2.dp
                )
            }
        } else if (uiState.activeConsequence != null) {
            com.paradoxlog.app.feature.consequence.ConsequenceView(
                consequence = uiState.activeConsequence,
                onContinue = onAcknowledgeConsequence
            )
        } else if (uiState.activeTimelineChange != null) {
            com.paradoxlog.app.feature.timeline.TimelineChangeView(
                timelineChange = uiState.activeTimelineChange,
                onContinue = onAcknowledgeTimelineChange
            )
        } else {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .verticalScroll(scrollState)
            ) {
                // 1. Act and Scene Context Header
                StoryHeader(
                    actTitle = uiState.actTitle,
                    sceneTitle = uiState.sceneTitle
                )

                Spacer(modifier = Modifier.height(16.dp))

                // 2. Prose and Dialogue Passages
                NarrativeContent(
                    dialogues = uiState.dialogues
                )

                Spacer(modifier = Modifier.height(36.dp))

                // 3. Interactive Choice Junctures
                ChoiceList(
                    choices = uiState.choices,
                    onChoiceSelected = onChoiceSelected
                )

                // Bottom padding to ensure comfortable breathing room
                Spacer(modifier = Modifier.height(48.dp))
            }
        }

        SnackbarHost(
            hostState = snackbarHostState,
            modifier = Modifier
                .align(Alignment.BottomCenter)
                .padding(16.dp)
        )
    }
}
