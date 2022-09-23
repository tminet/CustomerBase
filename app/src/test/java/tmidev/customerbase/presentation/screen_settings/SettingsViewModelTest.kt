package tmidev.customerbase.presentation.screen_settings

import app.cash.turbine.test
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.launch
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.Mock
import org.mockito.junit.MockitoJUnitRunner
import tmidev.core.data.source.local.UserPreferencesDataSource
import tmidev.customerbase.DispatcherRule

@OptIn(ExperimentalCoroutinesApi::class)
@RunWith(MockitoJUnitRunner::class)
class SettingsViewModelTest {
    @get:Rule
    val dispatcherRule = DispatcherRule()

    private lateinit var viewModel: SettingsViewModel

    @Mock
    private lateinit var userPreferencesDataSource: UserPreferencesDataSource

    private val initialScreenState = SettingsScreenState(
        isLoading = true,
        isAppThemeDarkMode = null
    )

    @Before
    fun setUp() {
        viewModel = SettingsViewModel(
            coroutinesDispatchers = dispatcherRule.testDispatchers,
            userPreferencesDataSource = userPreferencesDataSource
        )
    }

    @Test
    fun `initial screen state`() = runTest {
        val expectedValue = initialScreenState

        val job = launch {
            viewModel.screenState.test {
                val actualValue = awaitItem()
                assertEquals(expectedValue, actualValue)
                cancelAndIgnoreRemainingEvents()
            }
        }

        job.join()
        job.cancel()
    }

    @Test
    fun `channel receives properly element for navBackToHomeScreen`() = runTest {
        val expectedElement = SettingsChannel.NavBackToHomeScreen

        val job = launch {
            viewModel.channel.test {
                val actualElement = awaitItem()
                assertEquals(expectedElement, actualElement)
                cancelAndIgnoreRemainingEvents()
            }
        }

        viewModel.navBackToHomeScreen()

        job.join()
        job.cancel()
    }
}