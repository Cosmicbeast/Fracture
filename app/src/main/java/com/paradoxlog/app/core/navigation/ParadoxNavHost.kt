package com.paradoxlog.app.core.navigation

import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.navArgument
import com.paradoxlog.app.feature.story.GameUiEffect
import com.paradoxlog.app.feature.story.GameViewModel
import kotlinx.coroutines.flow.collectLatest

/**
 * Root Navigation Host for Paradox Log.
 *
 * Connects GameUiEffect emissions from GameViewModel to NavController
 * without passing mutable GameState or domain objects through route arguments.
 */
@Composable
fun ParadoxNavHost(
    navController: NavHostController,
    gameViewModel: GameViewModel,
    modifier: Modifier = Modifier,
    startDestination: String = ParadoxDestination.Splash.route,
    onShowMessage: (String) -> Unit = {},
    splashContent: @Composable () -> Unit = {
        com.paradoxlog.app.feature.splash.SplashScreen(
            onNavigateToMainMenu = {
                navController.navigate(ParadoxDestination.MainMenu.route) {
                    popUpTo(ParadoxDestination.Splash.route) { inclusive = true }
                }
            }
        )
    },
    mainMenuContent: @Composable () -> Unit = {
        com.paradoxlog.app.feature.mainmenu.MainMenuScreen(
            onContinueGame = {
                if (gameViewModel.uiState.value.actId.isBlank()) {
                    gameViewModel.startNewGame("act-01")
                }
                navController.navigate(ParadoxDestination.Story.route)
            },
            onNewGame = {
                gameViewModel.startNewGame("act-01")
                navController.navigate(ParadoxDestination.Intro.route)
            },
            onOpenSystem = {
                navController.navigate(ParadoxDestination.System.route)
            }
        )
    },
    introContent: @Composable () -> Unit = {
        com.paradoxlog.app.feature.intro.IntroScreen(
            onContinue = {
                navController.navigate(ParadoxDestination.Story.route)
            }
        )
    },
    storyContent: @Composable () -> Unit = {
        com.paradoxlog.app.feature.story.StoryScreen(viewModel = gameViewModel)
    },
    actTransitionContent: @Composable (completedActId: String, nextActId: String) -> Unit = { _, _ -> },
    endingContent: @Composable (endingId: String) -> Unit = {},
    timelineCollapseContent: @Composable () -> Unit = {},
    systemContent: @Composable () -> Unit = {
        com.paradoxlog.app.feature.system.SystemScreen(
            viewModel = gameViewModel,
            onNavigateBack = { navController.popBackStack() }
        )
    },
    timelineContent: @Composable () -> Unit = {
        com.paradoxlog.app.feature.system.SystemScreen(
            viewModel = gameViewModel,
            onNavigateBack = { navController.popBackStack() },
            initialTab = 0
        )
    },
    inventoryContent: @Composable () -> Unit = {},
    settingsContent: @Composable () -> Unit = {
        com.paradoxlog.app.feature.system.SystemScreen(
            viewModel = gameViewModel,
            onNavigateBack = { navController.popBackStack() },
            initialTab = 1
        )
    }
) {
    // Collect one-time effects from GameViewModel to drive navigation
    LaunchedEffect(gameViewModel) {
        gameViewModel.effects.collectLatest { effect ->
            when (effect) {
                is GameUiEffect.TriggerTimelineCollapse -> {
                    navController.navigate(ParadoxDestination.TimelineCollapse.route)
                }
                is GameUiEffect.NavigateToEnding -> {
                    navController.navigate(ParadoxDestination.Ending.createRoute(effect.endingId))
                }
                is GameUiEffect.NavigateToActTransition -> {
                    navController.navigate(
                        ParadoxDestination.ActTransition.createRoute(
                            completedActId = effect.completedActId,
                            nextActId = effect.nextActId
                        )
                    )
                }
                is GameUiEffect.ShowMessage -> {
                    onShowMessage(effect.message)
                }
            }
        }
    }

    NavHost(
        navController = navController,
        startDestination = startDestination,
        modifier = modifier
    ) {
        composable(ParadoxDestination.Splash.route) {
            splashContent()
        }

        composable(ParadoxDestination.MainMenu.route) {
            mainMenuContent()
        }

        composable(ParadoxDestination.Intro.route) {
            introContent()
        }

        composable(ParadoxDestination.Story.route) {
            storyContent()
        }

        composable(
            route = ParadoxDestination.ActTransition.route,
            arguments = listOf(
                navArgument(ParadoxDestination.ActTransition.ARG_COMPLETED_ACT) {
                    type = NavType.StringType
                },
                navArgument(ParadoxDestination.ActTransition.ARG_NEXT_ACT) {
                    type = NavType.StringType
                }
            )
        ) { backStackEntry ->
            val completedActId = backStackEntry.arguments?.getString(
                ParadoxDestination.ActTransition.ARG_COMPLETED_ACT
            ).orEmpty()
            val nextActId = backStackEntry.arguments?.getString(
                ParadoxDestination.ActTransition.ARG_NEXT_ACT
            ).orEmpty()
            actTransitionContent(completedActId, nextActId)
        }

        composable(
            route = ParadoxDestination.Ending.route,
            arguments = listOf(
                navArgument(ParadoxDestination.Ending.ARG_ENDING_ID) {
                    type = NavType.StringType
                }
            )
        ) { backStackEntry ->
            val endingId = backStackEntry.arguments?.getString(
                ParadoxDestination.Ending.ARG_ENDING_ID
            ).orEmpty()
            endingContent(endingId)
        }

        composable(ParadoxDestination.TimelineCollapse.route) {
            timelineCollapseContent()
        }

        composable(ParadoxDestination.Timeline.route) {
            timelineContent()
        }

        composable(ParadoxDestination.Inventory.route) {
            inventoryContent()
        }

        composable(ParadoxDestination.Settings.route) {
            settingsContent()
        }

        composable(ParadoxDestination.System.route) {
            systemContent()
        }
    }
}
