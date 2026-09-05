package org.example.project.ui.components

import androidx.compose.material3.LocalTextStyle
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import kotlinx.coroutines.delay

@Composable
fun TypewriterText(
    text: String,
    modifier: Modifier = Modifier,
    style: TextStyle = LocalTextStyle.current,
    color: Color = Color.Unspecified,
    typingSpeedMs: Long = 20L,
    onTypingComplete: () -> Unit = {}
) {
    var textToDisplay by remember(text) { mutableStateOf("") }

    LaunchedEffect(text) {
        textToDisplay = ""
        for (i in text.indices) {
            textToDisplay += text[i]
            delay(typingSpeedMs)
        }
        onTypingComplete()
    }

    Text(
        text = textToDisplay,
        modifier = modifier,
        style = style,
        color = color
    )
}
