package tmidev.customerbase.presentation

import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import tmidev.core.data.source.local.UserPreferencesDataSource
import javax.inject.Inject

@HiltViewModel
class MainViewModel @Inject constructor(
    private val userPreferencesDataSource: UserPreferencesDataSource
) : ViewModel() {
    var state = mutableStateOf(value = MainState())
        private set

    init {
        loadInitialSettings()
    }

    private fun loadInitialSettings() = viewModelScope.launch {
        userPreferencesDataSource.isAppThemeDarkMode.collectLatest { isDark ->
            state.value = state.value.copy(isAppThemeDarkMode = isDark)
            delayToShowCustomAnimationOnAppStartup()
        }
    }

    private suspend fun delayToShowCustomAnimationOnAppStartup() {
        if (state.value.isLoading) {
            delay(timeMillis = 1500)
            state.value = state.value.copy(isLoading = false)
        }
    }
}