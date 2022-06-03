package tmidev.customerbase.presentation

import androidx.compose.runtime.Composable
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.navArgument
import tmidev.core.domain.type.ScreenRouteType
import tmidev.core.util.ConstantsScreenKey
import tmidev.customerbase.presentation.screen_add_edit_customer.AddEditCustomerScreen
import tmidev.customerbase.presentation.screen_home.HomeScreen
import tmidev.customerbase.presentation.screen_settings.SettingsScreen

@Composable
fun MainNavHost(navController: NavHostController) = NavHost(
    navController = navController,
    startDestination = ScreenRouteType.Home.route,
) {
    homeScreen(navController = navController)
    addEditCustomerScreen(navController = navController)
    settingsScreen(navController = navController)
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
        onNavBackToHomeScreen = {
            navController.popBackStack()
        }
    )
}