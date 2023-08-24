package tmidev.customerbase.presentation.common

import androidx.annotation.StringRes
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp

@Composable
fun AppTextWithLabel(
    modifier: Modifier = Modifier,
    @StringRes labelRes: Int,
    text: String,
    textAlign: Alignment.Horizontal = Alignment.Start
) = Column(modifier = modifier) {
    Text(
        modifier = Modifier.align(alignment = textAlign),
        text = stringResource(id = labelRes),
        style = MaterialTheme.typography.labelMedium
    )

    Text(
        modifier = Modifier
            .padding(horizontal = 4.dp)
            .align(alignment = textAlign),
        text = text,
        style = MaterialTheme.typography.bodyLarge
    )
}