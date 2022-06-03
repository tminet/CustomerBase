package tmidev.customerbase.presentation.screen_home

sealed class HomeChannel {
    object OpenDrawer : HomeChannel()
    object NavToSettingsScreen : HomeChannel()
    data class NavToAddEditCustomerScreen(val customerId: Int?) : HomeChannel()
}