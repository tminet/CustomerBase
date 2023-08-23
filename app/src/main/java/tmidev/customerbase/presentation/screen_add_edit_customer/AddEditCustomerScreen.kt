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
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.NavigateBefore
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Switch
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusDirection
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardCapitalization
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import tmidev.customerbase.R
import tmidev.customerbase.presentation.common.AppOutlinedTextField
import tmidev.customerbase.presentation.common.ClearTrailingTextField

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AddEditCustomerScreen(
    modifier: Modifier = Modifier,
    navBackToHomeScreen: () -> Unit,
    viewModel: AddEditCustomerViewModel = hiltViewModel()
) {
    val state by viewModel.screenState.collectAsStateWithLifecycle()
    val scrollState = rememberScrollState()
    val focusManager = LocalFocusManager.current

    LaunchedEffect(key1 = Unit) {
        viewModel.channel.collect { channel ->
            when (channel) {
                AddEditCustomerChannel.NavBackToHomeScreen -> navBackToHomeScreen()
            }
        }
    }

    Scaffold(
        modifier = modifier,
        topBar = {
            TopAppBar(
                title = {
                    Text(text = stringResource(id = R.string.titleAddCustomerScreen))
                },
                navigationIcon = {
                    IconButton(onClick = viewModel::navBackToHomeScreen) {
                        Icon(
                            imageVector = Icons.Rounded.NavigateBefore,
                            contentDescription = stringResource(id = R.string.navigateBack)
                        )
                    }
                }
            )
        }
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues = innerPadding)
                .verticalScroll(state = scrollState)
        ) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(all = 16.dp)
            ) {
                AppOutlinedTextField(
                    modifier = Modifier.fillMaxWidth(),
                    value = state.firstName,
                    onValueChange = { viewModel.changeFirstName(value = it) },
                    strictString = true,
                    labelRes = R.string.labelFirstName,
                    placeholderRes = R.string.placeholderFirstName,
                    errorRes = state.firstNameError,
                    trailingIcon = if (state.firstName.isEmpty()) null else {
                        { ClearTrailingTextField { viewModel.changeFirstName(value = "") } }
                    },
                    keyboardOptions = KeyboardOptions(
                        capitalization = KeyboardCapitalization.Words,
                        keyboardType = KeyboardType.Text,
                        imeAction = ImeAction.Next
                    ),
                    keyboardActions = KeyboardActions(
                        onNext = { focusManager.moveFocus(focusDirection = FocusDirection.Down) }
                    )
                )

                Spacer(modifier = Modifier.height(height = 16.dp))

                AppOutlinedTextField(
                    modifier = Modifier.fillMaxWidth(),
                    value = state.lastName,
                    onValueChange = { viewModel.changeLastName(value = it) },
                    strictString = true,
                    labelRes = R.string.labelLastName,
                    placeholderRes = R.string.placeholderLastName,
                    errorRes = state.lastNameError,
                    trailingIcon = if (state.lastName.isEmpty()) null else {
                        { ClearTrailingTextField { viewModel.changeLastName(value = "") } }
                    },
                    keyboardOptions = KeyboardOptions(
                        capitalization = KeyboardCapitalization.Words,
                        keyboardType = KeyboardType.Text,
                        imeAction = ImeAction.Done
                    ),
                    keyboardActions = KeyboardActions(
                        onDone = { viewModel.saveCustomer() }
                    )
                )

                Spacer(modifier = Modifier.height(height = 16.dp))

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.End,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = stringResource(
                            id = if (state.isActive) R.string.active else R.string.inactive
                        ),
                        style = MaterialTheme.typography.bodyLarge
                    )

                    Spacer(modifier = Modifier.width(width = 8.dp))

                    Switch(
                        checked = state.isActive,
                        onCheckedChange = { viewModel.changeIsActive(value = it) }
                    )
                }

                Spacer(modifier = Modifier.height(height = 32.dp))

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.End,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    OutlinedButton(onClick = viewModel::navBackToHomeScreen) {
                        Text(text = stringResource(id = R.string.cancel))
                    }

                    Spacer(modifier = Modifier.width(width = 16.dp))

                    OutlinedButton(onClick = viewModel::saveCustomer) {
                        Text(text = stringResource(id = R.string.finish))
                    }
                }
            }
        }
    }
}