package tmidev.customerbase.presentation.screen_add_edit_customer

import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.launch
import tmidev.core.data.source.local.CustomersDataSource
import tmidev.core.domain.model.Customer
import tmidev.core.domain.type.InputErrorType
import tmidev.core.domain.usecase.ValidateSimpleFieldUseCase
import tmidev.core.util.ConstantsScreenKey
import tmidev.customerbase.R
import javax.inject.Inject

@HiltViewModel
class AddEditCustomerViewModel @Inject constructor(
    private val savedStateHandle: SavedStateHandle,
    private val customersDataSource: CustomersDataSource,
    private val validateSimpleFieldUseCase: ValidateSimpleFieldUseCase
) : ViewModel() {
    var state = mutableStateOf(value = AddEditCustomerState())
        private set

    private val _channel = Channel<AddEditCustomerChannel>()
    val channel = _channel.receiveAsFlow()

    init {
        getCustomer()
    }

    fun onAction(action: AddEditCustomerAction) {
        when (action) {
            AddEditCustomerAction.NavBackToHomeScreen -> navBackToHomeScreen()
            is AddEditCustomerAction.SaveCustomer -> saveCustomer()
            is AddEditCustomerAction.FirstNameChanged -> state.value = state.value.copy(
                firstName = action.firstName
            )
            is AddEditCustomerAction.LastNameChanged -> state.value = state.value.copy(
                lastName = action.lastName
            )
            is AddEditCustomerAction.IsActiveChanged -> state.value = state.value.copy(
                isActive = action.isActive
            )
        }
    }

    private fun getCustomer() = viewModelScope.launch {
        savedStateHandle.get<String>(ConstantsScreenKey.ADD_EDIT_CUSTOMER_ID)
            ?.toIntOrNull()
            ?.let { customerId ->
                customersDataSource.getById(customerId = customerId).collectLatest {
                    state.value = state.value.copy(
                        isLoading = false,
                        id = it.id,
                        firstName = it.firstName,
                        lastName = it.lastName,
                        isActive = it.isActive,
                        addedAt = it.addedAt,
                        screenTitle = R.string.titleEditCustomerScreen
                    )
                }
            }
    }

    private fun saveCustomer() {
        val firstNameResult = validateSimpleFieldUseCase(
            string = state.value.firstName
        )
        val lastNameResult = validateSimpleFieldUseCase(
            string = state.value.lastName
        )

        state.value = state.value.copy(
            firstNameError = when (firstNameResult.errorType) {
                InputErrorType.FIELD_EMPTY -> R.string.errorFirstNameEmpty
                else -> null
            },
            lastNameError = when (lastNameResult.errorType) {
                InputErrorType.FIELD_EMPTY -> R.string.errorLastNameEmpty
                else -> null
            }
        )

        listOf(
            firstNameResult,
            lastNameResult
        ).any { inputResult ->
            !inputResult.successful
        }.also { hasError ->
            if (hasError) return
        }

        val customer = Customer(
            id = state.value.id,
            firstName = state.value.firstName,
            lastName = state.value.lastName,
            isActive = state.value.isActive,
            addedAt = state.value.addedAt.takeIf { it != 0L } ?: System.currentTimeMillis()
        )

        viewModelScope.launch {
            customersDataSource.save(customer = customer)
        }
        navBackToHomeScreen()
    }

    private fun navBackToHomeScreen() = viewModelScope.launch {
        _channel.send(element = AddEditCustomerChannel.NavBackToHomeScreen)
    }
}