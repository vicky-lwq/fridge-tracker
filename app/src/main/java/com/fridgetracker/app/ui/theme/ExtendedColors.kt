package com.fridgetracker.app.ui.theme

import androidx.compose.runtime.compositionLocalOf
import androidx.compose.ui.graphics.Color

/**
 * "warning" is a semantic role M3's standard ColorScheme doesn't cover (used for the
 * "即将过期" badge). Exposed via CompositionLocal instead of overloading `tertiary`.
 */
data class ExtendedColorScheme(
    val warning: Color,
    val onWarning: Color,
    val warningContainer: Color,
    val onWarningContainer: Color
)

val LightExtendedColors = ExtendedColorScheme(
    warning = md_theme_light_warning,
    onWarning = md_theme_light_onWarning,
    warningContainer = md_theme_light_warningContainer,
    onWarningContainer = md_theme_light_onWarningContainer
)

val DarkExtendedColors = ExtendedColorScheme(
    warning = md_theme_dark_warning,
    onWarning = md_theme_dark_onWarning,
    warningContainer = md_theme_dark_warningContainer,
    onWarningContainer = md_theme_dark_onWarningContainer
)

val LocalExtendedColors = compositionLocalOf { LightExtendedColors }
