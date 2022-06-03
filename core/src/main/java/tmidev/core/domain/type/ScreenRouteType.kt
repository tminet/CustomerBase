package tmidev.core.domain.type

import tmidev.core.util.ConstantsScreenKey

sealed class ScreenRouteType(val route: String) {
    object Home : ScreenRouteType(route = "home_screen")

    object AddEditCustomer : ScreenRouteType(
        route = "add_edit_customer_screen/{${ConstantsScreenKey.ADD_EDIT_CUSTOMER_ID}}"
    ) {
        fun routeArgs(customerId: String): String = route.replace(
            oldValue = "{${ConstantsScreenKey.ADD_EDIT_CUSTOMER_ID}}",
            newValue = customerId
        )
    }

    object Settings : ScreenRouteType(route = "settings_screen")
}