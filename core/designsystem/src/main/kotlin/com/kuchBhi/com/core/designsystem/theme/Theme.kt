package com.kuchBhi.com.core.designsystem.theme

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.dynamicDarkColorScheme
import androidx.compose.material3.dynamicLightColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.platform.LocalContext

private val LightColorScheme = lightColorScheme(
    primary = Indigo40,
    primaryContainer = IndigoContainerLight,
    secondary = Teal40,
    secondaryContainer = TealContainerLight,
    tertiary = Amber40,
    background = Neutral99,
    surface = Neutral99,
    onBackground = Neutral10,
    onSurface = Neutral10,
    error = Error40,
)

private val DarkColorScheme = darkColorScheme(
    primary = Indigo80,
    primaryContainer = IndigoContainerDark,
    secondary = Teal80,
    secondaryContainer = TealContainerDark,
    tertiary = Amber80,
    background = Neutral10,
    surface = Neutral10,
    onBackground = Neutral90,
    onSurface = Neutral90,
    error = Error80,
)

/**
 * Enterprise Material 3 Theme for KuchBhi (Android 15 / API 35+).
 * Dynamic color is natively supported on API 31+ (guaranteed on minSdk 35).
 */
@Composable
fun KuchBhiTheme(
    darkTheme: Boolean = isSystemInDarkTheme(),
    dynamicColor: Boolean = true,
    content: @Composable () -> Unit,
) {
    val context = LocalContext.current
    val colorScheme = when {
        dynamicColor && darkTheme -> dynamicDarkColorScheme(context)
        dynamicColor && !darkTheme -> dynamicLightColorScheme(context)
        darkTheme -> DarkColorScheme
        else -> LightColorScheme
    }

    MaterialTheme(
        colorScheme = colorScheme,
        typography = KuchBhiTypography,
        shapes = KuchBhiShapes,
        content = content,
    )
}
