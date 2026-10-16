package com.paradoxlog.app.feature.system

import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.togetherWith
import android.content.res.Configuration
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
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
import androidx.compose.foundation.layout.safeDrawingPadding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.Slider
import androidx.compose.material3.SliderDefaults
import androidx.compose.material3.Surface
import androidx.compose.material3.Switch
import androidx.compose.material3.SwitchDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.paradoxlog.app.core.constants.UiLabels
import com.paradoxlog.app.core.designsystem.ParadoxColorBackground
import com.paradoxlog.app.core.designsystem.ParadoxColorStabilityCyan
import com.paradoxlog.app.core.designsystem.ParadoxColorTextPrimary
import com.paradoxlog.app.core.designsystem.ParadoxColorTextSecondary
import com.paradoxlog.app.core.designsystem.ParadoxTypography
import com.paradoxlog.app.feature.story.GameViewModel

/**
 * Unified System & Archives Screen.
 *
 * Implements a minimal, premium iOS Inset-Grouped interface merging:
 * 1. Timeline Log (Live authoritative state, Stability, Tyrant Index, Self-Trust, Chronology)
 * 2. System Preferences (Audio, Text Speed, Typography scaling, Haptics, Data Management)
 */
@Composable
fun SystemScreen(
    viewModel: GameViewModel,
    onNavigateBack: () -> Unit,
    modifier: Modifier = Modifier,
    initialTab: Int = 0
) {
    var selectedTab by remember { mutableIntStateOf(initialTab) } // 0: Timeline, 1: Preferences
    val uiState by viewModel.uiState.collectAsState()

    // Local user preferences state
    var audioLevel by remember { mutableFloatStateOf(0.75f) }
    var textSpeed by remember { mutableFloatStateOf(0.5f) }
    var textSizeIndex by remember { mutableIntStateOf(1) } // 0: Small, 1: Medium, 2: Large
    var vibrationEnabled by remember { mutableStateOf(true) }

    val configuration = LocalConfiguration.current
    val isLandscape = configuration.orientation == Configuration.ORIENTATION_LANDSCAPE

    Surface(
        color = ParadoxColorBackground,
        modifier = modifier
            .fillMaxSize()
            .safeDrawingPadding()
    ) {
        Box(
            modifier = Modifier.fillMaxSize(),
            contentAlignment = Alignment.TopCenter
        ) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .widthIn(max = 840.dp)
                    .padding(
                        horizontal = if (isLandscape) 28.dp else 20.dp,
                        vertical = if (isLandscape) 8.dp else 12.dp
                    )
            ) {
                // 1. iOS Top Navigation Bar
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = if (isLandscape) 4.dp else 8.dp),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    // Left: iOS Back Action
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        modifier = Modifier
                            .clickable(onClick = onNavigateBack)
                            .padding(vertical = 6.dp, horizontal = 4.dp)
                    ) {
                        Text(
                            text = "‹",
                            fontSize = 24.sp,
                            fontWeight = FontWeight.Light,
                            color = ParadoxColorStabilityCyan
                        )
                        Spacer(modifier = Modifier.width(4.dp))
                        Text(
                            text = "Menu",
                            fontSize = 15.sp,
                            color = ParadoxColorStabilityCyan,
                            fontWeight = FontWeight.Medium
                        )
                    }

                    // Center Title
                    Text(
                        text = UiLabels.LABEL_SYSTEM_ARCHIVES,
                        style = ParadoxTypography.labelLarge,
                        color = ParadoxColorTextPrimary,
                        letterSpacing = 2.sp,
                        fontSize = 14.sp,
                        fontWeight = FontWeight.SemiBold
                    )

                    // Balanced empty spacer
                    Spacer(modifier = Modifier.width(48.dp))
                }

                Spacer(modifier = Modifier.height(if (isLandscape) 8.dp else 12.dp))

                // 2. iOS Segmented Control (Pill switcher)
                Surface(
                    shape = RoundedCornerShape(20.dp),
                    color = Color(0x14FFFFFF),
                    border = BorderStroke(0.5.dp, Color(0x3338BDF8)),
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(38.dp)
                ) {
                    Row(
                        modifier = Modifier
                            .fillMaxSize()
                            .padding(3.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        // Segment 1: TIMELINE ARCHIVE
                        val isArchiveActive = selectedTab == 0
                        Box(
                            modifier = Modifier
                                .weight(1f)
                                .fillMaxSize()
                                .background(
                                    color = if (isArchiveActive) Color(0x3300E5FF) else Color.Transparent,
                                    shape = RoundedCornerShape(18.dp)
                                )
                                .clickable { selectedTab = 0 },
                            contentAlignment = Alignment.Center
                        ) {
                            Text(
                                text = "TIMELINE LOG",
                                fontSize = 12.sp,
                                fontWeight = if (isArchiveActive) FontWeight.SemiBold else FontWeight.Medium,
                                color = if (isArchiveActive) Color.White else ParadoxColorTextSecondary,
                                letterSpacing = 1.sp
                            )
                        }

                        // Segment 2: PREFERENCES
                        val isPrefActive = selectedTab == 1
                        Box(
                            modifier = Modifier
                                .weight(1f)
                                .fillMaxSize()
                                .background(
                                    color = if (isPrefActive) Color(0x3300E5FF) else Color.Transparent,
                                    shape = RoundedCornerShape(18.dp)
                                )
                                .clickable { selectedTab = 1 },
                            contentAlignment = Alignment.Center
                        ) {
                            Text(
                                text = "PREFERENCES",
                                fontSize = 12.sp,
                                fontWeight = if (isPrefActive) FontWeight.SemiBold else FontWeight.Medium,
                                color = if (isPrefActive) Color.White else ParadoxColorTextSecondary,
                                letterSpacing = 1.sp
                            )
                        }
                    }
                }

                Spacer(modifier = Modifier.height(if (isLandscape) 12.dp else 18.dp))

                // 3. Animated Section Body
                AnimatedContent(
                    targetState = selectedTab,
                    transitionSpec = { fadeIn() togetherWith fadeOut() },
                    label = "SystemTabTransition"
                ) { tab ->
                    if (tab == 0) {
                        // TAB 0: TIMELINE ARCHIVE CONTENT
                        Column(
                            modifier = Modifier
                                .fillMaxSize()
                                .verticalScroll(rememberScrollState()),
                            verticalArrangement = Arrangement.spacedBy(16.dp)
                        ) {
                            if (isLandscape) {
                                // Landscape: 2 balanced columns
                                Row(
                                    modifier = Modifier.fillMaxWidth(),
                                    horizontalArrangement = Arrangement.spacedBy(16.dp)
                                ) {
                                    // Column 1: Metrics
                                    Column(
                                        modifier = Modifier.weight(1f),
                                        verticalArrangement = Arrangement.spacedBy(12.dp)
                                    ) {
                                        IosSectionHeader(title = "TIMELINE METRICS")
                                        IosGroupedCard {
                                            IosMetricRow(
                                                label = UiLabels.LABEL_STABILITY.uppercase(),
                                                value = uiState.timelineStability,
                                                max = 100,
                                                accentColor = ParadoxColorStabilityCyan
                                            )
                                            IosDivider()
                                            IosMetricRow(
                                                label = UiLabels.LABEL_TYRANT_INDEX.uppercase(),
                                                value = uiState.tyrantIndex,
                                                max = 100,
                                                accentColor = Color(0xFFF59E0B)
                                            )
                                            IosDivider()
                                            IosMetricRow(
                                                label = UiLabels.LABEL_SELF_TRUST.uppercase(),
                                                value = uiState.selfTrust,
                                                max = 100,
                                                accentColor = Color(0xFF38BDF8)
                                            )
                                        }
                                    }

                                    // Column 2: Chronology & Causal Integrity
                                    Column(
                                        modifier = Modifier.weight(1f),
                                        verticalArrangement = Arrangement.spacedBy(12.dp)
                                    ) {
                                        IosSectionHeader(title = "RECORDED CHRONOLOGY")
                                        IosGroupedCard {
                                            IosDetailRow(
                                                label = "Current Act",
                                                value = uiState.actTitle.ifBlank { "Act I: The Consequences" }
                                            )
                                            IosDivider()
                                            IosDetailRow(
                                                label = "Active Scene",
                                                value = uiState.sceneTitle.ifBlank { "Sector 0: Breach Quarantine" }
                                            )
                                            IosDivider()
                                            IosDetailRow(
                                                label = "Acquired Relics",
                                                value = "${uiState.inventoryCount} items"
                                            )
                                        }

                                        IosSectionHeader(title = "CAUSAL INTEGRITY")
                                        IosGroupedCard {
                                            Row(
                                                modifier = Modifier
                                                    .fillMaxWidth()
                                                    .padding(vertical = 12.dp, horizontal = 16.dp),
                                                horizontalArrangement = Arrangement.SpaceBetween,
                                                verticalAlignment = Alignment.CenterVertically
                                            ) {
                                                Row(
                                                    verticalAlignment = Alignment.CenterVertically,
                                                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                                                ) {
                                                    Box(
                                                        modifier = Modifier
                                                            .size(8.dp)
                                                            .background(ParadoxColorStabilityCyan, CircleShape)
                                                    )
                                                    Text(
                                                        text = "Continuity Status",
                                                        style = ParadoxTypography.bodyMedium,
                                                        color = ParadoxColorTextPrimary
                                                    )
                                                }
                                                Text(
                                                    text = if (uiState.timelineStability > 30) "SYNCHRONIZED" else "DEGRADING",
                                                    fontSize = 12.sp,
                                                    fontWeight = FontWeight.SemiBold,
                                                    color = ParadoxColorStabilityCyan,
                                                    letterSpacing = 1.sp
                                                )
                                            }
                                        }
                                    }
                                }
                            } else {
                                // Portrait: Vertical stack
                                IosSectionHeader(title = "TIMELINE METRICS")
                                IosGroupedCard {
                                    IosMetricRow(
                                        label = UiLabels.LABEL_STABILITY.uppercase(),
                                        value = uiState.timelineStability,
                                        max = 100,
                                        accentColor = ParadoxColorStabilityCyan
                                    )
                                    IosDivider()
                                    IosMetricRow(
                                        label = UiLabels.LABEL_TYRANT_INDEX.uppercase(),
                                        value = uiState.tyrantIndex,
                                        max = 100,
                                        accentColor = Color(0xFFF59E0B)
                                    )
                                    IosDivider()
                                    IosMetricRow(
                                        label = UiLabels.LABEL_SELF_TRUST.uppercase(),
                                        value = uiState.selfTrust,
                                        max = 100,
                                        accentColor = Color(0xFF38BDF8)
                                    )
                                }

                                IosSectionHeader(title = "RECORDED CHRONOLOGY")
                                IosGroupedCard {
                                    IosDetailRow(
                                        label = "Current Act",
                                        value = uiState.actTitle.ifBlank { "Act I: The Consequences" }
                                    )
                                    IosDivider()
                                    IosDetailRow(
                                        label = "Active Scene",
                                        value = uiState.sceneTitle.ifBlank { "Sector 0: Breach Quarantine" }
                                    )
                                    IosDivider()
                                    IosDetailRow(
                                        label = "Acquired Relics",
                                        value = "${uiState.inventoryCount} items"
                                    )
                                }

                                IosSectionHeader(title = "CAUSAL INTEGRITY")
                                IosGroupedCard {
                                    Row(
                                        modifier = Modifier
                                            .fillMaxWidth()
                                            .padding(vertical = 12.dp, horizontal = 16.dp),
                                        horizontalArrangement = Arrangement.SpaceBetween,
                                        verticalAlignment = Alignment.CenterVertically
                                    ) {
                                        Row(
                                            verticalAlignment = Alignment.CenterVertically,
                                            horizontalArrangement = Arrangement.spacedBy(8.dp)
                                        ) {
                                            Box(
                                                modifier = Modifier
                                                    .size(8.dp)
                                                    .background(ParadoxColorStabilityCyan, CircleShape)
                                            )
                                            Text(
                                                text = "Continuity Status",
                                                style = ParadoxTypography.bodyMedium,
                                                color = ParadoxColorTextPrimary
                                            )
                                        }
                                        Text(
                                            text = if (uiState.timelineStability > 30) "SYNCHRONIZED" else "DEGRADING",
                                            fontSize = 12.sp,
                                            fontWeight = FontWeight.SemiBold,
                                            color = ParadoxColorStabilityCyan,
                                            letterSpacing = 1.sp
                                        )
                                    }
                                }
                            }

                            Spacer(modifier = Modifier.height(24.dp))
                        }
                    } else {
                        // TAB 1: PREFERENCES & SETTINGS CONTENT
                        Column(
                            modifier = Modifier
                                .fillMaxSize()
                                .verticalScroll(rememberScrollState()),
                            verticalArrangement = Arrangement.spacedBy(16.dp)
                        ) {
                            if (isLandscape) {
                                // Landscape: 2 balanced columns
                                Row(
                                    modifier = Modifier.fillMaxWidth(),
                                    horizontalArrangement = Arrangement.spacedBy(16.dp)
                                ) {
                                    // Column 1: Audio & Pacing + System Data
                                    Column(
                                        modifier = Modifier.weight(1f),
                                        verticalArrangement = Arrangement.spacedBy(12.dp)
                                    ) {
                                        IosSectionHeader(title = "AUDIO & PACING")
                                        IosGroupedCard {
                                            IosSliderRow(
                                                label = "Master Audio",
                                                value = audioLevel,
                                                onValueChange = { audioLevel = it }
                                            )
                                            IosDivider()
                                            IosSliderRow(
                                                label = "Text Reveal Speed",
                                                value = textSpeed,
                                                onValueChange = { textSpeed = it }
                                            )
                                        }

                                        IosSectionHeader(title = "SYSTEM DATA")
                                        IosGroupedCard {
                                            Row(
                                                modifier = Modifier
                                                    .fillMaxWidth()
                                                    .clickable { }
                                                    .padding(vertical = 12.dp, horizontal = 16.dp),
                                                horizontalArrangement = Arrangement.SpaceBetween,
                                                verticalAlignment = Alignment.CenterVertically
                                            ) {
                                                Text(
                                                    text = "Accessibility Preferences",
                                                    style = ParadoxTypography.bodyMedium,
                                                    color = ParadoxColorTextPrimary
                                                )
                                                Text(
                                                    text = "›",
                                                    fontSize = 18.sp,
                                                    color = ParadoxColorTextSecondary
                                                )
                                            }

                                            IosDivider()

                                            Row(
                                                modifier = Modifier
                                                    .fillMaxWidth()
                                                    .clickable { }
                                                    .padding(vertical = 12.dp, horizontal = 16.dp),
                                                horizontalArrangement = Arrangement.SpaceBetween,
                                                verticalAlignment = Alignment.CenterVertically
                                            ) {
                                                Text(
                                                    text = "Reset Timeline Data",
                                                    style = ParadoxTypography.bodyMedium,
                                                    color = Color(0xFFFF5252)
                                                )
                                                Text(
                                                    text = "›",
                                                    fontSize = 18.sp,
                                                    color = Color(0xFFFF5252)
                                                )
                                            }
                                        }
                                    }

                                    // Column 2: Typography & Haptics
                                    Column(
                                        modifier = Modifier.weight(1f),
                                        verticalArrangement = Arrangement.spacedBy(12.dp)
                                    ) {
                                        IosSectionHeader(title = "TYPOGRAPHY & HAPTICS")
                                        IosGroupedCard {
                                            Row(
                                                modifier = Modifier
                                                    .fillMaxWidth()
                                                    .padding(vertical = 12.dp, horizontal = 16.dp),
                                                horizontalArrangement = Arrangement.SpaceBetween,
                                                verticalAlignment = Alignment.CenterVertically
                                            ) {
                                                Text(
                                                    text = "Text Scale",
                                                    style = ParadoxTypography.bodyMedium,
                                                    color = ParadoxColorTextPrimary
                                                )

                                                Row(horizontalArrangement = Arrangement.spacedBy(14.dp)) {
                                                    listOf("S" to 13.sp, "M" to 16.sp, "L" to 19.sp).forEachIndexed { idx, (lbl, sz) ->
                                                        val isSel = textSizeIndex == idx
                                                        Text(
                                                            text = lbl,
                                                            fontSize = sz,
                                                            fontWeight = if (isSel) FontWeight.Bold else FontWeight.Normal,
                                                            color = if (isSel) ParadoxColorStabilityCyan else ParadoxColorTextSecondary,
                                                            modifier = Modifier
                                                                .clickable { textSizeIndex = idx }
                                                                .padding(4.dp)
                                                        )
                                                    }
                                                }
                                            }

                                            IosDivider()

                                            Row(
                                                modifier = Modifier
                                                    .fillMaxWidth()
                                                    .padding(vertical = 10.dp, horizontal = 16.dp),
                                                horizontalArrangement = Arrangement.SpaceBetween,
                                                verticalAlignment = Alignment.CenterVertically
                                            ) {
                                                Text(
                                                    text = "Haptic Vibration",
                                                    style = ParadoxTypography.bodyMedium,
                                                    color = ParadoxColorTextPrimary
                                                )

                                                Switch(
                                                    checked = vibrationEnabled,
                                                    onCheckedChange = { vibrationEnabled = it },
                                                    colors = SwitchDefaults.colors(
                                                        checkedThumbColor = ParadoxColorStabilityCyan,
                                                        checkedTrackColor = Color(0x3300E5FF),
                                                        uncheckedThumbColor = ParadoxColorTextSecondary,
                                                        uncheckedTrackColor = Color(0x14FFFFFF)
                                                    )
                                                )
                                            }
                                        }
                                    }
                                }
                            } else {
                                // Portrait: Vertical stack
                                IosSectionHeader(title = "AUDIO & PACING")
                                IosGroupedCard {
                                    IosSliderRow(
                                        label = "Master Audio",
                                        value = audioLevel,
                                        onValueChange = { audioLevel = it }
                                    )
                                    IosDivider()
                                    IosSliderRow(
                                        label = "Text Reveal Speed",
                                        value = textSpeed,
                                        onValueChange = { textSpeed = it }
                                    )
                                }

                                IosSectionHeader(title = "TYPOGRAPHY & HAPTICS")
                                IosGroupedCard {
                                    Row(
                                        modifier = Modifier
                                            .fillMaxWidth()
                                            .padding(vertical = 12.dp, horizontal = 16.dp),
                                        horizontalArrangement = Arrangement.SpaceBetween,
                                        verticalAlignment = Alignment.CenterVertically
                                    ) {
                                        Text(
                                            text = "Text Scale",
                                            style = ParadoxTypography.bodyMedium,
                                            color = ParadoxColorTextPrimary
                                        )

                                        Row(horizontalArrangement = Arrangement.spacedBy(14.dp)) {
                                            listOf("S" to 13.sp, "M" to 16.sp, "L" to 19.sp).forEachIndexed { idx, (lbl, sz) ->
                                                val isSel = textSizeIndex == idx
                                                Text(
                                                    text = lbl,
                                                    fontSize = sz,
                                                    fontWeight = if (isSel) FontWeight.Bold else FontWeight.Normal,
                                                    color = if (isSel) ParadoxColorStabilityCyan else ParadoxColorTextSecondary,
                                                    modifier = Modifier
                                                        .clickable { textSizeIndex = idx }
                                                        .padding(4.dp)
                                                )
                                            }
                                        }
                                    }

                                    IosDivider()

                                    Row(
                                        modifier = Modifier
                                            .fillMaxWidth()
                                            .padding(vertical = 10.dp, horizontal = 16.dp),
                                        horizontalArrangement = Arrangement.SpaceBetween,
                                        verticalAlignment = Alignment.CenterVertically
                                    ) {
                                        Text(
                                            text = "Haptic Vibration",
                                            style = ParadoxTypography.bodyMedium,
                                            color = ParadoxColorTextPrimary
                                        )

                                        Switch(
                                            checked = vibrationEnabled,
                                            onCheckedChange = { vibrationEnabled = it },
                                            colors = SwitchDefaults.colors(
                                                checkedThumbColor = ParadoxColorStabilityCyan,
                                                checkedTrackColor = Color(0x3300E5FF),
                                                uncheckedThumbColor = ParadoxColorTextSecondary,
                                                uncheckedTrackColor = Color(0x14FFFFFF)
                                            )
                                        )
                                    }
                                }

                                IosSectionHeader(title = "SYSTEM DATA")
                                IosGroupedCard {
                                    Row(
                                        modifier = Modifier
                                            .fillMaxWidth()
                                            .clickable { }
                                            .padding(vertical = 14.dp, horizontal = 16.dp),
                                        horizontalArrangement = Arrangement.SpaceBetween,
                                        verticalAlignment = Alignment.CenterVertically
                                    ) {
                                        Text(
                                            text = "Accessibility Preferences",
                                            style = ParadoxTypography.bodyMedium,
                                            color = ParadoxColorTextPrimary
                                        )
                                        Text(
                                            text = "›",
                                            fontSize = 18.sp,
                                            color = ParadoxColorTextSecondary
                                        )
                                    }

                                    IosDivider()

                                    Row(
                                        modifier = Modifier
                                            .fillMaxWidth()
                                            .clickable { }
                                            .padding(vertical = 14.dp, horizontal = 16.dp),
                                        horizontalArrangement = Arrangement.SpaceBetween,
                                        verticalAlignment = Alignment.CenterVertically
                                    ) {
                                        Text(
                                            text = "Reset Timeline Data",
                                            style = ParadoxTypography.bodyMedium,
                                            color = Color(0xFFFF5252)
                                        )
                                        Text(
                                            text = "›",
                                            fontSize = 18.sp,
                                            color = Color(0xFFFF5252)
                                        )
                                    }
                                }
                            }

                            Spacer(modifier = Modifier.height(24.dp))
                        }
                    }
                }
            }
        }
    }
}

