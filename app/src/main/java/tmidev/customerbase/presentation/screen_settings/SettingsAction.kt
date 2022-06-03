package tmidev.customerbase.presentation.screen_settings

sealed class SettingsAction {
    object NavBackToHomeScreen : SettingsAction()
    data class AppThemeChanged(val isDarkTheme: Boolean) : SettingsAction()
}