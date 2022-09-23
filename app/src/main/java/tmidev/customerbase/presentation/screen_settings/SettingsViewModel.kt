package tmidev.customerbase.presentation.screen_settings

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import tmidev.core.data.source.local.UserPreferencesDataSource
import tmidev.core.util.CoroutinesDispatchers
import javax.inject.Inject

sealed class SettingsChannel {
    object NavBackToHomeScreen : SettingsChannel()
}

data class SettingsScreenState(
    val isLoading: Boolean,
    val isAppThemeDarkMode: Boolean?
)

private data class SettingsViewModelState(
    val isLoading: Boolean = true,
    val isAppThemeDarkMode: Boolean? = null
) {
    fun toSettingsScreenState() = SettingsScreenState(
        isLoading = isLoading,
        isAppThemeDarkMode = isAppThemeDarkMode
    )
}

@HiltViewModel
class SettingsViewModel @Inject constructor(
    private val coroutinesDispatchers: CoroutinesDispatchers,
    private val userPreferencesDataSource: UserPreferencesDataSource
) : ViewModel() {
    private val viewModelState = MutableStateFlow(value = SettingsViewModelState())

    val screenState = viewModelState.map { it.toSettingsScreenState() }.stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(),
        initialValue = viewModelState.value.toSettingsScreenState()
    )

    private val _channel = Channel<SettingsChannel>()
    val channel = _channel.receiveAsFlow()

    init {
        loadAppTheme()
    }

    private fun loadAppTheme() {
        viewModelScope.launch(context = coroutinesDispatchers.main) {
            userPreferencesDataSource.isAppThemeDarkMode.collectLatest { isDark ->
                viewModelState.update { state ->
                    state.copy(
                        isLoading = false,
                        isAppThemeDarkMode = isDark
                    )
                }
            }
        }
    }

    fun navBackToHomeScreen() {
        viewModelScope.launch(context = coroutinesDispatchers.main) {
            _channel.send(element = SettingsChannel.NavBackToHomeScreen)
        }
    }

    fun updateAppTheme(currentMode: Boolean) {
        viewModelScope.launch(context = coroutinesDispatchers.main) {
            userPreferencesDataSource.updateAppTheme(darkMode = !currentMode)
        }
    }
}