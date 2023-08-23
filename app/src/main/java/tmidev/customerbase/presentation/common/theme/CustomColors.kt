package tmidev.customerbase.presentation.common.theme

import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.Immutable
import androidx.compose.runtime.ReadOnlyComposable
import androidx.compose.runtime.staticCompositionLocalOf
import androidx.compose.ui.graphics.Color

@Immutable
data class CustomColors(
    val activeStatus: Color = Color.Unspecified,
    val inactiveStatus: Color = Color.Unspecified
)

val OnDarkCustomColors = CustomColors(
    activeStatus = Color(color = 0xFF26A69A),
    inactiveStatus = Color(color = 0xFFEF5350)
)

val OnLightCustomColors = CustomColors(
    activeStatus = Color(color = 0xFF00695C),
    inactiveStatus = Color(color = 0xFFC62828)
)

val LocalCustomColors = staticCompositionLocalOf { CustomColors() }

val MaterialTheme.customColors: CustomColors
    @Composable
    @ReadOnlyComposable
    get() = LocalCustomColors.current