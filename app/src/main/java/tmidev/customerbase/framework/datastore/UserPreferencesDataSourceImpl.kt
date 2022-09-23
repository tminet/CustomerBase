package tmidev.customerbase.framework.datastore

import android.util.Log
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.booleanPreferencesKey
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.emptyPreferences
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.flow.map
import tmidev.core.data.source.local.UserPreferencesDataSource
import tmidev.core.util.CoroutinesDispatchers
import javax.inject.Inject

class UserPreferencesDataSourceImpl @Inject constructor(
    coroutinesDispatchers: CoroutinesDispatchers,
    private val dataStore: DataStore<Preferences>
) : UserPreferencesDataSource {
    private val tag = this::class.java.simpleName

    override val isAppThemeDarkMode: Flow<Boolean?> = dataStore.data
        .catch { exception ->
            exception.localizedMessage?.let { Log.e(tag, it) }
            emit(emptyPreferences())
        }
        .map { preferences ->
            preferences[PreferencesKeys.IS_APP_THEME_DARK_MODE]
        }.flowOn(context = coroutinesDispatchers.main)

    override val isOrderListAscending: Flow<Boolean> = dataStore.data
        .catch { exception ->
            exception.localizedMessage?.let { Log.e(tag, it) }
            emit(emptyPreferences())
        }
        .map { preferences ->
            preferences[PreferencesKeys.IS_ORDER_LIST_ASCENDING] ?: true
        }.flowOn(context = coroutinesDispatchers.main)

    override suspend fun updateAppTheme(darkMode: Boolean) {
        dataStore.edit { preferences ->
            preferences[PreferencesKeys.IS_APP_THEME_DARK_MODE] = darkMode
        }
    }

    override suspend fun switchOrderList() {
        dataStore.edit { preferences ->
            val currentValue = preferences[PreferencesKeys.IS_ORDER_LIST_ASCENDING] ?: true
            preferences[PreferencesKeys.IS_ORDER_LIST_ASCENDING] = !currentValue
        }
    }

    private object PreferencesKeys {
        val IS_APP_THEME_DARK_MODE = booleanPreferencesKey(name = "is_app_theme_dark_mode")
        val IS_ORDER_LIST_ASCENDING = booleanPreferencesKey(name = "is_order_list_ascending")
    }
}