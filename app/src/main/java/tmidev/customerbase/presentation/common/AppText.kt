package tmidev.customerbase.presentation.common

import androidx.annotation.StringRes
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import tmidev.customerbase.presentation.common.theme.spacing

@Composable
fun AppTextWithLabel(
    modifier: Modifier = Modifier,
    @StringRes labelRes: Int,
    text: String,
    textColor: Color = MaterialTheme.colors.onBackground,
    textAlign: Alignment.Horizontal = Alignment.Start
) = Column(modifier = modifier) {
    Text(
        modifier = Modifier.align(alignment = textAlign),
        text = stringResource(id = labelRes),
        color = textColor.copy(alpha = 0.8F),
        style = MaterialTheme.typography.overline
    )

    Text(
        modifier = Modifier
            .padding(
                horizontal = MaterialTheme.spacing.extraSmall,
                vertical = MaterialTheme.spacing.none
            )
            .align(alignment = textAlign),
        text = text,
        color = textColor,
        style = MaterialTheme.typography.body1
    )
}