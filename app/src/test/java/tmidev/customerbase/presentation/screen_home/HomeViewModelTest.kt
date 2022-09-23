package tmidev.customerbase.presentation.screen_home

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
import tmidev.core.data.source.local.CustomersDataSource
import tmidev.core.data.source.local.UserPreferencesDataSource
import tmidev.customerbase.DispatcherRule

@OptIn(ExperimentalCoroutinesApi::class)
@RunWith(MockitoJUnitRunner::class)
class HomeViewModelTest {
    @get:Rule
    val dispatcherRule = DispatcherRule()

    private lateinit var viewModel: HomeViewModel

    @Mock
    private lateinit var userPreferencesDataSource: UserPreferencesDataSource

    @Mock
    private lateinit var customersDataSource: CustomersDataSource

    private val initialScreenState = HomeScreenState(
        isLoading = true,
        query = "",
        customers = emptyList()
    )

    @Before
    fun setUp() {
        viewModel = HomeViewModel(
            coroutinesDispatchers = dispatcherRule.testDispatchers,
            userPreferencesDataSource = userPreferencesDataSource,
            customersDataSource = customersDataSource
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
    fun `channel receives properly element for openDrawer`() = runTest {
        val expectedElement = HomeChannel.OpenDrawer

        val job = launch {
            viewModel.channel.test {
                val actualElement = awaitItem()
                assertEquals(expectedElement, actualElement)
                cancelAndIgnoreRemainingEvents()
            }
        }

        viewModel.openDrawer()

        job.join()
        job.cancel()
    }

    @Test
    fun `channel receives properly element for navToSettingsScreen`() = runTest {
        val expectedElement = HomeChannel.NavToSettingsScreen

        val job = launch {
            viewModel.channel.test {
                val actualElement = awaitItem()
                assertEquals(expectedElement, actualElement)
                cancelAndIgnoreRemainingEvents()
            }
        }

        viewModel.navToSettingsScreen()

        job.join()
        job.cancel()
    }

    @Test
    fun `channel receives properly element for navToAddEditCustomerScreen`() = runTest {
        val customerId: Int? = null
        val expectedElement = HomeChannel.NavToAddEditCustomerScreen(customerId = customerId)

        val job = launch {
            viewModel.channel.test {
                val actualElement = awaitItem()
                assertEquals(expectedElement, actualElement)
                cancelAndIgnoreRemainingEvents()
            }
        }

        viewModel.navToAddEditCustomerScreen(customerId = customerId)

        job.join()
        job.cancel()
    }
}