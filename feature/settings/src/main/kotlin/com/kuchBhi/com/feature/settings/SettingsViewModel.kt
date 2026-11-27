package com.kuchBhi.com.feature.settings

import androidx.lifecycle.viewModelScope
import com.kuchBhi.com.core.domain.usecase.ObserveUserPreferencesUseCase
import com.kuchBhi.com.core.domain.usecase.UpdateAnalyticsPreferenceUseCase
import com.kuchBhi.com.core.domain.usecase.UpdateDynamicColorUseCase
import com.kuchBhi.com.core.domain.usecase.UpdateThemeConfigUseCase
import com.kuchBhi.com.core.ui.mvi.BaseMviViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class SettingsViewModel @Inject constructor(
    private val observeUserPreferences: ObserveUserPreferencesUseCase,
    private val updateThemeConfig: UpdateThemeConfigUseCase,
    private val updateDynamicColor: UpdateDynamicColorUseCase,
    private val updateAnalyticsPreference: UpdateAnalyticsPreferenceUseCase,
) : BaseMviViewModel<SettingsUiState, SettingsIntent, SettingsEffect>(
    initialState = SettingsUiState(),
) {

    init {
        viewModelScope.launch {
            observeUserPreferences().collect { prefs ->
                updateState {
                    copy(
                        isLoading = false,
                        themeConfig = prefs.themeConfig,
                        useDynamicColor = prefs.useDynamicColor,
                        analyticsEnabled = prefs.analyticsEnabled,
                    )
                }
            }
        }
    }

    override fun onIntent(intent: SettingsIntent) {
        when (intent) {
            is SettingsIntent.SelectTheme -> {
                viewModelScope.launch { updateThemeConfig(intent.themeConfig) }
            }
            is SettingsIntent.ToggleDynamicColor -> {
                viewModelScope.launch { updateDynamicColor(intent.enabled) }
            }
            is SettingsIntent.ToggleAnalytics -> {
                viewModelScope.launch { updateAnalyticsPreference(intent.enabled) }
            }
            SettingsIntent.NavigateBack -> {
                sendEffect(SettingsEffect.NavigateBack)
            }
        }
    }
}
