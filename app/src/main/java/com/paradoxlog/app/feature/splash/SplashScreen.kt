package com.paradoxlog.app.feature.splash

import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
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
import com.paradoxlog.app.core.designsystem.ParadoxColorTextSecondary
import kotlinx.coroutines.delay

/**
 * Atmospheric Branded Splash Screen (Panel 01).
 *
 * Displays the dark gothic-cyberpunk ruins background, luminous central
 * Paradox emblem, tracked cinematic typography, and "TIME REMEMBERS" caption.
 * Automatically advances or transitions on user tap.
 */
@Composable
fun SplashScreen(
    onNavigateToMainMenu: () -> Unit,
    modifier: Modifier = Modifier
) {
    val interactionSource = remember { MutableInteractionSource() }

    // Pulsing aura animation for the temporal emblem
    val infiniteTransition = rememberInfiniteTransition(label = "SplashEmblemPulse")
    val emblemAlpha by infiniteTransition.animateFloat(
        initialValue = 0.85f,
        targetValue = 1.0f,
        animationSpec = infiniteRepeatable(
            animation = tween(durationMillis = 1800, easing = FastOutSlowInEasing),
            repeatMode = RepeatMode.Reverse
        ),
        label = "EmblemAlpha"
    )

    // Auto-advance after 2.5 seconds
    LaunchedEffect(Unit) {
        delay(2500L)
        onNavigateToMainMenu()
    }

    Box(
        modifier = modifier
            .fillMaxSize()
            .clickable(
                interactionSource = interactionSource,
                indication = null,
                onClick = onNavigateToMainMenu
            )
    ) {
        // 1. High-fidelity cinematic background art
        Image(
            painter = painterResource(id = R.drawable.bg_splash),
            contentDescription = null,
            contentScale = ContentScale.Crop,
            modifier = Modifier.fillMaxSize()
        )

        // 2. Subtle dark vignette gradient overlay
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(
                    Brush.verticalGradient(
                        colors = listOf(
                            Color(0x660B0C0E),
                            Color(0x330B0C0E),
                            Color(0xBB0B0C0E),
                            Color(0xEE0B0C0E)
                        )
                    )
                )
        )

        // 3. Central Brand Content
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(horizontal = 24.dp, vertical = 64.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.SpaceBetween
        ) {
            Spacer(modifier = Modifier.weight(1f))

            // Glowing Emblem Logo
            Image(
                painter = painterResource(id = R.drawable.ic_paradox_emblem),
                contentDescription = UiLabels.LABEL_PARADOX_LOG,
                modifier = Modifier
                    .size(92.dp)
                    .alpha(emblemAlpha)
            )

            Spacer(modifier = Modifier.height(28.dp))

            // Cinematic Title
            Text(
                text = UiLabels.LABEL_PARADOX_LOG,
                color = ParadoxColorTextPrimary,
                fontSize = 28.sp,
                fontWeight = FontWeight.SemiBold,
                fontFamily = FontFamily.Serif,
                letterSpacing = 6.sp
            )

            Spacer(modifier = Modifier.weight(1.2f))

            // Atmospheric Subtitle
            Text(
                text = UiLabels.LABEL_TIME_REMEMBERS,
                color = ParadoxColorStabilityCyan.copy(alpha = 0.85f),
                fontSize = 12.sp,
                fontWeight = FontWeight.Normal,
                fontFamily = FontFamily.SansSerif,
                letterSpacing = 4.sp
            )
        }
    }
}
