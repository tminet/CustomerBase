package tmidev.customerbase.presentation.common

import androidx.annotation.StringRes
import androidx.compose.material.Button
import androidx.compose.material.ButtonDefaults
import androidx.compose.material.ContentAlpha
import androidx.compose.material.MaterialTheme
import androidx.compose.material.OutlinedButton
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import tmidev.customerbase.presentation.common.theme.elevating

@Composable
fun AppButton(
    modifier: Modifier = Modifier,
    @StringRes stringRes: Int,
    enabled: Boolean = true,
    backgroundColor: Color = MaterialTheme.colors.primary,
    contentColor: Color = MaterialTheme.colors.onPrimary,
    onClick: () -> Unit
) = Button(
    modifier = modifier,
    onClick = onClick,
    enabled = enabled,
    shape = MaterialTheme.shapes.large,
    colors = ButtonDefaults.buttonColors(
        backgroundColor = backgroundColor,
        contentColor = contentColor,
        disabledBackgroundColor = backgroundColor.copy(alpha = ContentAlpha.disabled),
        disabledContentColor = contentColor.copy(alpha = ContentAlpha.disabled)
    ),
    elevation = ButtonDefaults.elevation(
        defaultElevation = MaterialTheme.elevating.none,
        pressedElevation = MaterialTheme.elevating.none,
        disabledElevation = MaterialTheme.elevating.none,
        hoveredElevation = MaterialTheme.elevating.none,
        focusedElevation = MaterialTheme.elevating.none
    )
) {
    Text(text = stringResource(id = stringRes))
}

@Composable
fun AppButtonOutlined(
    modifier: Modifier = Modifier,
    @StringRes stringRes: Int,
    enabled: Boolean = true,
    backgroundColor: Color = MaterialTheme.colors.background,
    contentColor: Color = MaterialTheme.colors.primary,
    onClick: () -> Unit
) = OutlinedButton(
    modifier = modifier,
    onClick = onClick,
    enabled = enabled,
    shape = MaterialTheme.shapes.large,
    colors = ButtonDefaults.outlinedButtonColors(
        backgroundColor = backgroundColor,
        contentColor = contentColor,
        disabledContentColor = contentColor.copy(alpha = ContentAlpha.disabled)
    ),
    elevation = ButtonDefaults.elevation(
        defaultElevation = MaterialTheme.elevating.none,
        pressedElevation = MaterialTheme.elevating.none,
        disabledElevation = MaterialTheme.elevating.none,
        hoveredElevation = MaterialTheme.elevating.none,
        focusedElevation = MaterialTheme.elevating.none
    )
) {
    Text(text = stringResource(id = stringRes))
}