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
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.DarkMode
import androidx.compose.material.icons.rounded.LightMode
import androidx.compose.material.icons.rounded.NavigateBefore
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import tmidev.customerbase.R

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SettingsScreen(
    modifier: Modifier = Modifier,
    navBackToHomeScreen: () -> Unit,
    viewModel: SettingsViewModel = hiltViewModel()
) {
    val state by viewModel.screenState.collectAsStateWithLifecycle()
    val scrollState = rememberScrollState()

    val isThemeDarkMode = state.isAppThemeDarkMode ?: isSystemInDarkTheme()

    val themeIconState =
        if (isThemeDarkMode) Icons.Rounded.LightMode to stringResource(id = R.string.switchAppThemeToLight)
        else Icons.Rounded.DarkMode to stringResource(id = R.string.switchAppThemeToDark)

    LaunchedEffect(key1 = Unit) {
        viewModel.channel.collect { channel ->
            when (channel) {
                is SettingsChannel.NavBackToHomeScreen -> navBackToHomeScreen()
            }
        }
    }

    Scaffold(
        modifier = modifier,
        topBar = {
            TopAppBar(
                title = {
                    Text(text = stringResource(id = R.string.titleSettingsScreen))
                },
                navigationIcon = {
                    IconButton(onClick = viewModel::navBackToHomeScreen) {
                        Icon(
                            imageVector = Icons.Rounded.NavigateBefore,
                            contentDescription = stringResource(id = R.string.navigateBack)
                        )
                    }
                }
            )
        }
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues = innerPadding)
                .verticalScroll(state = scrollState)
        ) {
            ComposeSwitchTheme(
                currentThemeIcon = themeIconState.first,
                currentThemeName = themeIconState.second
            ) {
                viewModel.updateAppTheme(currentMode = isThemeDarkMode)
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
                .padding(all = 16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Icon(
                imageVector = currentThemeIcon,
                contentDescription = currentThemeName
            )

            Spacer(modifier = Modifier.width(width = 16.dp))

            Text(
                modifier = Modifier.weight(weight = 1F),
                text = currentThemeName,
                style = MaterialTheme.typography.bodyLarge
            )
        }
    }
}