package tmidev.customerbase.presentation.common

import androidx.annotation.StringRes
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.Clear
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.text.style.TextAlign
import tmidev.customerbase.R

@Composable
fun AppOutlinedTextField(
    modifier: Modifier = Modifier,
    value: String,
    onValueChange: (String) -> Unit,
    strictString: Boolean,
    @StringRes labelRes: Int,
    @StringRes placeholderRes: Int? = null,
    @StringRes errorRes: Int? = null,
    enabled: Boolean = true,
    readOnly: Boolean = false,
    singleLine: Boolean = true,
    enableWhiteSpace: Boolean = true,
    hideText: Boolean = false,
    leadingIcon: @Composable (() -> Unit)? = null,
    trailingIcon: @Composable (() -> Unit)? = null,
    keyboardOptions: KeyboardOptions = KeyboardOptions.Default,
    keyboardActions: KeyboardActions = KeyboardActions.Default
) = Column(
    modifier = modifier,
    verticalArrangement = Arrangement.Center
) {
    val visualTransformation =
        if (hideText) PasswordVisualTransformation() else VisualTransformation.None

    OutlinedTextField(
        modifier = Modifier.fillMaxWidth(),
        value = value,
        onValueChange = { value ->
            if (strictString) {
                if (enableWhiteSpace) onValueChange(value.clearDoubleWhitespace())
                else onValueChange(value.clearAllWhitespace())
            } else onValueChange(value)
        },
        enabled = enabled,
        readOnly = readOnly,
        label = { Text(text = stringResource(id = labelRes)) },
        placeholder = if (placeholderRes == null) null else {
            { Text(text = stringResource(id = placeholderRes)) }
        },
        leadingIcon = leadingIcon,
        trailingIcon = trailingIcon,
        visualTransformation = visualTransformation,
        singleLine = singleLine,
        keyboardOptions = keyboardOptions,
        keyboardActions = keyboardActions
    )

    errorRes?.let {
        Text(
            modifier = Modifier.fillMaxWidth(),
            text = stringResource(id = it),
            color = MaterialTheme.colorScheme.error,
            textAlign = TextAlign.End,
            style = MaterialTheme.typography.labelMedium
        )
    }
}

@Composable
fun ClearTrailingTextField(
    onClick: () -> Unit
) = IconButton(onClick = onClick) {
    Icon(
        imageVector = Icons.Rounded.Clear,
        contentDescription = stringResource(id = R.string.clear)
    )
}

private fun String.clearDoubleWhitespace(): String {
    val regex = "\\s{2,}".toRegex()
    return this.replace(regex = regex, replacement = " ")
}

private fun String.clearAllWhitespace(): String {
    return this.filterNot { it.isWhitespace() }
}