package tmidev.customerbase.framework.room

import androidx.room.Database
import androidx.room.RoomDatabase
import tmidev.customerbase.framework.room.customer.CustomerDao
import tmidev.customerbase.framework.room.customer.CustomerEntity

@Database(
    entities = [CustomerEntity::class],
    version = 1,
    exportSchema = false
)
abstract class AppDatabase : RoomDatabase() {
    abstract fun customerDao(): CustomerDao
}