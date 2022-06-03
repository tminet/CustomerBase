package tmidev.customerbase.presentation.screen_add_edit_customer

import androidx.annotation.StringRes
import tmidev.customerbase.R

data class AddEditCustomerState(
    val isLoading: Boolean = true,
    val id: Int = 0,
    val firstName: String = "",
    val lastName: String = "",
    val isActive: Boolean = true,
    val addedAt: Long = 0,
    @StringRes val firstNameError: Int? = null,
    @StringRes val lastNameError: Int? = null,
    @StringRes val screenTitle: Int = R.string.titleAddCustomerScreen
)