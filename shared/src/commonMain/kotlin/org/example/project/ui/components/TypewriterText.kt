package org.example.project.ui.components

import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import kotlinx.coroutines.delay
import org.example.project.ui.theme.TextPrimary
import kotlin.time.Duration.Companion.milliseconds

@Composable
fun TypewriterText(
    text: String,
    modifier: Modifier = Modifier,
    style: TextStyle = MaterialTheme.typography.bodyLarge,
    color: Color = TextPrimary,
    typingSpeedMs: Long = 15L,
    skipAnimation: Boolean = false,
    onTypingComplete: () -> Unit = {}
) {
    var displayedLength by remember(text, skipAnimation) {
        mutableStateOf(if (skipAnimation) text.length else 0)
    }

    LaunchedEffect(text, skipAnimation) {
        if (skipAnimation) {
            displayedLength = text.length
            onTypingComplete()
            return@LaunchedEffect
        }

        displayedLength = 0
        for (i in 1..text.length) {
            displayedLength = i
            delay(typingSpeedMs.milliseconds)
        }
        onTypingComplete()
    }

    Text(
        text = text.take(displayedLength),
        modifier = modifier,
        style = style,
        color = color
    )
}
