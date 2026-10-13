package com.paradoxlog.app.feature.timeline

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.paradoxlog.app.core.constants.UiLabels
import com.paradoxlog.app.core.designsystem.ParadoxColorBackground
import com.paradoxlog.app.core.designsystem.ParadoxColorBorder
import com.paradoxlog.app.core.designsystem.ParadoxColorStabilityCyan
import com.paradoxlog.app.core.designsystem.ParadoxColorSurface
import com.paradoxlog.app.core.designsystem.ParadoxColorTextPrimary
import com.paradoxlog.app.core.designsystem.ParadoxColorTextSecondary
import com.paradoxlog.app.core.designsystem.ParadoxTypography
import com.paradoxlog.app.feature.story.GameViewModel

/**
 * Paradox Log / Timeline Status Screen (Panels 09 & 10).
 *
 * Displays live authoritative variables (Stability, Tyrant Index, Self-Trust),
 * visited scenes / decisions log, and back navigation.
 */
@Composable
fun TimelineScreen(
    viewModel: GameViewModel,
    onNavigateBack: () -> Unit,
    modifier: Modifier = Modifier
) {
    val uiState by viewModel.uiState.collectAsState()

    Surface(
        color = ParadoxColorBackground,
        modifier = modifier
            .fillMaxSize()
            .statusBarsPadding()
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(horizontal = 24.dp, vertical = 24.dp)
                .verticalScroll(rememberScrollState()),
            verticalArrangement = Arrangement.spacedBy(28.dp)
        ) {
            // Header with Back Action
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Text(
                    text = UiLabels.LABEL_PARADOX_LOG,
                    style = ParadoxTypography.headlineMedium,
                    color = ParadoxColorTextPrimary,
                    letterSpacing = 2.sp
                )

                Text(
                    text = "BACK",
                    style = ParadoxTypography.labelMedium,
                    color = ParadoxColorStabilityCyan,
                    modifier = Modifier
                        .clickable(onClick = onNavigateBack)
                        .padding(8.dp)
                )
            }

            Spacer(modifier = Modifier.height(4.dp))

            // 1. Authoritative Variables Section
            Text(
                text = "TIMELINE STATUS",
                style = ParadoxTypography.labelLarge,
                color = ParadoxColorStabilityCyan,
                letterSpacing = 2.sp
            )

            StatProgressBar(
                label = UiLabels.LABEL_STABILITY.uppercase(),
                value = uiState.timelineStability,
                max = 100
            )

            StatProgressBar(
                label = UiLabels.LABEL_TYRANT_INDEX.uppercase(),
                value = uiState.tyrantIndex,
                max = 100
            )

            StatProgressBar(
                label = UiLabels.LABEL_SELF_TRUST.uppercase(),
                value = uiState.selfTrust,
                max = 100
            )

            Spacer(modifier = Modifier.height(8.dp))

            // 2. Active Timeline Node Information
            Text(
                text = "RECORDED CHRONOLOGY",
                style = ParadoxTypography.labelLarge,
                color = ParadoxColorStabilityCyan,
                letterSpacing = 2.sp
            )

            Surface(
                shape = RoundedCornerShape(8.dp),
                color = ParadoxColorSurface,
                border = BorderStroke(1.dp, ParadoxColorBorder),
                modifier = Modifier.fillMaxWidth()
            ) {
                Column(
                    modifier = Modifier.padding(16.dp),
                    verticalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    ChronologyRow("Current Act", uiState.actTitle.ifBlank { "None" })
                    ChronologyRow("Active Scene", uiState.sceneTitle.ifBlank { "None" })
                    ChronologyRow("Acquired Relics", "${uiState.inventoryCount}")
                }
            }
        }
    }
}

@Composable
private fun StatProgressBar(
    label: String,
    value: Int,
    max: Int,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier.fillMaxWidth(),
        verticalArrangement = Arrangement.spacedBy(6.dp)
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Text(
                text = label,
                style = ParadoxTypography.bodyMedium,
                color = ParadoxColorTextSecondary
            )

            Text(
                text = "$value%",
                style = ParadoxTypography.bodyMedium,
                fontWeight = FontWeight.Medium,
                color = ParadoxColorTextPrimary
            )
        }

        LinearProgressIndicator(
            progress = (value.toFloat() / max).coerceIn(0f, 1f),
            color = ParadoxColorStabilityCyan,
            trackColor = ParadoxColorSurface,
            modifier = Modifier
                .fillMaxWidth()
                .height(6.dp)
        )
    }
}

@Composable
private fun ChronologyRow(
    label: String,
    value: String,
    modifier: Modifier = Modifier
) {
    Row(
        modifier = modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Text(
            text = label,
            style = ParadoxTypography.bodyMedium,
            color = ParadoxColorTextSecondary
        )

        Text(
            text = value,
            style = ParadoxTypography.bodyMedium,
            fontWeight = FontWeight.Medium,
            color = ParadoxColorTextPrimary
        )
    }
}
