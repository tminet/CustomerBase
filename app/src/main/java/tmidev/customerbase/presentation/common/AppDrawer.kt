package tmidev.customerbase.presentation.common

import androidx.annotation.StringRes
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.Divider
import androidx.compose.material.Icon
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.stringResource
import tmidev.customerbase.R
import tmidev.customerbase.presentation.common.theme.spacing

data class MenuItem(
    val id: String,
    @StringRes val title: Int,
    val icon: ImageVector
)

@Composable
fun AppDrawerHeader() = Box(
    modifier = Modifier
        .fillMaxWidth()
        .background(color = MaterialTheme.colors.primary)
) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(
                horizontal = MaterialTheme.spacing.medium,
                vertical = MaterialTheme.spacing.large
            ),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            text = stringResource(id = R.string.appName),
            color = MaterialTheme.colors.onPrimary,
            style = MaterialTheme.typography.h5
        )
    }
}

@Composable
fun AppDrawerMenu(
    items: List<MenuItem>,
    onClick: (MenuItem) -> Unit
) = LazyColumn {
    items(items = items, key = { it.id }) { menuItem ->
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .clickable {
                    onClick(menuItem)
                },
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Row(modifier = Modifier.padding(all = MaterialTheme.spacing.medium)) {
                Icon(
                    imageVector = menuItem.icon,
                    contentDescription = stringResource(id = menuItem.title)
                )

                Spacer(modifier = Modifier.width(width = MaterialTheme.spacing.medium))

                Text(
                    modifier = Modifier.weight(weight = 1F),
                    text = stringResource(id = menuItem.title),
                    style = MaterialTheme.typography.body1
                )
            }

            Divider()
        }
    }
}