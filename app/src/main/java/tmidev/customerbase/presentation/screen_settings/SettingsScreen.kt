package tmidev.customerbase.presentation.screen_settings

import androidx.compose.foundation.clickable
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.Icon
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Scaffold
import androidx.compose.material.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.DarkMode
import androidx.compose.material.icons.rounded.LightMode
import androidx.compose.material.rememberScaffoldState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.hilt.navigation.compose.hiltViewModel
import tmidev.customerbase.R
import tmidev.customerbase.presentation.common.AppTopBarWithBack
import tmidev.customerbase.presentation.common.theme.spacing

@Composable
fun SettingsScreen(
    onNavBackToHomeScreen: () -> Unit,
    viewModel: SettingsViewModel = hiltViewModel()
) {
    val state by viewModel.state
    val scaffoldState = rememberScaffoldState()
    val scrollState = rememberScrollState()

    val isAppThemeDarkMode = state.isAppThemeDarkMode ?: isSystemInDarkTheme()

    val themeInfoState = if (isAppThemeDarkMode)
        Icons.Rounded.LightMode to stringResource(id = R.string.switchAppThemeToLight)
    else Icons.Rounded.DarkMode to stringResource(id = R.string.switchAppThemeToDark)

    LaunchedEffect(key1 = LocalContext.current) {
        viewModel.channel.collect { channel ->
            when (channel) {
                is SettingsChannel.NavBackToHomeScreen -> onNavBackToHomeScreen()
            }
        }
    }

    Scaffold(
        modifier = Modifier.fillMaxSize(),
        scaffoldState = scaffoldState,
        topBar = {
            AppTopBarWithBack(title = R.string.settings) {
                viewModel.onAction(action = SettingsAction.NavBackToHomeScreen)
            }
        }
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .verticalScroll(state = scrollState)
        ) {
            ComposeSwitchTheme(
                currentThemeIcon = themeInfoState.first,
                currentThemeName = themeInfoState.second
            ) {
                viewModel.onAction(
                    action = SettingsAction.AppThemeChanged(isDarkTheme = !isAppThemeDarkMode)
                )
            }
        }
    }
}

@Composable
private fun ComposeSwitchTheme(
    currentThemeIcon: ImageVector,
    currentThemeName: String,
    onSwitchTheme: () -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .clickable { onSwitchTheme() }
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(all = MaterialTheme.spacing.medium),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Icon(
                imageVector = currentThemeIcon,
                contentDescription = currentThemeName,
                tint = MaterialTheme.colors.onBackground
            )

            Spacer(modifier = Modifier.width(width = MaterialTheme.spacing.medium))

            Text(
                modifier = Modifier.weight(weight = 1F),
                text = currentThemeName,
                color = MaterialTheme.colors.onBackground,
                style = MaterialTheme.typography.body1
            )
        }
    }
}