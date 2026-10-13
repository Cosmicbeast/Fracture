package com.paradoxlog.app.core.navigation

/**
 * Canonical Destination Contracts for Paradox Log.
 *
 * ARCHITECTURAL CONSTRAINTS:
 * 1. Navigation is presentation infrastructure ONLY. It does not evaluate conditions,
 *    consequences, branching, or ending rules.
 * 2. NO mutable GameState, Scene, Choice, or domain objects are ever passed through navigation arguments.
 * 3. Only stable, immutable primitive identifiers (e.g., endingId, actId) are permitted where required.
 * 4. Screens obtain dynamic gameplay state authoritatively from the GameViewModel / StateFlow pipeline.
 */
sealed class ParadoxDestination(val route: String) {

    /**
     * Initial atmospheric branded splash screen (Panel 01).
     */
    data object Splash : ParadoxDestination(ROUTE_SPLASH)

    /**
     * Primary game title and entry menu (Panel 02).
     */
    data object MainMenu : ParadoxDestination(ROUTE_MAIN_MENU)

    /**
     * New game prologue introduction screen (Panel 03).
     */
    data object Intro : ParadoxDestination(ROUTE_INTRO)

    /**
     * Primary narrative screen rendering prose, speaker headers, and inline choices.
     * Takes no arguments; state is observed live from GameViewModel.
     */
    data object Story : ParadoxDestination(ROUTE_STORY)

    /**
     * Interstitial canvas displayed when transitioning between narrative acts.
     * Arguments: completedActId, nextActId (immutable identifiers only).
     */
    data object ActTransition : ParadoxDestination(ROUTE_ACT_TRANSITION_PATTERN) {
        const val ARG_COMPLETED_ACT = "completedActId"
        const val ARG_NEXT_ACT = "nextActId"

        fun createRoute(completedActId: String, nextActId: String): String {
            return "act_transition/$completedActId/$nextActId"
        }
    }

    /**
     * Epilogue screen reached upon narrative ending resolution.
     * Arguments: endingId (immutable identifier only, e.g. "THE_SACRIFICE").
     */
    data object Ending : ParadoxDestination(ROUTE_ENDING_PATTERN) {
        const val ARG_ENDING_ID = "endingId"

        fun createRoute(endingId: String): String {
            return "ending/$endingId"
        }
    }

    /**
     * Timeline collapse canvas reached when stability hits 0.
     * Maps from GameEngineResult.TimelineCollapsed.
     */
    data object TimelineCollapse : ParadoxDestination(ROUTE_TIMELINE_COLLAPSE)

    /**
     * Chronological decision and visited node inspection.
     * Takes no arguments; inspects GameState.timeline via ViewModel.
     */
    data object Timeline : ParadoxDestination(ROUTE_TIMELINE)

    /**
     * Acquired item and artifact inspection sheet/screen.
     * Takes no arguments; inspects GameState.inventory via ViewModel.
     */
    data object Inventory : ParadoxDestination(ROUTE_INVENTORY)

    /**
     * Accessibility and display preferences (font scale, contrast, haptics).
     */
    data object Settings : ParadoxDestination(ROUTE_SETTINGS)

    /**
     * Unified System & Archive screen (Timeline Log + Preferences).
     */
    data object System : ParadoxDestination(ROUTE_SYSTEM)

    companion object {
        const val ROUTE_SPLASH = "splash"
        const val ROUTE_MAIN_MENU = "main_menu"
        const val ROUTE_INTRO = "intro"
        const val ROUTE_STORY = "story"
        const val ROUTE_ACT_TRANSITION_PATTERN = "act_transition/{completedActId}/{nextActId}"
        const val ROUTE_ENDING_PATTERN = "ending/{endingId}"
        const val ROUTE_TIMELINE_COLLAPSE = "timeline_collapse"
        const val ROUTE_TIMELINE = "timeline"
        const val ROUTE_INVENTORY = "inventory"
        const val ROUTE_SETTINGS = "settings"
        const val ROUTE_SYSTEM = "system"
    }
}
