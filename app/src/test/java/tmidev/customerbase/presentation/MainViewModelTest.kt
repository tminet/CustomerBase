package tmidev.customerbase.presentation

import app.cash.turbine.test
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

@RunWith(MockitoJUnitRunner::class)
class MainViewModelTest {
    @get:Rule
    val dispatcherRule = DispatcherRule()

    private lateinit var viewModel: MainViewModel

    @Mock
    private lateinit var userPreferencesDataSource: UserPreferencesDataSource

    private val initialScreenState = MainScreenState(
        isLoading = true,
        isAppThemeDarkMode = null
    )

    @Before
    fun setUp() {
        viewModel = MainViewModel(
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
}