package tmidev.customerbase.presentation.common

import androidx.compose.foundation.layout.size
import androidx.compose.material.FloatingActionButton
import androidx.compose.material.FloatingActionButtonDefaults
import androidx.compose.material.Icon
import androidx.compose.material.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.unit.dp
import tmidev.customerbase.presentation.common.theme.elevating

@Composable
fun AppFloatingActionButton(
    onClick: () -> Unit,
    icon: ImageVector
) = FloatingActionButton(
    modifier = Modifier.size(size = 50.dp),
    onClick = onClick,
    shape = MaterialTheme.shapes.large,
    backgroundColor = MaterialTheme.colors.primary,
    contentColor = MaterialTheme.colors.onPrimary,
    elevation = FloatingActionButtonDefaults.elevation(
        defaultElevation = MaterialTheme.elevating.extraLow,
        pressedElevation = MaterialTheme.elevating.none,
        hoveredElevation = MaterialTheme.elevating.none,
        focusedElevation = MaterialTheme.elevating.none
    )
) {
    Icon(
        imageVector = icon,
        contentDescription = icon.name,
        tint = MaterialTheme.colors.onPrimary
    )
}