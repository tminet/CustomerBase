package tmidev.customerbase.framework.room

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.flow.map
import tmidev.core.data.source.local.CustomersDataSource
import tmidev.core.domain.model.Customer
import tmidev.core.util.CoroutinesDispatchers
import tmidev.customerbase.framework.room.customer.CustomerDao
import tmidev.customerbase.framework.room.customer.CustomerEntity
import javax.inject.Inject

class CustomersDataSourceImpl @Inject constructor(
    private val coroutinesDispatchers: CoroutinesDispatchers,
    private val customerDao: CustomerDao
) : CustomersDataSource {
    override fun getAll(
        query: String,
        isAscending: Boolean
    ): Flow<List<Customer>> = customerDao.getAll(
        query = query,
        isAscending = isAscending
    ).map { customersEntity ->
        customersEntity.toCustomers()
    }.flowOn(context = coroutinesDispatchers.main)

    override fun getById(
        customerId: Int
    ): Flow<Customer> = customerDao.getById(
        customerId = customerId
    ).map { customerEntity ->
        customerEntity.toCustomer()
    }.flowOn(context = coroutinesDispatchers.main)

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
