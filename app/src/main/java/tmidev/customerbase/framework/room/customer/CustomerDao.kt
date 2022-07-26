package tmidev.customerbase.framework.room.customer

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy.REPLACE
import androidx.room.Query
import kotlinx.coroutines.flow.Flow
import tmidev.core.util.ConstantsRoom

@Dao
interface CustomerDao {
    fun getAll(
        query: String,
        isAscending: Boolean
    ): Flow<List<CustomerEntity>> = if (isAscending)
        getAllAscending(query = query) else getAllDescending(query = query)

    @Query(
        value = """
            SELECT * 
            FROM ${ConstantsRoom.TABLE_CUSTOMER}
            WHERE LOWER(firstName) || ' ' || LOWER(lastName) LIKE '%' || LOWER(:query) || '%'
            ORDER BY addedAt ASC
        """
    )
    fun getAllAscending(query: String): Flow<List<CustomerEntity>>

    @Query(
        value = """
            SELECT * 
            FROM ${ConstantsRoom.TABLE_CUSTOMER}
            WHERE LOWER(firstName) || ' ' || LOWER(lastName) LIKE '%' || LOWER(:query) || '%'
            ORDER BY addedAt DESC
        """
    )
    fun getAllDescending(query: String): Flow<List<CustomerEntity>>

    @Query(
        value = """
            SELECT * 
            FROM ${ConstantsRoom.TABLE_CUSTOMER}
            WHERE id = :customerId
            LIMIT 1
        """
    )
    fun getById(customerId: Int): Flow<CustomerEntity>

    @Insert(onConflict = REPLACE)
    suspend fun save(customerEntity: CustomerEntity)

    @Delete
    suspend fun delete(customerEntity: CustomerEntity)
}