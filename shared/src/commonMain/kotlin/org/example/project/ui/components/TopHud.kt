package org.example.project.ui.components

import androidx.compose.animation.animateColorAsState
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import org.example.project.core.GameState
import org.example.project.core.enums.Perspective
import org.example.project.ui.theme.*

@Composable
fun TopHud(
    state: GameState,
    modifier: Modifier = Modifier
) {
    val perspectiveAccent = if (state.currentPerspective == Perspective.FUTURE_SELF) {
        FutureSelfAccent
    } else {
        PastSelfAccent
    }

    val stabilityColor by animateColorAsState(
        when {
            state.timelineStability >= 60 -> StabilityGreen
            state.timelineStability >= 30 -> StabilityWarning
            else -> StabilityCritical
        }
    )

    Column(
        modifier = modifier
            .fillMaxWidth()
            .background(SurfaceDark.copy(alpha = 0.92f))
            .padding(horizontal = 16.dp, vertical = 10.dp)
    ) {
        // Top row: Act & Perspective
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = "ACT ${state.currentActNumber}",
                style = MaterialTheme.typography.labelLarge,
                color = TextSecondary,
                letterSpacing = 2.sp
            )

            Box(
                modifier = Modifier
                    .clip(RoundedCornerShape(4.dp))
                    .background(perspectiveAccent.copy(alpha = 0.15f))
                    .border(1.dp, perspectiveAccent.copy(alpha = 0.5f), RoundedCornerShape(4.dp))
                    .padding(horizontal = 8.dp, vertical = 2.dp)
            ) {
                Text(
                    text = state.currentPerspective.displayName.uppercase(),
                    style = MaterialTheme.typography.labelLarge.copy(fontSize = 11.sp),
                    color = perspectiveAccent,
                    fontWeight = FontWeight.Bold,
                    letterSpacing = 1.sp
                )
            }
        }

        Spacer(modifier = Modifier.height(8.dp))

        // Stability Bar
        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Text(
                text = "TIMELINE STABILITY",
                style = MaterialTheme.typography.bodyMedium.copy(fontSize = 11.sp),
                color = TextSystem,
                letterSpacing = 1.sp
            )
            Text(
                text = "${state.timelineStability}%",
                style = MaterialTheme.typography.labelLarge.copy(fontSize = 12.sp),
                color = stabilityColor,
                fontWeight = FontWeight.Bold
            )
        }

        Spacer(modifier = Modifier.height(4.dp))

        LinearProgressIndicator(
            progress = { state.timelineStability / 100f },
            modifier = Modifier
                .fillMaxWidth()
                .height(4.dp)
                .clip(RoundedCornerShape(2.dp)),
            color = stabilityColor,
            trackColor = SurfaceHighlight
        )

        Spacer(modifier = Modifier.height(8.dp))

        // Secondary Stats: Tyrant Index & Self-Trust
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Text(
                text = "TYRANT: ${state.tyrantIndex}%",
                style = MaterialTheme.typography.bodyMedium.copy(fontSize = 11.sp),
                color = if (state.tyrantIndex >= 70) ErrorRed else TextSecondary
            )
            Text(
                text = "SELF-TRUST: ${state.selfTrust}%",
                style = MaterialTheme.typography.bodyMedium.copy(fontSize = 11.sp),
                color = if (state.selfTrust <= 30) ErrorRed else TextSecondary
            )
        }
    }
}
