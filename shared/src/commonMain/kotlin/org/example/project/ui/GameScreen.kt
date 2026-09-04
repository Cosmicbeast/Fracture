package org.example.project.ui

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import org.jetbrains.compose.resources.painterResource
import fracture_thetyrantparadox.shared.generated.resources.Res
import fracture_thetyrantparadox.shared.generated.resources.scene_1_sky

@Composable
fun GameScreen() {
    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background),
        contentAlignment = Alignment.Center
    ) {
        Image(
            painter = painterResource(Res.drawable.scene_1_sky),
            contentDescription = "The Sky That Never Heals",
            modifier = Modifier.fillMaxSize(),
            contentScale = ContentScale.Crop // Crop to fill the entire screen, or use Fit to show full image
        )
    }
}
