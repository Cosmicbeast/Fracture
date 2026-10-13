package com.paradoxlog.app.feature.consequence

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
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
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
import com.paradoxlog.app.core.designsystem.ParadoxColorStabilityAmber
import com.paradoxlog.app.core.designsystem.ParadoxColorStabilityCyan
import com.paradoxlog.app.core.designsystem.ParadoxColorSurface
import com.paradoxlog.app.core.designsystem.ParadoxColorTextPrimary
import com.paradoxlog.app.core.designsystem.ParadoxColorTextSecondary
import com.paradoxlog.app.core.designsystem.ParadoxColorTyrantAccent
import com.paradoxlog.app.core.designsystem.ParadoxTypography

/**
 * Consequence presentation canvas.
 *
 * Implements panel "05. Choice Consequence" from the canonical screen flow:
 * Story -> Choice -> Consequence -> Timeline Change -> Exploration -> Encounter -> Story
 *
 * ARCHITECTURAL CONSTRAINTS:
 * 1. Strictly renders pre-calculated authoritative consequence projection data.
 * 2. Does NOT calculate variable changes, clamp values, or determine branches in Compose.
 * 3. Only displays non-zero stat shifts and changes present in ConsequenceUiState.
 * 4. Dispatches onContinue() to allow the authoritative game engine pipeline to determine the next stage.
 */
@Composable
fun ConsequenceView(
    consequence: ConsequenceUiState,
    onContinue: () -> Unit,
    modifier: Modifier = Modifier
) {
    val interactionSource = remember { MutableInteractionSource() }

    Surface(
        color = ParadoxColorBackground,
        modifier = modifier.fillMaxSize()
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(horizontal = 24.dp, vertical = 32.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            Spacer(modifier = Modifier.weight(1f))

            // 1. Consequence Header Status
            Text(
                text = UiLabels.LABEL_TIMELINE_SHIFTED,
                color = ParadoxColorStabilityCyan,
                fontSize = 13.sp,
                fontWeight = FontWeight.SemiBold,
                fontFamily = FontFamily.SansSerif,
                letterSpacing = 2.sp,
                textAlign = TextAlign.Center
            )

            Spacer(modifier = Modifier.height(20.dp))

            // 2. Choice / Context Narrative Feedback
            if (consequence.choiceText.isNotBlank()) {
                Text(
                    text = consequence.choiceText,
                    style = ParadoxTypography.bodyLarge,
                    color = ParadoxColorTextPrimary,
                    textAlign = TextAlign.Center,
                    lineHeight = 28.sp,
                    modifier = Modifier.fillMaxWidth()
                )

                Spacer(modifier = Modifier.height(32.dp))
            }

            // 3. Stat Deltas (Rendered ONLY if non-zero shifts exist)
            if (consequence.statDeltas.isNotEmpty() || consequence.itemsAdded.isNotEmpty() || consequence.itemsRemoved.isNotEmpty()) {
                Column(
                    modifier = Modifier
                        .fillMaxWidth(0.85f)
                        .padding(vertical = 12.dp),
                    verticalArrangement = Arrangement.spacedBy(16.dp)
                ) {
                    consequence.statDeltas.forEach { deltaUi ->
                        StatDeltaRow(delta = deltaUi)
                    }

                    consequence.itemsAdded.forEach { itemId ->
                        ItemDeltaRow(label = "+ Item", itemName = itemId, isAdded = true)
                    }

                    consequence.itemsRemoved.forEach { itemId ->
                        ItemDeltaRow(label = "- Item", itemName = itemId, isAdded = false)
                    }
                }

                Spacer(modifier = Modifier.height(24.dp))
            }

            Spacer(modifier = Modifier.weight(1f))

            // 4. Continue Action Button
            Surface(
                shape = RoundedCornerShape(8.dp),
                color = ParadoxColorSurface,
                border = BorderStroke(1.dp, ParadoxColorBorder),
                modifier = Modifier
                    .fillMaxWidth()
                    .defaultMinSize(minHeight = 56.dp)
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
                        .padding(vertical = 16.dp),
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
        }
    }
}

@Composable
private fun StatDeltaRow(
    delta: StatDeltaUi,
    modifier: Modifier = Modifier
) {
    val deltaFormatted = if (delta.delta > 0) "+${delta.delta}" else "${delta.delta}"
    val deltaColor = when {
        delta.delta < 0 -> ParadoxColorStabilityAmber
        delta.statName.equals(UiLabels.LABEL_TYRANT_INDEX, ignoreCase = true) -> ParadoxColorTyrantAccent
        else -> ParadoxColorStabilityCyan
    }

    Row(
        modifier = modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(
            text = delta.statName,
            style = ParadoxTypography.bodyMedium,
            color = ParadoxColorTextSecondary
        )

        Text(
            text = deltaFormatted,
            style = ParadoxTypography.bodyMedium,
            fontWeight = FontWeight.SemiBold,
            color = deltaColor
        )
    }
}

@Composable
private fun ItemDeltaRow(
    label: String,
    itemName: String,
    isAdded: Boolean,
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
            text = itemName,
            style = ParadoxTypography.bodyMedium,
            color = if (isAdded) ParadoxColorStabilityCyan else ParadoxColorTextSecondary
        )
    }
}
