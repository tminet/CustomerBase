package tmidev.customerbase.presentation.screen_add_edit_customer

sealed class AddEditCustomerAction {
    object NavBackToHomeScreen : AddEditCustomerAction()
    object SaveCustomer : AddEditCustomerAction()
    data class FirstNameChanged(val firstName: String) : AddEditCustomerAction()
    data class LastNameChanged(val lastName: String) : AddEditCustomerAction()
    data class IsActiveChanged(val isActive: Boolean) : AddEditCustomerAction()
}