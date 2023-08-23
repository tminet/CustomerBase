package tmidev.customerbase.presentation.common.theme

import android.os.Build
import androidx.compose.foundation.background
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.dynamicDarkColorScheme
import androidx.compose.material3.dynamicLightColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.DisposableEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.luminance
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.google.accompanist.systemuicontroller.SystemUiController
import com.google.accompanist.systemuicontroller.rememberSystemUiController
import tmidev.customerbase.R
import tmidev.customerbase.presentation.common.AppLoadingAnimation

@Composable
fun AppTheme(
    systemUiController: SystemUiController = rememberSystemUiController(),
    useDarkColors: Boolean = isSystemInDarkTheme(),
    useDynamicColors: Boolean = true,
    content: @Composable () -> Unit
) {
    val colorScheme = when {
        Build.VERSION.SDK_INT >= Build.VERSION_CODES.S && useDynamicColors -> {
            if (useDarkColors) dynamicDarkColorScheme(context = LocalContext.current)
            else dynamicLightColorScheme(context = LocalContext.current)
        }

        useDarkColors -> darkColorScheme()
        else -> lightColorScheme()
    }

    val customColors = if (useDarkColors) OnDarkCustomColors else OnLightCustomColors

    DisposableEffect(
        key1 = systemUiController,
        key2 = useDarkColors,
        key3 = useDynamicColors
    ) {
        systemUiController.setSystemBarsColor(
            color = Color.Transparent,
            darkIcons = colorScheme.background.luminance() > 0.5,
            isNavigationBarContrastEnforced = false,
            transformColorForLightContent = { Color.Black.copy(alpha = 0.6F) }
        )

        onDispose { }
    }

    CompositionLocalProvider(
        LocalCustomColors provides customColors
    ) {
        MaterialTheme(
            colorScheme = colorScheme,
            typography = Typography,
            content = {
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = colorScheme.background,
                    content = content
                )
            }
        )
    }
}

@Composable
fun SplashTheme(
    systemUiController: SystemUiController = rememberSystemUiController()
) {
    val colorScheme = if (isSystemInDarkTheme()) darkColorScheme() else lightColorScheme()

    DisposableEffect(
        key1 = systemUiController,
        key2 = colorScheme
    ) {
        systemUiController.setSystemBarsColor(
            color = Color.Transparent,
            darkIcons = colorScheme.background.luminance() > 0.5,
            isNavigationBarContrastEnforced = false,
            transformColorForLightContent = { Color.Black.copy(alpha = 0.6F) }
        )

        onDispose { }
    }

    MaterialTheme(
        colorScheme = colorScheme,
        typography = Typography,
        content = {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .background(color = colorScheme.background),
                verticalArrangement = Arrangement.Center,
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                AppLoadingAnimation()

                Spacer(modifier = Modifier.height(height = 16.dp))

                Text(
                    text = stringResource(id = R.string.appName),
                    color = colorScheme.onBackground,
                    style = MaterialTheme.typography.headlineMedium
                )
            }
        }
    )
}