@Composable
private fun IosSectionHeader(title: String) {
    Text(
        text = title,
        fontSize = 11.sp,
        fontWeight = FontWeight.SemiBold,
        color = Color(0x88FFFFFF),
        letterSpacing = 1.2.sp,
        modifier = Modifier.padding(start = 6.dp, top = 2.dp)
    )
}

@Composable
private fun IosGroupedCard(
    modifier: Modifier = Modifier,
    content: @Composable () -> Unit
) {
    Surface(
        shape = RoundedCornerShape(16.dp),
        color = Color(0x0EFFFFFF),
        border = BorderStroke(0.5.dp, Color(0x2438BDF8)),
        modifier = modifier.fillMaxWidth()
    ) {
        Column(modifier = Modifier.fillMaxWidth()) {
            content()
        }
    }
}

@Composable
private fun IosDivider() {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .height(0.5.dp)
            .padding(horizontal = 16.dp)
            .background(Color(0x18FFFFFF))
    )
}

@Composable
private fun IosMetricRow(
    label: String,
    value: Int,
    max: Int,
    accentColor: Color
) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 12.dp, horizontal = 16.dp),
        verticalArrangement = Arrangement.spacedBy(6.dp)
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Text(
                text = label,
                style = ParadoxTypography.bodyMedium,
                color = ParadoxColorTextSecondary,
                fontSize = 13.sp
            )
            Text(
                text = "$value%",
                style = ParadoxTypography.bodyMedium,
                fontWeight = FontWeight.SemiBold,
                color = ParadoxColorTextPrimary,
                fontSize = 13.sp
            )
        }

        LinearProgressIndicator(
            progress = (value.toFloat() / max).coerceIn(0f, 1f),
            color = accentColor,
            trackColor = Color(0x18FFFFFF),
            modifier = Modifier
                .fillMaxWidth()
                .height(4.dp)
        )
    }
}

