package tmidev.customerbase.framework.hilt

import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import tmidev.core.data.source.local.CustomersDataSource
import tmidev.core.data.source.local.UserPreferencesDataSource
import tmidev.customerbase.framework.datastore.UserPreferencesDataSourceImpl
import tmidev.customerbase.framework.room.CustomersDataSourceImpl
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
interface DataSourceModule {
    @Binds
    @Singleton
    fun bindUserPreferences(
        dataSource: UserPreferencesDataSourceImpl
    ): UserPreferencesDataSource

    @Binds
    @Singleton
    fun bindCustomers(
        dataSource: CustomersDataSourceImpl
    ): CustomersDataSource
}