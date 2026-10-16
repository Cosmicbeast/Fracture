package com.paradoxlog.app.feature.timeline

import android.content.res.Configuration
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.defaultMinSize
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.safeDrawingPadding
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.semantics.Role
import androidx.compose.ui.semantics.role
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
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

/**
 * Minimalist Timeline Change presentation canvas.
 *
 * Implements the Timeline Change stage of the canonical loop:
 * Story -> Choice -> Consequence -> Timeline Change -> Exploration -> Encounter -> Story
 *
 * Serves as a brief temporal state-change confirmation presenting only
 * authoritative timeline log data.
 */
@Composable
fun TimelineChangeView(
    timelineChange: TimelineChangeUiState,
    onContinue: () -> Unit,
    modifier: Modifier = Modifier
) {
    val interactionSource = remember { MutableInteractionSource() }

    val configuration = LocalConfiguration.current
    val isLandscape = configuration.orientation == Configuration.ORIENTATION_LANDSCAPE

    Surface(
        color = ParadoxColorBackground,
        modifier = modifier.fillMaxSize()
    ) {
        Box(
            modifier = Modifier
                .fillMaxSize()
                .safeDrawingPadding(),
            contentAlignment = Alignment.Center
        ) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .widthIn(max = 620.dp)
                    .padding(
                        horizontal = if (isLandscape) 32.dp else 24.dp,
                        vertical = if (isLandscape) 16.dp else 28.dp
                    )
                    .verticalScroll(rememberScrollState()),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Center
            ) {
                Spacer(modifier = Modifier.height(if (isLandscape) 8.dp else 20.dp))

                // 1. Timeline Change Header
                Text(
                    text = UiLabels.LABEL_TIMELINE_UPDATED,
                    color = ParadoxColorStabilityCyan,
                    fontSize = if (isLandscape) 12.sp else 13.sp,
                    fontWeight = FontWeight.SemiBold,
                    fontFamily = FontFamily.SansSerif,
                    letterSpacing = 2.sp,
                    textAlign = TextAlign.Center
                )

                Spacer(modifier = Modifier.height(if (isLandscape) 16.dp else 28.dp))

                // 2. Authoritative Timeline Information Block
                Column(
                    modifier = Modifier
                        .fillMaxWidth(if (isLandscape) 0.92f else 0.85f)
                        .padding(vertical = 8.dp),
                    verticalArrangement = Arrangement.spacedBy(if (isLandscape) 10.dp else 16.dp)
                ) {
                    if (timelineChange.actId.isNotBlank()) {
                        TimelineInfoRow(
                            label = UiLabels.LABEL_CURRENT_ACT,
                            value = timelineChange.actId.uppercase()
                        )
                    }

                    if (timelineChange.sceneId.isNotBlank()) {
                        TimelineInfoRow(
                            label = UiLabels.LABEL_CURRENT_SCENE,
                            value = timelineChange.sceneId.uppercase()
                        )
                    }

                    if (!timelineChange.recordedDecisionChoiceId.isNullOrBlank()) {
                        TimelineInfoRow(
                            label = UiLabels.LABEL_DECISION_RECORDED,
                            value = timelineChange.recordedDecisionChoiceId
                        )
                    }

                    if (timelineChange.totalVisitedScenesCount > 0) {
                        TimelineInfoRow(
                            label = UiLabels.LABEL_TIMELINE_NODES,
                            value = "${timelineChange.totalVisitedScenesCount}"
                        )
                    }

                    if (timelineChange.timelineStability != null) {
                        TimelineInfoRow(
                            label = UiLabels.LABEL_STABILITY,
                            value = "${timelineChange.timelineStability}%"
                        )
                    }
                }

                Spacer(modifier = Modifier.height(if (isLandscape) 16.dp else 28.dp))

                // 3. Continue Action Button
                Surface(
                    shape = RoundedCornerShape(8.dp),
                    color = ParadoxColorSurface,
                    border = BorderStroke(1.dp, ParadoxColorBorder),
                    modifier = Modifier
                        .fillMaxWidth()
                        .widthIn(max = 380.dp)
                        .defaultMinSize(minHeight = 52.dp)
                        .semantics { role = Role.Button }
                        .clickable(
                            interactionSource = interactionSource,
                            indication = null,
                            onClick = onContinue
                        )
                ) {
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(vertical = 14.dp),
                        contentAlignment = Alignment.Center
                    ) {
                        Text(
                            text = UiLabels.LABEL_CONTINUE,
                            style = ParadoxTypography.labelLarge,
                            color = ParadoxColorTextPrimary,
                            letterSpacing = 1.2.sp
                        )
                    }
                }

                Spacer(modifier = Modifier.height(if (isLandscape) 8.dp else 16.dp))
            }
        }
    }
}

@Composable
private fun TimelineInfoRow(
    label: String,
    value: String,
    modifier: Modifier = Modifier
) {
    Row(
        modifier = modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
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
