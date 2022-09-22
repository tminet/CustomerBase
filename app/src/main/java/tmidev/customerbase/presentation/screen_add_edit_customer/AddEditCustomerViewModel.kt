package tmidev.customerbase.presentation.screen_add_edit_customer

import androidx.annotation.StringRes
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import tmidev.core.data.source.local.CustomersDataSource
import tmidev.core.domain.model.Customer
import tmidev.core.domain.type.InputErrorType
import tmidev.core.domain.usecase.ValidateSimpleFieldUseCase
import tmidev.core.util.ConstantsScreenKey
import tmidev.customerbase.R
import javax.inject.Inject

sealed class AddEditCustomerChannel {
    object NavBackToHomeScreen : AddEditCustomerChannel()
}

data class AddEditCustomerScreenState(
    val id: Int,
    val firstName: String,
    val lastName: String,
    val isActive: Boolean,
    val addedAt: Long,
    @StringRes val firstNameError: Int?,
    @StringRes val lastNameError: Int?,
    @StringRes val screenTitle: Int
)

private data class AddEditCustomerViewModelSate(
    val id: Int = 0,
    val firstName: String = "",
    val lastName: String = "",
    val isActive: Boolean = true,
    val addedAt: Long = 0,
    @StringRes val firstNameError: Int? = null,
    @StringRes val lastNameError: Int? = null,
    @StringRes val screenTitle: Int = R.string.titleAddCustomerScreen
) {
    fun toAddEditCustomerScreenState() = AddEditCustomerScreenState(
        id = id,
        firstName = firstName,
        lastName = lastName,
        isActive = isActive,
        addedAt = addedAt,
        firstNameError = firstNameError,
        lastNameError = lastNameError,
        screenTitle = screenTitle
    )
}

@HiltViewModel
class AddEditCustomerViewModel @Inject constructor(
    private val savedStateHandle: SavedStateHandle,
    private val customersDataSource: CustomersDataSource,
    private val validateSimpleFieldUseCase: ValidateSimpleFieldUseCase
) : ViewModel() {
    private val viewModelState = MutableStateFlow(value = AddEditCustomerViewModelSate())

    val screenState = viewModelState.map { it.toAddEditCustomerScreenState() }.stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(),
        initialValue = viewModelState.value.toAddEditCustomerScreenState()
    )

    private val _channel = Channel<AddEditCustomerChannel>()
    val channel = _channel.receiveAsFlow()

    init {
        getCustomer()
    }

    private fun getCustomer() = viewModelScope.launch {
        savedStateHandle.get<String>(ConstantsScreenKey.ADD_EDIT_CUSTOMER_ID)
            ?.toIntOrNull()
            ?.let { customerId ->
                val customer = customersDataSource.getById(customerId = customerId).first()

                viewModelState.update { state ->
                    state.copy(
                        id = customer.id,
                        firstName = customer.firstName,
                        lastName = customer.lastName,
                        isActive = customer.isActive,
                        addedAt = customer.addedAt,
                        screenTitle = R.string.titleEditCustomerScreen
                    )
                }
            }
    }

    fun changeFirstName(value: String) {
        viewModelState.update { it.copy(firstName = value) }
    }

    fun changeLastName(value: String) {
        viewModelState.update { it.copy(lastName = value) }
    }

    fun changeIsActive(value: Boolean) {
        viewModelState.update { it.copy(isActive = value) }
    }

    fun saveCustomer() {
        viewModelState.update { state ->
            state.copy(
                firstName = state.firstName.trim(),
                lastName = state.lastName.trim()
            )
        }

        val firstNameResult = validateSimpleFieldUseCase(
            string = viewModelState.value.firstName
        )
        val lastNameResult = validateSimpleFieldUseCase(
            string = viewModelState.value.lastName
        )

        viewModelState.update { state ->
            state.copy(
                firstNameError = when (firstNameResult.errorType) {
                    InputErrorType.FIELD_EMPTY -> R.string.errorFirstNameEmpty
                    else -> null
                },
                lastNameError = when (lastNameResult.errorType) {
                    InputErrorType.FIELD_EMPTY -> R.string.errorLastNameEmpty
                    else -> null
                }
            )
        }

        listOf(
            firstNameResult,
            lastNameResult
        ).any { inputResult ->
            !inputResult.successful
        }.also { hasError ->
            if (hasError) return
        }

        val customer = Customer(
            id = viewModelState.value.id,
            firstName = viewModelState.value.firstName,
            lastName = viewModelState.value.lastName,
            isActive = viewModelState.value.isActive,
            addedAt = viewModelState.value.addedAt.takeIf { it != 0L } ?: System.currentTimeMillis()
        )

        viewModelScope.launch {
            customersDataSource.save(customer = customer)
        }

        navBackToHomeScreen()
    }

    fun navBackToHomeScreen() = viewModelScope.launch {
        _channel.send(element = AddEditCustomerChannel.NavBackToHomeScreen)
    }
}