package com.paradoxlog.app

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.compose.rememberNavController
import com.paradoxlog.app.core.designsystem.ParadoxTheme
import com.paradoxlog.app.core.navigation.ParadoxNavHost
import com.paradoxlog.app.data.content.AssetContentDataSource
import com.paradoxlog.app.di.DataModule
import com.paradoxlog.app.di.GameModule
import com.paradoxlog.app.feature.story.GameViewModel

/**
 * Root Activity for Paradox Log.
 *
 * Bootstraps the application viewport, initializes GameViewModel via
 * composition root modules, and binds ParadoxNavHost within ParadoxTheme.
 */
class MainActivity : ComponentActivity() {

    private val viewModel: GameViewModel by viewModels {
        object : ViewModelProvider.Factory {
            @Suppress("UNCHECKED_CAST")
            override fun <T : ViewModel> create(modelClass: Class<T>): T {
                val dataSource = AssetContentDataSource(applicationContext.assets)
                val repository = DataModule.provideContentRepository(dataSource)
                val engine = GameModule.provideGameEngine(repository)
                return GameViewModel(engine, repository) as T
            }
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        enableEdgeToEdge()
        super.onCreate(savedInstanceState)

        // Dismiss the Android 12+ OS splash screen immediately so the custom Splash screen displays instantly
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.S) {
            splashScreen.setOnExitAnimationListener { splashScreenView ->
                splashScreenView.remove()
            }
        }

        setContent {
            androidx.compose.runtime.CompositionLocalProvider(
                androidx.lifecycle.compose.LocalLifecycleOwner provides this
            ) {
                ParadoxTheme {
                    val navController = rememberNavController()
                    ParadoxNavHost(
                        navController = navController,
                        gameViewModel = viewModel
                    )
                }
            }
        }
    }
}
