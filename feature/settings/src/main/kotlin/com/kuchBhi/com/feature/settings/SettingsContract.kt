package com.kuchBhi.com.feature.settings

import com.kuchBhi.com.core.model.ThemeConfig
import com.kuchBhi.com.core.ui.mvi.UiEffect
import com.kuchBhi.com.core.ui.mvi.UiIntent
import com.kuchBhi.com.core.ui.mvi.UiState

data class SettingsUiState(
    val isLoading: Boolean = true,
    val themeConfig: ThemeConfig = ThemeConfig.FOLLOW_SYSTEM,
    val useDynamicColor: Boolean = true,
    val analyticsEnabled: Boolean = true,
) : UiState

sealed interface SettingsIntent : UiIntent {
    data class SelectTheme(val themeConfig: ThemeConfig) : SettingsIntent
    data class ToggleDynamicColor(val enabled: Boolean) : SettingsIntent
    data class ToggleAnalytics(val enabled: Boolean) : SettingsIntent
    data object NavigateBack : SettingsIntent
}

sealed interface SettingsEffect : UiEffect {
    data object NavigateBack : SettingsEffect
}
