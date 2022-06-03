package tmidev.customerbase.presentation.screen_add_edit_customer

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Scaffold
import androidx.compose.material.Switch
import androidx.compose.material.Text
import androidx.compose.material.rememberScaffoldState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardCapitalization
import androidx.compose.ui.text.input.KeyboardType
import androidx.hilt.navigation.compose.hiltViewModel
import tmidev.customerbase.R
import tmidev.customerbase.presentation.common.AppButton
import tmidev.customerbase.presentation.common.AppButtonOutlined
import tmidev.customerbase.presentation.common.AppOutlinedTextField
import tmidev.customerbase.presentation.common.AppTopBarWithBack
import tmidev.customerbase.presentation.common.theme.spacing

@Composable
fun AddEditCustomerScreen(
    navBackToHomeScreen: () -> Unit,
    viewModel: AddEditCustomerViewModel = hiltViewModel()
) {
    val state by viewModel.state
    val scaffoldState = rememberScaffoldState()
    val scrollState = rememberScrollState()

    LaunchedEffect(key1 = LocalContext.current) {
        viewModel.channel.collect { channel ->
            when (channel) {
                AddEditCustomerChannel.NavBackToHomeScreen -> navBackToHomeScreen()
            }
        }
    }

    Scaffold(
        modifier = Modifier.fillMaxSize(),
        scaffoldState = scaffoldState,
        topBar = {
            AppTopBarWithBack(title = state.screenTitle) {
                viewModel.onAction(action = AddEditCustomerAction.NavBackToHomeScreen)
            }
        }
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .verticalScroll(state = scrollState)
        ) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(all = MaterialTheme.spacing.medium)
            ) {
                AppOutlinedTextField(
                    modifier = Modifier.fillMaxWidth(),
                    value = state.firstName,
                    onValueChange = {
                        viewModel.onAction(
                            action = AddEditCustomerAction.FirstNameChanged(firstName = it)
                        )
                    },
                    labelRes = R.string.labelFirstName,
                    placeholderRes = R.string.placeholderFirstName,
                    errorRes = state.firstNameError,
                    keyboardOptions = KeyboardOptions(
                        capitalization = KeyboardCapitalization.Words,
                        keyboardType = KeyboardType.Text,
                        imeAction = ImeAction.Next
                    )
                )

                Spacer(modifier = Modifier.height(height = MaterialTheme.spacing.medium))

                AppOutlinedTextField(
                    modifier = Modifier.fillMaxWidth(),
                    value = state.lastName,
                    onValueChange = {
                        viewModel.onAction(
                            action = AddEditCustomerAction.LastNameChanged(lastName = it)
                        )
                    },
                    labelRes = R.string.labelLastName,
                    placeholderRes = R.string.placeholderLastName,
                    errorRes = state.lastNameError,
                    keyboardOptions = KeyboardOptions(
                        capitalization = KeyboardCapitalization.Words,
                        keyboardType = KeyboardType.Text,
                        imeAction = ImeAction.Done
                    ),
                    keyboardActions = KeyboardActions(
                        onDone = {
                            viewModel.onAction(action = AddEditCustomerAction.SaveCustomer)
                        }
                    )
                )

                Spacer(modifier = Modifier.height(height = MaterialTheme.spacing.medium))

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.End,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = stringResource(
                            id = if (state.isActive) R.string.active else R.string.inactive
                        ),
                        style = MaterialTheme.typography.body1
                    )

                    Switch(
                        checked = state.isActive,
                        onCheckedChange = {
                            viewModel.onAction(
                                action = AddEditCustomerAction.IsActiveChanged(isActive = it)
                            )
                        }
                    )
                }

                Spacer(modifier = Modifier.height(height = MaterialTheme.spacing.extraLarge))

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.End,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    AppButtonOutlined(stringRes = R.string.cancel) {
                        viewModel.onAction(action = AddEditCustomerAction.NavBackToHomeScreen)
                    }

                    Spacer(modifier = Modifier.width(width = MaterialTheme.spacing.medium))

                    AppButton(stringRes = R.string.finish) {
                        viewModel.onAction(action = AddEditCustomerAction.SaveCustomer)
                    }
                }
            }
        }
    }
}