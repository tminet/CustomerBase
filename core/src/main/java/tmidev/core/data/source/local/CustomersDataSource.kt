package tmidev.core.data.source.local

import kotlinx.coroutines.flow.Flow
import tmidev.core.domain.model.Customer

interface CustomersDataSource {
    fun getAll(query: String): Flow<List<Customer>>
    fun getById(customerId: Int): Flow<Customer>
    suspend fun save(customer: Customer)
    suspend fun delete(customer: Customer)
}