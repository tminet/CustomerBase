package tmidev.customerbase.presentation

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.navigation.NavHostController
import androidx.navigation.compose.rememberNavController
import dagger.hilt.android.AndroidEntryPoint
import tmidev.customerbase.presentation.common.theme.AppTheme
import tmidev.customerbase.presentation.common.theme.SplashTheme

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    private val viewModel: MainViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContent {
            val state by viewModel.state
            val navController = rememberNavController()

            val isAppThemeDarkMode = state.isAppThemeDarkMode ?: isSystemInDarkTheme()

            if (state.isLoading) ComposeWaitState()
            else ComposeContentState(
                navController = navController,
                isAppThemeDarkMode = isAppThemeDarkMode
            )
        }
    }

    @Composable
    private fun ComposeWaitState() = SplashTheme()

    @Composable
    private fun ComposeContentState(
        navController: NavHostController,
        isAppThemeDarkMode: Boolean
    ) = AppTheme(darkTheme = isAppThemeDarkMode) {
        MainNavHost(navController = navController)
    }
}