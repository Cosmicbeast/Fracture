package com.paradoxlog.app.feature.mainmenu

import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.tween
import android.content.res.Configuration
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
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
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.paradoxlog.app.R
import com.paradoxlog.app.core.constants.UiLabels
import com.paradoxlog.app.core.designsystem.ParadoxColorStabilityCyan
import com.paradoxlog.app.core.designsystem.ParadoxColorTextPrimary
import com.paradoxlog.app.core.designsystem.ParadoxTypography

/**
 * Main Menu Screen (Panel 02).
 *
 * Implements the dark gothic ruins title screen with:
 * - Subtle, slow atmospheric camera push-in with an eerie sci-fi vibe
 * - Compact iOS glassmorphic controls
 */
@Composable
fun MainMenuScreen(
    onContinueGame: () -> Unit,
    onNewGame: () -> Unit,
    onOpenSystem: () -> Unit,
    modifier: Modifier = Modifier,
    onOpenParadoxLog: () -> Unit = onOpenSystem,
    onOpenSettings: () -> Unit = onOpenSystem
) {
    val infiniteTransition = rememberInfiniteTransition(label = "MainMenuCinematic")

    // Continuous push-in loop with zero-velocity C1 cosine easing on BOTH forward and return phases:
    // - Forward push-in: 6800ms with smooth ease-in & ease-out (gentle start & gentle deceleration into peak)
    // - Gentle return: 3600ms with smooth ease-in & ease-out (zero turnaround jerk at both ends)
    // Total cycle: 10400ms
    val loopTimeMs by infiniteTransition.animateFloat(
        initialValue = 0f,
        targetValue = 10400f,
        animationSpec = infiniteRepeatable(
            animation = tween(durationMillis = 10400, easing = LinearEasing),
            repeatMode = RepeatMode.Restart
        ),
        label = "CinematicLoopTime"
    )

    val zoomDuration = 6800f
    val resetDuration = 3600f

    val progress = if (loopTimeMs < zoomDuration) {
        // Forward zoom: smooth sinusoidal ease-in and ease-out (0.0 -> 1.0)
        val p = loopTimeMs / zoomDuration
        0.5f * (1f - kotlin.math.cos(Math.PI * p).toFloat())
    } else {
        // Return glide: smooth sinusoidal ease-in and ease-out (1.0 -> 0.0)
        val resetP = (loopTimeMs - zoomDuration) / resetDuration
        0.5f * (1f + kotlin.math.cos(Math.PI * resetP).toFloat())
    }

    val cameraZoom = 1.0f + progress * 0.12f
    val cameraPanY = -progress * 15f

    val configuration = LocalConfiguration.current
    val isLandscape = configuration.orientation == Configuration.ORIENTATION_LANDSCAPE

    Box(modifier = modifier.fillMaxSize()) {
        // Single Background Image: Crisp, sharp, and buttery smooth
        Image(
            painter = painterResource(id = R.drawable.bg_main_menu),
            contentDescription = null,
            contentScale = ContentScale.Crop,
            modifier = Modifier
                .fillMaxSize()
                .graphicsLayer {
                    scaleX = cameraZoom
                    scaleY = cameraZoom
                    translationY = cameraPanY
                }
        )

        // Layer 3: Contrast gradient to maintain button and title legibility
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(
                    Brush.verticalGradient(
                        colors = listOf(
                            Color(0x33000000),
                            Color(0x14000000),
                            Color(0x28000000),
                            Color(0x55000000)
                        )
                    )
                )
        )

        // Screen content - Responsive for Landscape and Portrait
        if (isLandscape) {
            // Landscape layout: Split 2-column view
            Row(
                modifier = Modifier
                    .fillMaxSize()
                    .safeDrawingPadding()
                    .padding(horizontal = 32.dp, vertical = 20.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                // Left Column: Brand Emblem + Title
                Column(
                    modifier = Modifier
                        .weight(1f)
                        .fillMaxSize(),
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.Center
                ) {
                    Image(
                        painter = painterResource(id = R.drawable.ic_paradox_emblem),
                        contentDescription = UiLabels.LABEL_PARADOX_LOG,
                        modifier = Modifier.size(80.dp)
                    )

                    Spacer(modifier = Modifier.height(14.dp))

                    Text(
                        text = UiLabels.LABEL_PARADOX_LOG,
                        color = ParadoxColorTextPrimary,
                        fontSize = 22.sp,
                        fontWeight = FontWeight.SemiBold,
                        fontFamily = FontFamily.Serif,
                        letterSpacing = 4.5.sp
                    )
                }

                // Right Column: Centered Action Buttons
                Column(
                    modifier = Modifier
                        .weight(1f)
                        .fillMaxSize(),
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.Center
                ) {
                    Column(
                        modifier = Modifier
                            .fillMaxWidth()
                            .widthIn(max = 340.dp)
                            .padding(horizontal = 16.dp),
                        horizontalAlignment = Alignment.CenterHorizontally,
                        verticalArrangement = Arrangement.spacedBy(10.dp)
                    ) {
                        MenuActionButton(
                            text = UiLabels.LABEL_CONTINUE,
                            isHighlighted = true,
                            onClick = onContinueGame
                        )

                        MenuActionButton(
                            text = UiLabels.LABEL_NEW_GAME,
                            isHighlighted = false,
                            onClick = onNewGame
                        )

                        MenuActionButton(
                            text = UiLabels.LABEL_SYSTEM_ARCHIVES,
                            isHighlighted = false,
                            onClick = onOpenSystem
                        )
                    }
                }
            }
        } else {
            // Portrait layout: Elegant vertical hierarchy
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .safeDrawingPadding()
                    .padding(horizontal = 28.dp, vertical = 36.dp),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.SpaceBetween
            ) {
                Spacer(modifier = Modifier.weight(0.8f))

                // Brand Header: Luminous Emblem + Title
                Column(
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Image(
                        painter = painterResource(id = R.drawable.ic_paradox_emblem),
                        contentDescription = UiLabels.LABEL_PARADOX_LOG,
                        modifier = Modifier.size(96.dp)
                    )

                    Spacer(modifier = Modifier.height(16.dp))

                    Text(
                        text = UiLabels.LABEL_PARADOX_LOG,
                        color = ParadoxColorTextPrimary,
                        fontSize = 24.sp,
                        fontWeight = FontWeight.SemiBold,
                        fontFamily = FontFamily.Serif,
                        letterSpacing = 5.sp
                    )
                }

                Spacer(modifier = Modifier.weight(1.2f))

                // Action Buttons: Compact iOS-style translucent controls with bounded max width
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .widthIn(max = 380.dp)
                        .padding(horizontal = 16.dp),
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    // Primary action: CONTINUE (Glowing Cyan Glass Highlight)
                    MenuActionButton(
                        text = UiLabels.LABEL_CONTINUE,
                        isHighlighted = true,
                        onClick = onContinueGame
                    )

                    // Secondary action: NEW GAME (Translucent Frost Glass)
                    MenuActionButton(
                        text = UiLabels.LABEL_NEW_GAME,
                        isHighlighted = false,
                        onClick = onNewGame
                    )

                    // Tertiary action: Unified SYSTEM & ARCHIVES option box
                    MenuActionButton(
                        text = UiLabels.LABEL_SYSTEM_ARCHIVES,
                        isHighlighted = false,
                        onClick = onOpenSystem
                    )
                }

                Spacer(modifier = Modifier.height(12.dp))
            }
        }
    }
}

@Composable
private fun MenuActionButton(
    text: String,
    isHighlighted: Boolean,
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    val borderColor = if (isHighlighted) ParadoxColorStabilityCyan else Color(0x5538BDF8)
    val borderWidth = if (isHighlighted) 1.5.dp else 1.dp
    val surfaceColor = if (isHighlighted) {
        Color(0x1F00E5FF)
    } else {
        Color(0x0DFFFFFF)
    }

    Surface(
        shape = RoundedCornerShape(22.dp),
        color = surfaceColor,
        border = BorderStroke(borderWidth, borderColor),
        onClick = onClick,
        modifier = modifier
            .fillMaxWidth()
            .defaultMinSize(minHeight = 48.dp)
    ) {
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 13.dp),
            contentAlignment = Alignment.Center
        ) {
            Text(
                text = text,
                style = ParadoxTypography.labelLarge,
                color = ParadoxColorTextPrimary,
                letterSpacing = 2.sp,
                fontSize = 13.sp,
                fontWeight = if (isHighlighted) FontWeight.SemiBold else FontWeight.Medium
            )
        }
    }
}
