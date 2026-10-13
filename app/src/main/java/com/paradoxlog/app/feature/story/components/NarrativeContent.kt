package com.paradoxlog.app.feature.story.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.paradoxlog.app.core.designsystem.ParadoxColorStabilityCyan
import com.paradoxlog.app.core.designsystem.ParadoxColorTextPrimary
import com.paradoxlog.app.core.designsystem.ParadoxColorTextSecondary
import com.paradoxlog.app.core.designsystem.ParadoxTypography
import com.paradoxlog.app.domain.model.Dialogue

/**
 * Pure presentation component rendering the active narrative exposition and dialogue passages.
 *
 * Implements typography-first design principles from docs/design/design-system.md:
 * - High-contrast off-white text against obsidian canvas
 * - Generous 1.6x line-height ratio for eye relaxation and reading immersion
 * - Distinct, subtle speaker headers without bulky speech bubbles or comic frames
 */
@Composable
fun NarrativeContent(
    dialogues: List<Dialogue>,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier
            .fillMaxWidth()
            .padding(horizontal = 24.dp),
        verticalArrangement = Arrangement.spacedBy(20.dp)
    ) {
        dialogues.forEach { dialogue ->
            DialogueBlock(dialogue = dialogue)
        }
    }
}

@Composable
private fun DialogueBlock(
    dialogue: Dialogue,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier
            .fillMaxWidth()
            .semantics(mergeDescendants = true) {}
    ) {
        val hasDistinctSpeaker = dialogue.speaker.isNotBlank() &&
                !dialogue.speaker.equals("NARRATOR", ignoreCase = true)

        if (hasDistinctSpeaker) {
            Text(
                text = dialogue.speaker.uppercase(),
                color = ParadoxColorStabilityCyan,
                fontSize = 11.sp,
                fontWeight = FontWeight.SemiBold,
                fontFamily = FontFamily.SansSerif,
                letterSpacing = 1.8.sp,
                modifier = Modifier.padding(bottom = 6.dp)
            )
        }

        Text(
            text = dialogue.text,
            style = if (hasDistinctSpeaker) ParadoxTypography.bodyMedium else ParadoxTypography.bodyLarge,
            color = ParadoxColorTextPrimary,
            lineHeight = 28.sp
        )
    }
}
