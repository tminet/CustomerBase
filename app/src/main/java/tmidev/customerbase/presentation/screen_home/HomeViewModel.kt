package tmidev.customerbase.presentation.screen_home

import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.launch
import tmidev.core.data.source.local.CustomersDataSource
import tmidev.core.data.source.local.UserPreferencesDataSource
import tmidev.core.domain.model.Customer
import javax.inject.Inject

@HiltViewModel
class HomeViewModel @Inject constructor(
    private val userPreferencesDataSource: UserPreferencesDataSource,
    private val customersDataSource: CustomersDataSource
) : ViewModel() {
    var state = mutableStateOf(value = HomeState())
        private set

    private val searchQuery = MutableStateFlow(value = state.value.query)

    private val _channel = Channel<HomeChannel>()
    val channel = _channel.receiveAsFlow()

    init {
        getCustomers()
    }

    fun onAction(action: HomeAction) {
        when (action) {
            is HomeAction.OpenDrawer -> openDrawer()
            is HomeAction.NavToSettingsScreen -> navToSettingsScreen()
            is HomeAction.SwitchListOrder -> switchListOrder()
            is HomeAction.NavToAddEditCustomerScreen -> {
                navToAddEditCustomerScreen(customerId = action.customerId)
            }
            is HomeAction.DeleteCustomer -> {
                deleteCustomer(customer = action.customer)
            }
            is HomeAction.QueryChanged -> updateSearchQuery(query = action.query)
        }
    }

    @OptIn(ExperimentalCoroutinesApi::class)
    private fun getCustomers() = viewModelScope.launch {
        combine(searchQuery, userPreferencesDataSource.isOrderListAscending) { query, isAscending ->
            query to isAscending
        }.flatMapLatest { (query, isAscending) ->
            customersDataSource.getAll(query = query, isAscending = isAscending)
        }.collectLatest { customers ->
            state.value = state.value.copy(
                isLoading = false,
                customers = customers
            )
        }
    }

    private fun openDrawer() = viewModelScope.launch {
        _channel.send(element = HomeChannel.OpenDrawer)
    }

    private fun navToSettingsScreen() = viewModelScope.launch {
        _channel.send(element = HomeChannel.NavToSettingsScreen)
    }

    private fun navToAddEditCustomerScreen(customerId: Int?) = viewModelScope.launch {
        _channel.send(element = HomeChannel.NavToAddEditCustomerScreen(customerId = customerId))
    }

    private fun deleteCustomer(customer: Customer) = viewModelScope.launch {
        customersDataSource.delete(customer = customer)
    }

    private fun switchListOrder() = viewModelScope.launch {
        userPreferencesDataSource.switchOrderList()
    }

    private fun updateSearchQuery(query: String) {
        state.value = state.value.copy(query = query)
        searchQuery.value = query
    }
}