package com.paradoxlog.app.feature.story.components

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Box
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
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.paradoxlog.app.core.designsystem.ParadoxColorBorder
import com.paradoxlog.app.core.designsystem.ParadoxColorStabilityCyan
import com.paradoxlog.app.core.designsystem.ParadoxColorSurface
import com.paradoxlog.app.core.designsystem.ParadoxColorTextMuted
import com.paradoxlog.app.core.designsystem.ParadoxColorTextPrimary
import com.paradoxlog.app.core.designsystem.ParadoxTypography

/**
 * Individual interactive decision item.
 *
 * Implements the sleek choice container design from the reference image:
 * - Restrained rounded rectangle (8dp radius)
 * - Minimalist dark surface with subtle hairline outline
 * - High contrast text with generous touch targets (min 56dp height)
 * - Non-garish disabled treatment when conditions are unmet
 */
@Composable
fun ChoiceItem(
    text: String,
    isAvailable: Boolean,
    onClick: () -> Unit,
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
                        indication = null, // Restrained touch feedback without garish ripples
                        onClick = onClick
                    )
                } else {
                    Modifier
                }
            )
    ) {
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 20.dp, vertical = 16.dp),
            contentAlignment = Alignment.CenterStart
        ) {
            Text(
                text = text,
                style = ParadoxTypography.labelLarge,
                color = if (isAvailable) ParadoxColorTextPrimary else ParadoxColorTextMuted,
                textAlign = TextAlign.Start
            )
        }
    }
}
