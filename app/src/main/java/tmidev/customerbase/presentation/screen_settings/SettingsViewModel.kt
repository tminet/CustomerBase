package tmidev.customerbase.presentation.screen_settings

import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.launch
import tmidev.core.data.source.local.UserPreferencesDataSource
import javax.inject.Inject

@HiltViewModel
class SettingsViewModel @Inject constructor(
    private val userPreferencesDataSource: UserPreferencesDataSource
) : ViewModel() {
    var state = mutableStateOf(value = SettingsState())
        private set

    private val _channel = Channel<SettingsChannel>()
    val channel = _channel.receiveAsFlow()

    init {
        loadAppTheme()
    }

    fun onAction(action: SettingsAction) {
        when (action) {
            is SettingsAction.NavBackToHomeScreen -> navBackToHomeScreen()
            is SettingsAction.AppThemeChanged -> updateAppTheme(isDarkTheme = action.isDarkTheme)
        }
    }

    private fun loadAppTheme() = viewModelScope.launch {
        userPreferencesDataSource.isAppThemeDarkMode.collectLatest { isDark ->
            state.value = state.value.copy(
                isLoading = false,
                isAppThemeDarkMode = isDark
            )
        }
    }

    private fun navBackToHomeScreen() = viewModelScope.launch {
        _channel.send(element = SettingsChannel.NavBackToHomeScreen)
    }

    private fun updateAppTheme(isDarkTheme: Boolean) = viewModelScope.launch {
        userPreferencesDataSource.updateAppTheme(darkMode = isDarkTheme)
    }
}