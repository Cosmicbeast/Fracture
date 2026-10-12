package org.example.project.ui.screens

import androidx.compose.foundation.Image
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
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import fracture_thetyrantparadox.shared.generated.resources.Res
import fracture_thetyrantparadox.shared.generated.resources.scene_1_sky
import org.jetbrains.compose.resources.painterResource
import org.example.project.ui.theme.*

@Composable
fun TitleScreen(
    hasSaveGame: Boolean,
    onContinue: () -> Unit,
    onNewGame: () -> Unit,
    modifier: Modifier = Modifier
) {
    Box(
        modifier = modifier
            .fillMaxSize()
            .background(BackgroundDark),
        contentAlignment = Alignment.Center
    ) {
        // Atmospheric Backdrop Image
        Image(
            painter = painterResource(Res.drawable.scene_1_sky),
            contentDescription = "Atmospheric Backdrop",
            modifier = Modifier.fillMaxSize(),
            contentScale = ContentScale.Crop
        )

        // Moody Vignette Gradient
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(
                    Brush.verticalGradient(
                        colors = listOf(
                            BackgroundDark.copy(alpha = 0.75f),
                            BackgroundDark.copy(alpha = 0.88f),
                            BackgroundDark.copy(alpha = 0.98f)
                        )
                    )
                )
        )

        // Main Title Content
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 32.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            Text(
                text = "FRACTURE",
                style = MaterialTheme.typography.titleLarge.copy(
                    fontSize = 38.sp,
                    letterSpacing = 6.sp,
                    fontWeight = FontWeight.Bold
                ),
                color = TextPrimary
            )

            Spacer(modifier = Modifier.height(4.dp))

            Text(
                text = "THE TYRANT PARADOX",
                style = MaterialTheme.typography.labelLarge.copy(
                    fontSize = 13.sp,
                    letterSpacing = 3.sp
                ),
                color = Accent
            )

            Spacer(modifier = Modifier.height(28.dp))

            Text(
                text = "“What are you willing to become to protect the future?”",
                style = MaterialTheme.typography.bodyLarge.copy(
                    fontStyle = FontStyle.Italic,
                    fontSize = 15.sp,
                    lineHeight = 22.sp
                ),
                color = TextSecondary,
                textAlign = TextAlign.Center
            )

            Spacer(modifier = Modifier.height(48.dp))

            // Action Buttons
            if (hasSaveGame) {
                TitleActionButton(
                    text = "CONTINUE TIMELINE",
                    isPrimary = true,
                    onClick = onContinue
                )
                Spacer(modifier = Modifier.height(14.dp))
            }

            TitleActionButton(
                text = "NEW TIMELINE",
                isPrimary = !hasSaveGame,
                onClick = onNewGame
            )
        }
    }
}

@Composable
private fun TitleActionButton(
    text: String,
    isPrimary: Boolean,
    onClick: () -> Unit
) {
    val backgroundColor = if (isPrimary) SurfaceHighlight else SurfaceDark
    val borderColor = if (isPrimary) Accent.copy(alpha = 0.8f) else SurfaceHighlight
    val textColor = if (isPrimary) TextPrimary else TextSecondary

    Box(
        modifier = Modifier
            .fillMaxWidth()
            .height(52.dp)
            .clip(RoundedCornerShape(8.dp))
            .background(backgroundColor)
            .border(1.dp, borderColor, RoundedCornerShape(8.dp))
            .clickable { onClick() },
        contentAlignment = Alignment.Center
    ) {
        Text(
            text = text,
            style = MaterialTheme.typography.labelLarge,
            color = textColor,
            letterSpacing = 2.sp
        )
    }
}
