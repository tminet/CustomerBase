package tmidev.customerbase.framework.room.customer

import androidx.room.Entity
import androidx.room.PrimaryKey
import tmidev.core.util.ConstantsRoom

@Entity(tableName = ConstantsRoom.TABLE_CUSTOMER)
data class CustomerEntity(
    val firstName: String,
    val lastName: String,
    val isActive: Boolean,
    val addedAt: Long,
    @PrimaryKey(autoGenerate = true) val id: Int
)