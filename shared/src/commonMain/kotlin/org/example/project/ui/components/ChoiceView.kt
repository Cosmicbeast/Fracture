package org.example.project.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import org.example.project.core.Choice
import org.example.project.core.GameState
import org.example.project.engine.ChoiceResolver
import org.example.project.ui.theme.*

@Composable
fun ChoiceView(
    choices: List<Choice>,
    state: GameState,
    onChoiceSelected: (Choice) -> Unit,
    modifier: Modifier = Modifier
) {
    val availableChoices = ChoiceResolver.filterAvailableChoices(choices, state)

    Column(
        modifier = modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp, vertical = 12.dp),
        verticalArrangement = Arrangement.spacedBy(10.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        availableChoices.forEach { choice ->
            val foresight = ChoiceResolver.getForesightPreview(choice, state)
            val stabilityPreview = ChoiceResolver.getStabilityPreview(choice, state)

            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .defaultMinSize(minHeight = 52.dp)
                    .clip(RoundedCornerShape(8.dp))
                    .background(SurfaceDark.copy(alpha = 0.95f))
                    .border(1.dp, SurfaceHighlight, RoundedCornerShape(8.dp))
                    .clickable { onChoiceSelected(choice) }
                    .padding(horizontal = 16.dp, vertical = 12.dp),
                contentAlignment = Alignment.CenterStart
            ) {
                Column {
                    Text(
                        text = choice.text,
                        style = MaterialTheme.typography.labelLarge,
                        color = TextPrimary,
                        textAlign = TextAlign.Start
                    )

                    if (foresight != null) {
                        Spacer(modifier = Modifier.height(4.dp))
                        Text(
                            text = "✦ Foresight: $foresight",
                            style = MaterialTheme.typography.bodyMedium.copy(fontSize = 12.sp),
                            color = Accent,
                            fontStyle = FontStyle.Italic
                        )
                    }

                    if (stabilityPreview != null) {
                        Spacer(modifier = Modifier.height(4.dp))
                        Text(
                            text = "⚖ Compass: $stabilityPreview",
                            style = MaterialTheme.typography.bodyMedium.copy(fontSize = 12.sp),
                            color = StabilityWarning,
                            fontStyle = FontStyle.Italic
                        )
                    }
                }
            }
        }
    }
}