@Composable
private fun IosDetailRow(label: String, value: String) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 12.dp, horizontal = 16.dp),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(
            text = label,
            style = ParadoxTypography.bodyMedium,
            color = ParadoxColorTextSecondary,
            fontSize = 13.sp
        )
        Text(
            text = value,
            style = ParadoxTypography.bodyMedium,
            fontWeight = FontWeight.Medium,
            color = ParadoxColorTextPrimary,
            fontSize = 13.sp
        )
    }
}

@Composable
private fun IosSliderRow(
    label: String,
    value: Float,
    onValueChange: (Float) -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 10.dp, horizontal = 16.dp),
        verticalArrangement = Arrangement.spacedBy(4.dp)
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Text(
                text = label,
                style = ParadoxTypography.bodyMedium,
                color = ParadoxColorTextSecondary,
                fontSize = 13.sp
            )
            Text(
                text = "${(value * 100).toInt()}%",
                style = ParadoxTypography.bodyMedium,
                color = ParadoxColorTextPrimary,
                fontSize = 13.sp
            )
        }

        Slider(
            value = value,
            onValueChange = onValueChange,
            colors = SliderDefaults.colors(
                thumbColor = ParadoxColorStabilityCyan,
                activeTrackColor = ParadoxColorStabilityCyan,
                inactiveTrackColor = Color(0x18FFFFFF)
            )
        )
    }
}
