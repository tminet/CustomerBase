package tmidev.core.data.source.local

import kotlinx.coroutines.flow.Flow

interface UserPreferencesDataSource {
    val isAppThemeDarkMode: Flow<Boolean?>
    suspend fun updateAppTheme(darkMode: Boolean)
}