package tmidev.core.data.source.local

import kotlinx.coroutines.flow.Flow

interface UserPreferencesDataSource {
    val isAppThemeDarkMode: Flow<Boolean?>
    val isOrderListAscending: Flow<Boolean>
    suspend fun updateAppTheme(darkMode: Boolean)
    suspend fun switchOrderList()
}