package tmidev.customerbase.presentation.common

import androidx.annotation.StringRes
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.MaterialTheme
import androidx.compose.material.OutlinedTextField
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.text.style.TextAlign

@Composable
fun AppOutlinedTextField(
    modifier: Modifier = Modifier,
    value: String,
    onValueChange: (String) -> Unit,
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
    keyboardActions: KeyboardActions = KeyboardActions.Default,
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
            if (enableWhiteSpace) onValueChange(value)
            else onValueChange(value.filterNot { it.isWhitespace() })
        },
        enabled = enabled,
        readOnly = readOnly,
        label = { Text(text = stringResource(id = labelRes)) },
        placeholder = {
            placeholderRes?.let { Text(text = stringResource(id = placeholderRes)) }
        },
        leadingIcon = leadingIcon,
        trailingIcon = trailingIcon,
        visualTransformation = visualTransformation,
        singleLine = singleLine,
        keyboardOptions = keyboardOptions,
        keyboardActions = keyboardActions,
        shape = MaterialTheme.shapes.large
    )

    errorRes?.let {
        Text(
            modifier = Modifier.fillMaxWidth(),
            text = stringResource(id = it),
            color = MaterialTheme.colors.error,
            textAlign = TextAlign.End,
            style = MaterialTheme.typography.caption
        )
    }
}