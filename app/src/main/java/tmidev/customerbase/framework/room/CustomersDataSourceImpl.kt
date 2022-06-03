package tmidev.customerbase.framework.room

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import tmidev.core.data.source.local.CustomersDataSource
import tmidev.core.domain.model.Customer
import tmidev.customerbase.framework.room.customer.CustomerDao
import tmidev.customerbase.framework.room.customer.CustomerEntity
import javax.inject.Inject

class CustomersDataSourceImpl @Inject constructor(
    private val customerDao: CustomerDao
) : CustomersDataSource {
    override fun getAll(query: String): Flow<List<Customer>> =
        customerDao.getAll(query = query).map { it.toCustomers() }

    override fun getById(customerId: Int): Flow<Customer> =
        customerDao.getById(customerId = customerId).map { it.toCustomer() }

    override suspend fun save(customer: Customer) =
        customerDao.save(customerEntity = customer.toCustomerEntity())

    override suspend fun delete(customer: Customer) =
        customerDao.delete(customerEntity = customer.toCustomerEntity())

    private fun List<CustomerEntity>.toCustomers() = map { customerEntity ->
        customerEntity.toCustomer()
    }

    private fun CustomerEntity.toCustomer() = Customer(
        firstName = firstName,
        lastName = lastName,
        isActive = isActive,
        addedAt = addedAt,
        id = id
    )

    private fun Customer.toCustomerEntity() = CustomerEntity(
        firstName = firstName,
        lastName = lastName,
        isActive = isActive,
        addedAt = addedAt,
        id = id
    )
}
