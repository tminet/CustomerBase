package tmidev.customerbase.presentation.screen_add_edit_customer

import androidx.lifecycle.SavedStateHandle
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
import tmidev.core.data.source.local.CustomersDataSource
import tmidev.core.domain.usecase.ValidateSimpleFieldUseCase
import tmidev.customerbase.DispatcherRule
import tmidev.customerbase.R

@RunWith(MockitoJUnitRunner::class)
class AddEditCustomerViewModelTest {
    @get:Rule
    val dispatcherRule = DispatcherRule()

    private lateinit var viewModel: AddEditCustomerViewModel

    @Mock
    private lateinit var customersDataSource: CustomersDataSource

    @Mock
    lateinit var validateSimpleFieldUseCase: ValidateSimpleFieldUseCase

    private val savedStateHandle = SavedStateHandle()

    private val initialScreenState = AddEditCustomerScreenState(
        id = 0,
        firstName = "",
        lastName = "",
        isActive = true,
        addedAt = 0,
        firstNameError = null,
        lastNameError = null,
        screenTitle = R.string.titleAddCustomerScreen
    )

    @Before
    fun setUp() {
        viewModel = AddEditCustomerViewModel(
            savedStateHandle = savedStateHandle,
            coroutinesDispatchers = dispatcherRule.testDispatchers,
            customersDataSource = customersDataSource,
            validateSimpleFieldUseCase = validateSimpleFieldUseCase
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
        val expectedElement = AddEditCustomerChannel.NavBackToHomeScreen

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