package com.paradoxlog.app.feature.choice

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.paradoxlog.app.core.designsystem.ParadoxColorStabilityCyan
import com.paradoxlog.app.feature.story.ChoiceUi

/**
 * Canonical ChoiceComponent presentation module.
 *
 * Placed within the canonical gameplay flow:
 * Story -> Choice -> Consequence -> Timeline Change -> Exploration -> Encounter -> Story
 *
 * RESPONSIBILITY:
 * - Responsible ONLY for the Choice selection stage.
 * - Emits `choiceId` when the player selects an available option.
 * - Does NOT perform condition checks, consequence math, or timeline mutations.
 * - Does NOT predict or shortcut to subsequent stages (Consequence, Timeline, Exploration).
 */
@Composable
fun ChoiceComponent(
    choices: List<ChoiceUi>,
    onChoiceSelected: (String) -> Unit,
    modifier: Modifier = Modifier,
    promptText: String? = null
) {
    if (choices.isEmpty()) return

    Column(
        modifier = modifier
            .fillMaxWidth()
            .padding(horizontal = 24.dp),
        verticalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        if (!promptText.isNullOrBlank()) {
            Text(
                text = promptText.uppercase(),
                color = ParadoxColorStabilityCyan,
                fontSize = 11.sp,
                fontWeight = FontWeight.SemiBold,
                fontFamily = FontFamily.SansSerif,
                letterSpacing = 1.5.sp,
                modifier = Modifier.padding(bottom = 4.dp)
            )
        }

        choices.forEach { choice ->
            ChoiceItem(
                choiceId = choice.id,
                text = choice.text,
                isAvailable = choice.isAvailable,
                onChoiceSelected = onChoiceSelected
            )
        }
    }
}
