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
import androidx.compose.material.Card
import androidx.compose.material.Icon
import androidx.compose.material.IconButton
import androidx.compose.material.LinearProgressIndicator
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Scaffold
import androidx.compose.material.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.Add
import androidx.compose.material.icons.rounded.Delete
import androidx.compose.material.icons.rounded.Edit
import androidx.compose.material.icons.rounded.Settings
import androidx.compose.material.icons.rounded.Sort
import androidx.compose.material.rememberScaffoldState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.focus.FocusManager
import androidx.compose.ui.graphics.RectangleShape
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardCapitalization
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import tmidev.core.domain.model.Customer
import tmidev.customerbase.R
import tmidev.customerbase.presentation.common.AppDrawerHeader
import tmidev.customerbase.presentation.common.AppDrawerMenu
import tmidev.customerbase.presentation.common.AppFloatingActionButton
import tmidev.customerbase.presentation.common.AppOutlinedTextField
import tmidev.customerbase.presentation.common.AppTextWithLabel
import tmidev.customerbase.presentation.common.AppTopBarWithDrawer
import tmidev.customerbase.presentation.common.MenuItem
import tmidev.customerbase.presentation.common.theme.activeColor
import tmidev.customerbase.presentation.common.theme.elevating
import tmidev.customerbase.presentation.common.theme.inactiveColor
import tmidev.customerbase.presentation.common.theme.spacing
import tmidev.customerbase.util.toFormattedDate

@Composable
fun HomeScreen(
    navToAddEditCustomerScreen: (String) -> Unit,
    navToSettingsScreen: () -> Unit,
    viewModel: HomeViewModel = hiltViewModel()
) {
    val state by viewModel.screenState.collectAsState()
    val scaffoldState = rememberScaffoldState()

    LaunchedEffect(key1 = Unit) {
        viewModel.channel.collect { channel ->
            when (channel) {
                is HomeChannel.OpenDrawer -> scaffoldState.drawerState.open()
                is HomeChannel.NavToSettingsScreen -> navToSettingsScreen()
                is HomeChannel.NavToAddEditCustomerScreen -> navToAddEditCustomerScreen(
                    channel.customerId.toString()
                )
            }
        }
    }

    Scaffold(
        modifier = Modifier.fillMaxSize(),
        scaffoldState = scaffoldState,
        topBar = {
            AppTopBarWithDrawer(title = R.string.titleHomeScreen) {
                viewModel.openDrawer()
            }
        },
        floatingActionButton = {
            AppFloatingActionButton(
                onClick = {
                    viewModel.navToAddEditCustomerScreen(customerId = null)
                },
                icon = Icons.Rounded.Add
            )
        },
        drawerGesturesEnabled = scaffoldState.drawerState.isOpen,
        drawerShape = RectangleShape,
        drawerElevation = MaterialTheme.elevating.extraLow,
        drawerBackgroundColor = MaterialTheme.colors.surface,
        drawerContentColor = MaterialTheme.colors.onSurface,
        drawerContent = {
            AppDrawerHeader()

            AppDrawerMenu(
                items = listOf(
                    MenuItem(
                        id = "settings",
                        title = R.string.settings,
                        icon = Icons.Rounded.Settings
                    )
                ),
                onClick = {
                    when (it.id) {
                        "settings" -> viewModel.navToSettingsScreen()
                    }
                }
            )
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

        Spacer(modifier = Modifier.height(height = MaterialTheme.spacing.small))

        Text(
            text = stringResource(id = R.string.loading),
            color = MaterialTheme.colors.onBackground,
            style = MaterialTheme.typography.body2
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
    Spacer(modifier = Modifier.height(height = MaterialTheme.spacing.small))

    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = MaterialTheme.spacing.medium),
        verticalAlignment = Alignment.CenterVertically
    ) {
        AppOutlinedTextField(
            modifier = Modifier.weight(weight = 1F),
            value = query,
            onValueChange = { onQueryChanged(it) },
            labelRes = R.string.labelSearch,
            placeholderRes = R.string.placeholderSearch,
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

        Spacer(modifier = Modifier.width(width = MaterialTheme.spacing.small))

        IconButton(onClick = onSwitchListClick) {
            Icon(
                imageVector = Icons.Rounded.Sort,
                contentDescription = stringResource(id = R.string.sortOrder),
                tint = MaterialTheme.colors.onBackground
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
            .padding(all = MaterialTheme.spacing.medium),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            text = stringResource(id = R.string.nothingToShow),
            color = MaterialTheme.colors.onBackground,
            style = MaterialTheme.typography.h6
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
    contentPadding = PaddingValues(all = MaterialTheme.spacing.medium),
    verticalArrangement = Arrangement.spacedBy(space = MaterialTheme.spacing.medium)
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
        .animateItemPlacement(),
    shape = MaterialTheme.shapes.large,
    elevation = MaterialTheme.elevating.none,
    backgroundColor = MaterialTheme.colors.surface
) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(all = MaterialTheme.spacing.medium)
    ) {
        Row(modifier = Modifier.fillMaxWidth()) {
            Column(modifier = Modifier.weight(weight = 1F)) {
                AppTextWithLabel(
                    labelRes = R.string.firstName,
                    text = customer.firstName,
                    textColor = MaterialTheme.colors.onSurface
                )

                Spacer(modifier = Modifier.height(height = MaterialTheme.spacing.small))

                AppTextWithLabel(
                    labelRes = R.string.lastName,
                    text = customer.lastName,
                    textColor = MaterialTheme.colors.onSurface
                )
            }

            Box(
                modifier = Modifier
                    .size(size = MaterialTheme.spacing.medium)
                    .clip(shape = CircleShape)
                    .background(
                        color = if (customer.isActive)
                            MaterialTheme.colors.activeColor else MaterialTheme.colors.inactiveColor
                    )
            )
        }

        Spacer(modifier = Modifier.height(height = MaterialTheme.spacing.small))

        AppTextWithLabel(
            modifier = Modifier.fillMaxWidth(),
            labelRes = R.string.addedAt,
            text = customer.addedAt.toFormattedDate(),
            textColor = MaterialTheme.colors.onSurface,
            textAlign = Alignment.End
        )

        Spacer(modifier = Modifier.height(height = MaterialTheme.spacing.small))

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
                    contentDescription = Icons.Rounded.Edit.name,
                    tint = MaterialTheme.colors.onSurface
                )
            }

            IconButton(
                onClick = { onDelete(customer) }
            ) {
                Icon(
                    imageVector = Icons.Rounded.Delete,
                    contentDescription = Icons.Rounded.Delete.name,
                    tint = MaterialTheme.colors.onSurface
                )
            }
        }
    }
}