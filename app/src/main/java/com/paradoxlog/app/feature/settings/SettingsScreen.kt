package com.paradoxlog.app.feature.settings

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
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Slider
import androidx.compose.material3.SliderDefaults
import androidx.compose.material3.Surface
import androidx.compose.material3.Switch
import androidx.compose.material3.SwitchDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.semantics.Role
import androidx.compose.ui.semantics.role
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.text.font.FontWeight
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
 * Settings Screen (Panel 15).
 *
 * Implements audio, text speed, text scaling, haptics, and back navigation.
 */
@Composable
fun SettingsScreen(
    onNavigateBack: () -> Unit,
    modifier: Modifier = Modifier
) {
    var audioLevel by remember { mutableFloatStateOf(0.7f) }
    var textSpeed by remember { mutableFloatStateOf(0.5f) }
    var textSizeIndex by remember { mutableStateOf(1) } // 0: Small, 1: Medium, 2: Large
    var vibrationEnabled by remember { mutableStateOf(true) }

    Surface(
        color = ParadoxColorBackground,
        modifier = modifier
            .fillMaxSize()
            .statusBarsPadding()
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(horizontal = 24.dp, vertical = 24.dp)
                .verticalScroll(rememberScrollState()),
            verticalArrangement = Arrangement.spacedBy(28.dp)
        ) {
            // Header with Back Action
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Text(
                    text = UiLabels.LABEL_SETTINGS,
                    style = ParadoxTypography.headlineMedium,
                    color = ParadoxColorTextPrimary,
                    letterSpacing = 2.sp
                )

                Text(
                    text = "BACK",
                    style = ParadoxTypography.labelMedium,
                    color = ParadoxColorStabilityCyan,
                    modifier = Modifier
                        .clickable(onClick = onNavigateBack)
                        .padding(8.dp)
                )
            }

            Spacer(modifier = Modifier.height(8.dp))

            // 1. Audio Slider
            SettingSliderRow(
                label = "Audio",
                value = audioLevel,
                onValueChange = { audioLevel = it }
            )

            // 2. Text Speed Slider
            SettingSliderRow(
                label = "Text Speed",
                value = textSpeed,
                onValueChange = { textSpeed = it }
            )

            // 3. Text Size Selector
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = "Text Size",
                    style = ParadoxTypography.bodyLarge,
                    color = ParadoxColorTextSecondary
                )

                Row(horizontalArrangement = Arrangement.spacedBy(16.dp)) {
                    listOf("A" to 14.sp, "A" to 17.sp, "A" to 20.sp).forEachIndexed { index, (label, size) ->
                        val isSelected = textSizeIndex == index
                        Text(
                            text = label,
                            fontSize = size,
                            fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Normal,
                            color = if (isSelected) ParadoxColorStabilityCyan else ParadoxColorTextSecondary,
                            modifier = Modifier
                                .clickable { textSizeIndex = index }
                                .padding(4.dp)
                        )
                    }
                }
            }

            // 4. Vibration Toggle
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = "Vibration",
                    style = ParadoxTypography.bodyLarge,
                    color = ParadoxColorTextSecondary
                )

                Switch(
                    checked = vibrationEnabled,
                    onCheckedChange = { vibrationEnabled = it },
                    colors = SwitchDefaults.colors(
                        checkedThumbColor = ParadoxColorStabilityCyan,
                        checkedTrackColor = ParadoxColorSurface,
                        uncheckedThumbColor = ParadoxColorTextSecondary,
                        uncheckedTrackColor = ParadoxColorSurface
                    )
                )
            }

            // 5. Accessibility Link
            SettingNavRow(
                label = "Accessibility",
                onClick = {}
            )

            // 6. Reset Data
            SettingNavRow(
                label = "Save / Reset Data",
                onClick = {}
            )
        }
    }
}

@Composable
private fun SettingSliderRow(
    label: String,
    value: Float,
    onValueChange: (Float) -> Unit,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier.fillMaxWidth(),
        verticalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        Text(
            text = label,
            style = ParadoxTypography.bodyLarge,
            color = ParadoxColorTextSecondary
        )

        Slider(
            value = value,
            onValueChange = onValueChange,
            colors = SliderDefaults.colors(
                thumbColor = ParadoxColorStabilityCyan,
                activeTrackColor = ParadoxColorStabilityCyan,
                inactiveTrackColor = ParadoxColorSurface
            )
        )
    }
}

@Composable
private fun SettingNavRow(
    label: String,
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    Surface(
        shape = RoundedCornerShape(8.dp),
        color = ParadoxColorSurface.copy(alpha = 0.6f),
        border = BorderStroke(1.dp, ParadoxColorBorder),
        modifier = modifier
            .fillMaxWidth()
            .defaultMinSize(minHeight = 52.dp)
            .semantics { role = Role.Button }
            .clickable(onClick = onClick)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp, vertical = 14.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = label,
                style = ParadoxTypography.bodyMedium,
                color = ParadoxColorTextPrimary
            )

            Text(
                text = ">",
                style = ParadoxTypography.bodyMedium,
                color = ParadoxColorTextSecondary
            )
        }
    }
}
