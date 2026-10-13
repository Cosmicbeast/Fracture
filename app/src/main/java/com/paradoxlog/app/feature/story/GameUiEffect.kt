package com.paradoxlog.app.feature.story

/**
 * One-time UI events dispatched by GameViewModel to drive navigation
 * or transient visual feedback without polluting the persistent GameState.
 */
sealed interface GameUiEffect {
    /**
     * Triggered when the engine reports timeline stability reaching 0.
     * Maps directly from GameEngineResult.TimelineCollapsed.
     */
    data class TriggerTimelineCollapse(val stability: Int) : GameUiEffect

    /**
     * Triggered when the engine reports an ending resolution.
     * Maps directly from GameEngineResult.EndingReached.
     */
    data class NavigateToEnding(val endingId: String) : GameUiEffect

    /**
     * Triggered when an act progression transition occurs.
     */
    data class NavigateToActTransition(val completedActId: String, val nextActId: String) : GameUiEffect

    /**
     * Transient message for unavailable choice rejection or missing content.
     */
    data class ShowMessage(val message: String) : GameUiEffect
}
