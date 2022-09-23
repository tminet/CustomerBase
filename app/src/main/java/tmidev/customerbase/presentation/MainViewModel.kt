package tmidev.customerbase.presentation

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import tmidev.core.data.source.local.UserPreferencesDataSource
import tmidev.core.util.CoroutinesDispatchers
import javax.inject.Inject

data class MainScreenState(
    val isLoading: Boolean,
    val isAppThemeDarkMode: Boolean?
)

private data class MainViewModelState(
    val isLoading: Boolean = true,
    val isAppThemeDarkMode: Boolean? = null
) {
    fun toMainScreenState() = MainScreenState(
        isLoading = isLoading,
        isAppThemeDarkMode = isAppThemeDarkMode
    )
}

@HiltViewModel
class MainViewModel @Inject constructor(
    private val coroutinesDispatchers: CoroutinesDispatchers,
    private val userPreferencesDataSource: UserPreferencesDataSource
) : ViewModel() {
    private val viewModelState = MutableStateFlow(value = MainViewModelState())

    val screenState = viewModelState.map { it.toMainScreenState() }.stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(),
        initialValue = viewModelState.value.toMainScreenState()
    )

    init {
        loadInitialSettings()
    }

    private fun loadInitialSettings() {
        viewModelScope.launch(context = coroutinesDispatchers.main) {
            userPreferencesDataSource.isAppThemeDarkMode.collectLatest { isDark ->
                viewModelState.update { it.copy(isAppThemeDarkMode = isDark) }
                delayToShowCustomAnimationOnAppStartup()
            }
        }
    }

    private suspend fun delayToShowCustomAnimationOnAppStartup() {
        if (viewModelState.value.isLoading) {
            delay(timeMillis = 1500)
            viewModelState.update { it.copy(isLoading = false) }
        }
    }
}