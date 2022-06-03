package tmidev.customerbase.framework.hilt

import android.content.Context
import androidx.room.Room
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import tmidev.core.util.ConstantsRoom
import tmidev.customerbase.framework.room.AppDatabase
import tmidev.customerbase.framework.room.customer.CustomerDao
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object RoomModule {
    @Provides
    @Singleton
    fun provideAppDatabase(
        @ApplicationContext context: Context
    ): AppDatabase = Room.databaseBuilder(
        context,
        AppDatabase::class.java,
        ConstantsRoom.DATABASE_NAME
    ).build()

    @Provides
    @Singleton
    fun provideCustomerDao(
        appDatabase: AppDatabase
    ): CustomerDao = appDatabase.customerDao()
}