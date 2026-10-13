package com.paradoxlog.app.feature.story.components

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.paradoxlog.app.core.constants.UiLabels
import com.paradoxlog.app.core.designsystem.ParadoxColorBorder
import com.paradoxlog.app.core.designsystem.ParadoxColorStabilityCyan
import com.paradoxlog.app.core.designsystem.ParadoxColorTextSecondary

/**
 * Minimalist status and narrative context header for the Story Screen.
 *
 * Implements the restrained top metadata line shown in reference:
 * "ACT I | CONTEXT / SCENE"
 *
 * ARCHITECTURAL RULE:
 * Does NOT invent replacement act or scene text. Renders only canonical content.
 */
@Composable
fun StoryHeader(
    actTitle: String,
    sceneTitle: String,
    modifier: Modifier = Modifier
) {
    if (actTitle.isBlank() && sceneTitle.isBlank()) return

    Column(
        modifier = modifier
            .fillMaxWidth()
            .padding(horizontal = 24.dp, vertical = 16.dp)
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically
        ) {
            if (actTitle.isNotBlank()) {
                Text(
                    text = actTitle.uppercase(),
                    color = ParadoxColorStabilityCyan,
                    fontSize = 12.sp,
                    fontWeight = FontWeight.SemiBold,
                    fontFamily = FontFamily.SansSerif,
                    letterSpacing = 2.sp
                )
            }

            if (actTitle.isNotBlank() && sceneTitle.isNotBlank()) {
                Text(
                    text = UiLabels.HEADER_DELIMITER,
                    color = ParadoxColorTextSecondary.copy(alpha = 0.5f),
                    fontSize = 12.sp,
                    fontWeight = FontWeight.Normal,
                    fontFamily = FontFamily.SansSerif
                )
            }

            if (sceneTitle.isNotBlank()) {
                Text(
                    text = sceneTitle.uppercase(),
                    color = ParadoxColorTextSecondary,
                    fontSize = 12.sp,
                    fontWeight = FontWeight.Medium,
                    fontFamily = FontFamily.SansSerif,
                    letterSpacing = 1.5.sp
                )
            }
        }

        Spacer(modifier = Modifier.height(12.dp))

        HorizontalDivider(
            thickness = 1.dp,
            color = ParadoxColorBorder.copy(alpha = 0.6f)
        )
    }
}
