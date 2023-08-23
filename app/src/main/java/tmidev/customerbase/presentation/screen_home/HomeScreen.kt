package tmidev.customerbase.presentation.screen_home

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.tween
import androidx.compose.animation.expandVertically
import androidx.compose.animation.shrinkVertically
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyItemScope
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.Add
import androidx.compose.material.icons.rounded.Delete
import androidx.compose.material.icons.rounded.Edit
import androidx.compose.material.icons.rounded.Settings
import androidx.compose.material.icons.rounded.Sort
import androidx.compose.material3.Card
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.focus.FocusManager
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardCapitalization
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import tmidev.core.domain.model.Customer
import tmidev.customerbase.R
import tmidev.customerbase.presentation.common.AppOutlinedTextField
import tmidev.customerbase.presentation.common.AppTextWithLabel
import tmidev.customerbase.presentation.common.ClearTrailingTextField
import tmidev.customerbase.presentation.common.theme.customColors
import tmidev.customerbase.util.toFormattedDate

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HomeScreen(
    modifier: Modifier = Modifier,
    navToAddEditCustomerScreen: (String) -> Unit,
    navToSettingsScreen: () -> Unit,
    viewModel: HomeViewModel = hiltViewModel()
) {
    val state by viewModel.screenState.collectAsStateWithLifecycle()

    LaunchedEffect(key1 = Unit) {
        viewModel.channel.collect { channel ->
            when (channel) {
                is HomeChannel.NavToSettingsScreen -> navToSettingsScreen()
                is HomeChannel.NavToAddEditCustomerScreen -> navToAddEditCustomerScreen(
                    channel.customerId.toString()
                )
            }
        }
    }

    Scaffold(
        modifier = modifier,
        topBar = {
            TopAppBar(
                title = {
                    Text(text = stringResource(id = R.string.titleHomeScreen))
                },
                actions = {
                    IconButton(
                        onClick = { navToSettingsScreen() }
                    ) {
                        Icon(
                            imageVector = Icons.Rounded.Settings,
                            contentDescription = stringResource(id = R.string.settings)
                        )
                    }
                }
            )
        },
        floatingActionButton = {
            FloatingActionButton(
                onClick = { viewModel.navToAddEditCustomerScreen(customerId = null) }
            ) {
                Icon(
                    imageVector = Icons.Rounded.Add,
                    contentDescription = stringResource(id = R.string.addCustomer)
                )
            }
        }
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues = innerPadding)
        ) {
            ComposeLoading(isLoading = state.isLoading)

            ComposeSearchField(
                query = state.query,
                onQueryChanged = { query ->
                    viewModel.updateSearchQuery(query = query)
                },
                onSwitchListClick = {
                    viewModel.switchListOrder()
                }
            )

            ComposeEmptyMessage(isEmpty = state.customers.isEmpty())

            ComposeCustomersList(
                customers = state.customers,
                onEditCustomer = { customerId ->
                    viewModel.navToAddEditCustomerScreen(customerId = customerId)
                },
                onDeleteCustomer = { customer ->
                    viewModel.deleteCustomer(customer = customer)
                }
            )
        }
    }
}

@Composable
private fun ComposeLoading(
    isLoading: Boolean
) = AnimatedVisibility(
    modifier = Modifier.fillMaxWidth(),
    visible = isLoading,
    enter = expandVertically(animationSpec = tween()),
    exit = shrinkVertically(animationSpec = tween())
) {
    Column(
        modifier = Modifier.fillMaxWidth(),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        LinearProgressIndicator(modifier = Modifier.fillMaxWidth())

        Spacer(modifier = Modifier.height(height = 4.dp))

        Text(
            text = stringResource(id = R.string.loading),
            style = MaterialTheme.typography.bodyMedium
        )
    }
}

