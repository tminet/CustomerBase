package tmidev.customerbase.presentation.screen_home

import tmidev.core.domain.model.Customer

data class HomeState(
    val isLoading: Boolean = true,
    val customers: List<Customer> = emptyList()
)