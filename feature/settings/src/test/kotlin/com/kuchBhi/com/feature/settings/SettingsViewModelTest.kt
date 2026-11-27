package com.kuchBhi.com.feature.settings

import app.cash.turbine.test
import com.google.common.truth.Truth.assertThat
import com.kuchBhi.com.core.domain.usecase.ObserveUserPreferencesUseCase
import com.kuchBhi.com.core.domain.usecase.UpdateAnalyticsPreferenceUseCase
import com.kuchBhi.com.core.domain.usecase.UpdateDynamicColorUseCase
import com.kuchBhi.com.core.domain.usecase.UpdateThemeConfigUseCase
import com.kuchBhi.com.core.model.ThemeConfig
import com.kuchBhi.com.core.testing.MainDispatcherRule
import com.kuchBhi.com.core.testing.fake.FakeUserPreferencesRepository
import kotlinx.coroutines.test.runTest
import org.junit.Rule
import org.junit.Test

class SettingsViewModelTest {

    @get:Rule
    val mainDispatcherRule = MainDispatcherRule()

    @Test
    fun `selecting dark theme updates uiState`() = runTest {
        val repository = FakeUserPreferencesRepository()
        val viewModel = createViewModel(repository)

        viewModel.onIntent(SettingsIntent.SelectTheme(ThemeConfig.DARK))

        assertThat(viewModel.uiState.value.themeConfig).isEqualTo(ThemeConfig.DARK)
    }

    @Test
    fun `toggling dynamic color updates preference state`() = runTest {
        val repository = FakeUserPreferencesRepository()
        val viewModel = createViewModel(repository)

        viewModel.onIntent(SettingsIntent.ToggleDynamicColor(false))

        assertThat(viewModel.uiState.value.useDynamicColor).isFalse()
    }

    @Test
    fun `navigateBack intent emits NavigateBack effect`() = runTest {
        val repository = FakeUserPreferencesRepository()
        val viewModel = createViewModel(repository)

        viewModel.effects.test {
            viewModel.onIntent(SettingsIntent.NavigateBack)
            assertThat(awaitItem()).isEqualTo(SettingsEffect.NavigateBack)
        }
    }

    private fun createViewModel(repository: FakeUserPreferencesRepository): SettingsViewModel =
        SettingsViewModel(
            observeUserPreferences = ObserveUserPreferencesUseCase(repository),
            updateThemeConfig = UpdateThemeConfigUseCase(repository),
            updateDynamicColor = UpdateDynamicColorUseCase(repository),
            updateAnalyticsPreference = UpdateAnalyticsPreferenceUseCase(repository),
        )
}