@Composable
private fun ComposeSearchField(
    focusManager: FocusManager = LocalFocusManager.current,
    query: String,
    onQueryChanged: (String) -> Unit,
    onSwitchListClick: () -> Unit
) = Column(modifier = Modifier.fillMaxWidth()) {
    Spacer(modifier = Modifier.height(height = 4.dp))

    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        AppOutlinedTextField(
            modifier = Modifier.weight(weight = 1F),
            value = query,
            onValueChange = { onQueryChanged(it) },
            strictString = false,
            labelRes = R.string.labelSearch,
            placeholderRes = R.string.placeholderSearch,
            trailingIcon = if (query.isEmpty()) null else {
                { ClearTrailingTextField { onQueryChanged("") } }
            },
            keyboardOptions = KeyboardOptions(
                capitalization = KeyboardCapitalization.Words,
                keyboardType = KeyboardType.Text,
                imeAction = ImeAction.Search
            ),
            keyboardActions = KeyboardActions(
                onSearch = {
                    focusManager.clearFocus()
                }
            )
        )

        Spacer(modifier = Modifier.width(width = 8.dp))

        IconButton(onClick = onSwitchListClick) {
            Icon(
                imageVector = Icons.Rounded.Sort,
                contentDescription = stringResource(id = R.string.sortOrder)
            )
        }
    }
}

@Composable
private fun ComposeEmptyMessage(
    isEmpty: Boolean
) = AnimatedVisibility(
    modifier = Modifier.fillMaxWidth(),
    visible = isEmpty,
    enter = expandVertically(animationSpec = tween()),
    exit = shrinkVertically(animationSpec = tween())
) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(all = 16.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            text = stringResource(id = R.string.nothingToShow),
            style = MaterialTheme.typography.titleMedium
        )
    }
}

@Composable
private fun ComposeCustomersList(
    customers: List<Customer>,
    onEditCustomer: (Int) -> Unit,
    onDeleteCustomer: (Customer) -> Unit
) = LazyColumn(
    modifier = Modifier.fillMaxWidth(),
    contentPadding = PaddingValues(all = 16.dp),
    verticalArrangement = Arrangement.spacedBy(space = 16.dp)
) {
    items(
        items = customers,
        key = { it.id }
    ) { customer ->
        ComposeCustomerCard(
            customer = customer,
            onEdit = { onEditCustomer(it) },
            onDelete = { onDeleteCustomer(it) }
        )
    }

    item {
        Spacer(modifier = Modifier.height(height = 50.dp))
    }
}

@OptIn(ExperimentalFoundationApi::class)
@Composable
private fun LazyItemScope.ComposeCustomerCard(
    customer: Customer,
    onEdit: (Int) -> Unit,
    onDelete: (Customer) -> Unit
) = Card(
    modifier = Modifier
        .fillMaxWidth()
        .animateItemPlacement()
) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(all = 16.dp)
    ) {
        Row(modifier = Modifier.fillMaxWidth()) {
            Column(modifier = Modifier.weight(weight = 1F)) {
                AppTextWithLabel(
                    labelRes = R.string.firstName,
                    text = customer.firstName
                )

                Spacer(modifier = Modifier.height(height = 8.dp))

                AppTextWithLabel(
                    labelRes = R.string.lastName,
                    text = customer.lastName
                )
            }

            Box(
                modifier = Modifier
                    .size(size = 16.dp)
                    .clip(shape = CircleShape)
                    .background(
                        color = if (customer.isActive) MaterialTheme.customColors.activeStatus
                        else MaterialTheme.customColors.inactiveStatus
                    )
            )
        }

        Spacer(modifier = Modifier.height(height = 8.dp))

        AppTextWithLabel(
            modifier = Modifier.fillMaxWidth(),
            labelRes = R.string.addedAt,
            text = customer.addedAt.toFormattedDate(),
            textAlign = Alignment.End
        )

        Spacer(modifier = Modifier.height(height = 8.dp))

        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.End,
            verticalAlignment = Alignment.CenterVertically
        ) {
            IconButton(
                onClick = { onEdit(customer.id) }
            ) {
                Icon(
                    imageVector = Icons.Rounded.Edit,
                    contentDescription = stringResource(id = R.string.edit)
                )
            }

            IconButton(
                onClick = { onDelete(customer) }
            ) {
                Icon(
                    imageVector = Icons.Rounded.Delete,
                    contentDescription = stringResource(id = R.string.delete)
                )
            }
        }
    }
}