package tmidev.customerbase.presentation.screen_home

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import tmidev.core.data.source.local.CustomersDataSource
import tmidev.core.data.source.local.UserPreferencesDataSource
import tmidev.core.domain.model.Customer
import tmidev.core.util.CoroutinesDispatchers
import javax.inject.Inject

sealed class HomeChannel {
    object OpenDrawer : HomeChannel()
    object NavToSettingsScreen : HomeChannel()
    data class NavToAddEditCustomerScreen(val customerId: Int?) : HomeChannel()
}

data class HomeScreenState(
    val isLoading: Boolean,
    val query: String,
    val customers: List<Customer>
)

private data class HomeViewModelState(
    val isLoading: Boolean = true,
    val query: String = "",
    val customers: List<Customer> = emptyList()
) {
    fun toHomeScreenState() = HomeScreenState(
        isLoading = isLoading,
        query = query,
        customers = customers
    )
}

@HiltViewModel
class HomeViewModel @Inject constructor(
    private val coroutinesDispatchers: CoroutinesDispatchers,
    private val userPreferencesDataSource: UserPreferencesDataSource,
    private val customersDataSource: CustomersDataSource
) : ViewModel() {
    private val viewModelState = MutableStateFlow(value = HomeViewModelState())

    val screenState = viewModelState.map { it.toHomeScreenState() }.stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(),
        initialValue = viewModelState.value.toHomeScreenState()
    )

    private val searchQuery = MutableStateFlow(value = viewModelState.value.query)

    private val _channel = Channel<HomeChannel>()
    val channel = _channel.receiveAsFlow()

    init {
        getCustomers()
    }

    @OptIn(ExperimentalCoroutinesApi::class)
    private fun getCustomers() {
        viewModelScope.launch(context = coroutinesDispatchers.main) {
            combine(
                searchQuery,
                userPreferencesDataSource.isOrderListAscending
            ) { query, isAscending ->
                query to isAscending
            }.flatMapLatest { (query, isAscending) ->
                customersDataSource.getAll(query = query, isAscending = isAscending)
            }.collectLatest { customers ->
                viewModelState.update { state ->
                    state.copy(
                        isLoading = false,
                        customers = customers
                    )
                }
            }
        }
    }

    fun openDrawer() {
        viewModelScope.launch(context = coroutinesDispatchers.main) {
            _channel.send(element = HomeChannel.OpenDrawer)
        }
    }

    fun navToSettingsScreen() {
        viewModelScope.launch(context = coroutinesDispatchers.main) {
            _channel.send(element = HomeChannel.NavToSettingsScreen)
        }
    }

    fun navToAddEditCustomerScreen(customerId: Int?) {
        viewModelScope.launch(context = coroutinesDispatchers.main) {
            _channel.send(element = HomeChannel.NavToAddEditCustomerScreen(customerId = customerId))
        }
    }

    fun deleteCustomer(customer: Customer) {
        viewModelScope.launch(context = coroutinesDispatchers.main) {
            customersDataSource.delete(customer = customer)
        }
    }

    fun switchListOrder() {
        viewModelScope.launch(context = coroutinesDispatchers.main) {
            userPreferencesDataSource.switchOrderList()
        }
    }

    fun updateSearchQuery(query: String) {
        viewModelState.update { it.copy(query = query) }
        searchQuery.update { query }
    }
}