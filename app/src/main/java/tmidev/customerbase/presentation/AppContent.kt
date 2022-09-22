package tmidev.customerbase.presentation

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import tmidev.core.domain.type.ScreenRouteType
import tmidev.core.util.ConstantsScreenKey
import tmidev.customerbase.presentation.common.theme.AppTheme
import tmidev.customerbase.presentation.common.theme.SplashTheme
import tmidev.customerbase.presentation.screen_add_edit_customer.AddEditCustomerScreen
import tmidev.customerbase.presentation.screen_home.HomeScreen
import tmidev.customerbase.presentation.screen_settings.SettingsScreen

@Composable
fun AppContent(
    mainViewModel: MainViewModel = hiltViewModel()
) {
    val state by mainViewModel.screenState.collectAsState()
    val navController = rememberNavController()

    val isDarkTheme = state.isAppThemeDarkMode ?: isSystemInDarkTheme()

    if (state.isLoading) SplashTheme()
    else AppTheme(darkTheme = isDarkTheme) {
        NavHost(
            modifier = Modifier.fillMaxSize(),
            navController = navController,
            startDestination = ScreenRouteType.Home.route,
        ) {
            homeScreen(navController = navController)
            addEditCustomerScreen(navController = navController)
            settingsScreen(navController = navController)
        }
    }
}

private fun NavGraphBuilder.homeScreen(
    navController: NavHostController
) = composable(route = ScreenRouteType.Home.route) {
    HomeScreen(
        navToAddEditCustomerScreen = { customerId ->
            navController.navigate(
                route = ScreenRouteType.AddEditCustomer.routeArgs(customerId = customerId)
            ) { launchSingleTop = true }
        },
        navToSettingsScreen = {
            navController.navigate(route = ScreenRouteType.Settings.route) {
                launchSingleTop = true
            }
        }
    )
}

private fun NavGraphBuilder.addEditCustomerScreen(
    navController: NavHostController
) = composable(
    route = ScreenRouteType.AddEditCustomer.route,
    arguments = listOf(
        navArgument(name = ConstantsScreenKey.ADD_EDIT_CUSTOMER_ID) {
            type = NavType.StringType
        }
    )
) {
    AddEditCustomerScreen(
        navBackToHomeScreen = {
            navController.popBackStack()
        }
    )
}

private fun NavGraphBuilder.settingsScreen(
    navController: NavHostController
) = composable(
    route = ScreenRouteType.Settings.route
) {
    SettingsScreen(
        navBackToHomeScreen = {
            navController.popBackStack()
        }
    )
}