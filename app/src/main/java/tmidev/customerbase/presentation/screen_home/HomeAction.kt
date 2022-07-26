package tmidev.customerbase.presentation.screen_home

import tmidev.core.domain.model.Customer

sealed class HomeAction {
    object OpenDrawer : HomeAction()
    object NavToSettingsScreen : HomeAction()
    object SwitchListOrder : HomeAction()
    data class NavToAddEditCustomerScreen(val customerId: Int? = null) : HomeAction()
    data class DeleteCustomer(val customer: Customer) : HomeAction()
    data class QueryChanged(val query: String) : HomeAction()
}