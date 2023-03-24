package tmidev.customerbase.presentation.common

import androidx.annotation.StringRes
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.RowScope
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.Icon
import androidx.compose.material.IconButton
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.material.TopAppBar
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.Menu
import androidx.compose.material.icons.rounded.NavigateBefore
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import tmidev.customerbase.R
import tmidev.customerbase.presentation.common.theme.elevating
import tmidev.customerbase.presentation.common.theme.spacing

@Composable
private fun AppTopBar(
    backgroundColor: Color,
    contentColor: Color,
    content: @Composable RowScope.() -> Unit
) = TopAppBar(
    modifier = Modifier.fillMaxWidth(),
    backgroundColor = backgroundColor,
    contentColor = contentColor,
    elevation = MaterialTheme.elevating.none,
    contentPadding = PaddingValues(all = MaterialTheme.spacing.none),
    content = content
)

@Composable
fun AppTopBarWithDrawer(
    @StringRes title: Int,
    backgroundColor: Color = MaterialTheme.colors.primary,
    contentColor: Color = MaterialTheme.colors.onPrimary,
    onDrawer: () -> Unit
) = AppTopBar(
    backgroundColor = backgroundColor,
    contentColor = contentColor
) {
    IconButton(
        modifier = Modifier.padding(horizontal = MaterialTheme.spacing.extraSmall),
        onClick = onDrawer
    ) {
        Icon(
            imageVector = Icons.Rounded.Menu,
            contentDescription = stringResource(id = R.string.openMenu),
            tint = contentColor
        )
    }

    Text(
        modifier = Modifier
            .weight(weight = 1F)
            .padding(end = MaterialTheme.spacing.medium),
        text = stringResource(id = title),
        color = contentColor,
        style = MaterialTheme.typography.h6,
        textAlign = TextAlign.Start,
        overflow = TextOverflow.Ellipsis,
        maxLines = 1
    )
}

@Composable
fun AppTopBarWithBack(
    @StringRes title: Int,
    backgroundColor: Color = MaterialTheme.colors.primary,
    contentColor: Color = MaterialTheme.colors.onPrimary,
    onBack: () -> Unit
) = AppTopBar(
    backgroundColor = backgroundColor,
    contentColor = contentColor
) {
    IconButton(
        modifier = Modifier.padding(horizontal = MaterialTheme.spacing.extraSmall),
        onClick = onBack
    ) {
        Icon(
            imageVector = Icons.Rounded.NavigateBefore,
            contentDescription = stringResource(id = R.string.navigateBack),
            tint = contentColor
        )
    }

    Text(
        modifier = Modifier
            .weight(weight = 1F)
            .padding(end = MaterialTheme.spacing.medium),
        text = stringResource(id = title),
        color = contentColor,
        style = MaterialTheme.typography.h6,
        textAlign = TextAlign.Start,
        overflow = TextOverflow.Ellipsis,
        maxLines = 1
    )
}