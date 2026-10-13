package com.paradoxlog.app.feature.choice

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.defaultMinSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.semantics.Role
import androidx.compose.ui.semantics.disabled
import androidx.compose.ui.semantics.role
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.paradoxlog.app.core.designsystem.ParadoxColorBorder
import com.paradoxlog.app.core.designsystem.ParadoxColorSurface
import com.paradoxlog.app.core.designsystem.ParadoxColorTextMuted
import com.paradoxlog.app.core.designsystem.ParadoxColorTextPrimary
import com.paradoxlog.app.core.designsystem.ParadoxTypography

/**
 * Canonical ChoiceItem presentation component.
 *
 * ARCHITECTURAL CONSTRAINTS:
 * 1. Responsible ONLY for rendering a discrete choice and emitting its `choiceId`.
 * 2. Does NOT evaluate conditions, calculate consequences, or alter timeline state.
 * 3. Does NOT predict the outcome or directly navigate to next stages.
 * 4. Precondition availability is supplied exclusively from ChoiceUi.isAvailable.
 */
@Composable
fun ChoiceItem(
    choiceId: String,
    text: String,
    isAvailable: Boolean,
    onChoiceSelected: (String) -> Unit,
    modifier: Modifier = Modifier
) {
    val interactionSource = remember { MutableInteractionSource() }

    val borderColor = if (isAvailable) {
        ParadoxColorBorder
    } else {
        ParadoxColorBorder.copy(alpha = 0.4f)
    }

    val contentAlpha = if (isAvailable) 1.0f else 0.45f

    Surface(
        shape = RoundedCornerShape(8.dp),
        color = ParadoxColorSurface,
        border = BorderStroke(1.dp, borderColor),
        modifier = modifier
            .fillMaxWidth()
            .defaultMinSize(minHeight = 56.dp)
            .alpha(contentAlpha)
            .semantics {
                role = Role.Button
                if (!isAvailable) {
                    disabled()
                }
            }
            .then(
                if (isAvailable) {
                    Modifier.clickable(
                        interactionSource = interactionSource,
                        indication = null, // Restrained touch interaction without neon ripples
                        onClick = { onChoiceSelected(choiceId) }
                    )
                } else {
                    Modifier
                }
            )
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 20.dp, vertical = 16.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Text(
                text = text,
                style = ParadoxTypography.labelLarge,
                color = if (isAvailable) ParadoxColorTextPrimary else ParadoxColorTextMuted,
                textAlign = TextAlign.Start,
                modifier = Modifier.weight(1f, fill = false)
            )

            if (!isAvailable) {
                Text(
                    text = com.paradoxlog.app.core.constants.UiLabels.LABEL_LOCKED,
                    fontSize = 10.sp,
                    fontWeight = FontWeight.SemiBold,
                    fontFamily = FontFamily.SansSerif,
                    letterSpacing = 1.2.sp,
                    color = ParadoxColorTextMuted,
                    modifier = Modifier.padding(start = 12.dp)
                )
            }
        }
    }
}
