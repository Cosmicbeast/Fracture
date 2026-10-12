package org.example.project.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import org.example.project.core.GameState
import org.example.project.engine.TimelineStateTracker
import org.example.project.ui.theme.*

@Composable
fun CharacterModal(
    state: GameState,
    onDismiss: () -> Unit
) {
    val stabilityStatus = TimelineStateTracker.getStabilityStatus(state)
    val tyrantPosture = TimelineStateTracker.getTyrantPosture(state)
    val selfTrustStatus = TimelineStateTracker.getSelfTrustStatus(state)

    BaseModal(
        title = "IDENTITY ARCHIVE",
        subtitle = "Causal Profile & Resonance",
        onDismiss = onDismiss
    ) {
        Column(verticalArrangement = Arrangement.spacedBy(14.dp)) {
            ModalSectionCard(
                title = "CURRENT PERSPECTIVE",
                value = state.currentPerspective.displayName,
                detail = state.currentPerspective.philosophy,
                accentColor = if (state.currentPerspective.name.contains("FUTURE")) FutureSelfAccent else PastSelfAccent
            )

            ModalSectionCard(
                title = "GOVERNING POSTURE",
                value = tyrantPosture.label,
                detail = tyrantPosture.description,
                accentColor = if (state.tyrantIndex >= 70) ErrorRed else TextPrimary
            )

            ModalSectionCard(
                title = "TIMELINE CONTINUUM",
                value = stabilityStatus.label,
                detail = stabilityStatus.description,
                accentColor = when (stabilityStatus) {
                    TimelineStateTracker.StabilityStatus.PRISTINE,
                    TimelineStateTracker.StabilityStatus.STABLE -> StabilityGreen
                    TimelineStateTracker.StabilityStatus.STRAINED -> StabilityWarning
                    else -> StabilityCritical
                }
            )

            ModalSectionCard(
                title = "SELF-ALIGNMENT",
                value = selfTrustStatus.label,
                detail = selfTrustStatus.description,
                accentColor = if (state.selfTrust <= 30) ErrorRed else Accent
            )
        }
    }
}

@Composable
fun ToolsModal(
    state: GameState,
    onDismiss: () -> Unit
) {
    BaseModal(
        title = "TEMPORAL ARTIFACTS",
        subtitle = "Philosophical Instruments of Intervention",
        onDismiss = onDismiss
    ) {
        if (state.activeTools.isEmpty()) {
            Text(
                text = "No temporal tools currently attuned.",
                style = MaterialTheme.typography.bodyLarge,
                color = TextSecondary
            )
        } else {
            Column(verticalArrangement = Arrangement.spacedBy(12.dp)) {
                state.activeTools.forEach { tool ->
                    ModalSectionCard(
                        title = "${tool.displayName.uppercase()} (${tool.concept.uppercase()})",
                        value = "Attuned",
                        detail = tool.description,
                        accentColor = Accent
                    )
                }
            }
        }
    }
}

@Composable
fun TimelineLogModal(
    state: GameState,
    onDismiss: () -> Unit
) {
    BaseModal(
        title = "CHRONO LOG",
        subtitle = "Causal History & Interventions",
        onDismiss = onDismiss
    ) {
        if (state.decisionHistory.isEmpty()) {
            Text(
                text = "The timeline remains uncommitted. No decisions recorded yet in this cycle.",
                style = MaterialTheme.typography.bodyLarge,
                color = TextSecondary
            )
        } else {
            Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                state.decisionHistory.forEachIndexed { index, decisionId ->
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .clip(RoundedCornerShape(6.dp))
                            .background(SurfaceDark)
                            .border(1.dp, SurfaceHighlight, RoundedCornerShape(6.dp))
                            .padding(12.dp)
                    ) {
                        Text(
                            text = "#${index + 1}: Choice node committed [$decisionId]",
                            style = MaterialTheme.typography.bodyMedium,
                            color = TextSecondary
                        )
                    }
                }
            }
        }
    }
}

@Composable
private fun BaseModal(
    title: String,
    subtitle: String,
    onDismiss: () -> Unit,
    content: @Composable ColumnScope.() -> Unit
) {
    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(BackgroundDark.copy(alpha = 0.85f))
            .padding(top = 40.dp)
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .clip(RoundedCornerShape(topStart = 16.dp, topEnd = 16.dp))
                .background(SurfaceDark)
                .border(1.dp, SurfaceHighlight, RoundedCornerShape(topStart = 16.dp, topEnd = 16.dp))
                .padding(20.dp)
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Column {
                    Text(
                        text = title,
                        style = MaterialTheme.typography.titleLarge,
                        color = TextPrimary
                    )
                    Text(
                        text = subtitle,
                        style = MaterialTheme.typography.bodyMedium,
                        color = TextSystem
                    )
                }

                Button(
                    onClick = onDismiss,
                    colors = ButtonDefaults.buttonColors(containerColor = SurfaceHighlight)
                ) {
                    Text("Close", color = TextPrimary)
                }
            }

            Spacer(modifier = Modifier.height(16.dp))
            HorizontalDivider(color = SurfaceHighlight)
            Spacer(modifier = Modifier.height(16.dp))

            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .verticalScroll(rememberScrollState())
            ) {
                content()
            }
        }
    }
}

@Composable
private fun ModalSectionCard(
    title: String,
    value: String,
    detail: String,
    accentColor: androidx.compose.ui.graphics.Color
) {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(8.dp))
            .background(BackgroundDark)
            .border(1.dp, SurfaceHighlight, RoundedCornerShape(8.dp))
            .padding(14.dp)
    ) {
        Column {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Text(
                    text = title,
                    style = MaterialTheme.typography.bodyMedium.copy(fontSize = 11.sp),
                    color = TextSystem,
                    letterSpacing = 1.sp
                )
                Text(
                    text = value,
                    style = MaterialTheme.typography.labelLarge,
                    color = accentColor,
                    fontWeight = FontWeight.Bold
                )
            }
            Spacer(modifier = Modifier.height(6.dp))
            Text(
                text = detail,
                style = MaterialTheme.typography.bodyLarge.copy(fontSize = 15.sp, lineHeight = 22.sp),
                color = TextSecondary
            )
        }
    }
}
