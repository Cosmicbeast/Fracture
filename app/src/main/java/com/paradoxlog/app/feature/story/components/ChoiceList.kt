package com.paradoxlog.app.feature.story.components

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.paradoxlog.app.feature.choice.ChoiceComponent
import com.paradoxlog.app.feature.story.ChoiceUi

/**
 * Story-specific choice presentation layer.
 * Delegates directly to the canonical ChoiceComponent in com.paradoxlog.app.feature.choice.
 *
 * CANONICAL FLOW:
 * Story -> Choice -> Consequence -> Timeline Change -> Exploration -> Encounter -> Story
 *
 * ChoiceList only forwards the chosen choiceId. It does NOT predict consequences,
 * modify timeline, or shortcut navigation.
 */
@Composable
fun ChoiceList(
    choices: List<ChoiceUi>,
    onChoiceSelected: (String) -> Unit,
    modifier: Modifier = Modifier
) {
    ChoiceComponent(
        choices = choices,
        onChoiceSelected = onChoiceSelected,
        modifier = modifier
    )
}
