package com.paradoxlog.app.core.constants

/**
 * Centralized UI system presentation labels and structural tokens.
 *
 * ARCHITECTURAL CONSTRAINT:
 * Strictly limited to genuine UI/system presentation labels.
 * Contains ZERO narrative, act, scene, or dialogue fallbacks.
 * Narrative content must always flow strictly from Content JSON -> ContentRepository.
 */
object UiLabels {
    /**
     * System status badge for locked / precondition-unmet choices.
     */
    const val LABEL_LOCKED = "LOCKED"

    /**
     * Consequence screen status header.
     */
    const val LABEL_TIMELINE_SHIFTED = "TIMELINE SHIFTED"

    /**
     * Timeline change screen status header.
     */
    const val LABEL_TIMELINE_UPDATED = "TIMELINE UPDATED"

    /**
     * Universal progression action button.
     */
    const val LABEL_CONTINUE = "CONTINUE"

    /**
     * Variable labels for consequence presentation.
     */
    const val LABEL_STABILITY = "Stability"
    const val LABEL_TYRANT_INDEX = "Tyrant Index"
    const val LABEL_SELF_TRUST = "Self-Trust"

    /**
     * Timeline change metadata labels.
     */
    const val LABEL_DECISION_RECORDED = "Decision Recorded"
    const val LABEL_TIMELINE_NODES = "Timeline Nodes"
    const val LABEL_CURRENT_ACT = "Act"
    const val LABEL_CURRENT_SCENE = "Scene"

    /**
     * Navigation and menu presentation labels.
     */
    const val LABEL_PARADOX_LOG = "PARADOX LOG"
    const val LABEL_TIME_REMEMBERS = "TIME REMEMBERS"
    const val LABEL_NEW_GAME = "NEW GAME"
    const val LABEL_SETTINGS = "SETTINGS"
    const val LABEL_SYSTEM_ARCHIVES = "SYSTEM & ARCHIVES"

    /**
     * Structural visual delimiter for metadata headers.
     */
    const val HEADER_DELIMITER = "  |  "
}
