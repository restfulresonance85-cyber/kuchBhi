package com.kuchBhi.com.core.domain.usecase

import com.kuchBhi.com.core.domain.repository.UserPreferencesRepository
import com.kuchBhi.com.core.model.ThemeConfig
import com.kuchBhi.com.core.model.UserPreferences
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class ObserveUserPreferencesUseCase @Inject constructor(
    private val userPreferencesRepository: UserPreferencesRepository,
) {
    operator fun invoke(): Flow<UserPreferences> = userPreferencesRepository.userPreferences
}

class UpdateThemeConfigUseCase @Inject constructor(
    private val userPreferencesRepository: UserPreferencesRepository,
) {
    suspend operator fun invoke(themeConfig: ThemeConfig) {
        userPreferencesRepository.setThemeConfig(themeConfig)
    }
}

class UpdateDynamicColorUseCase @Inject constructor(
    private val userPreferencesRepository: UserPreferencesRepository,
) {
    suspend operator fun invoke(enabled: Boolean) {
        userPreferencesRepository.setDynamicColorPreference(enabled)
    }
}

class UpdateAnalyticsPreferenceUseCase @Inject constructor(
    private val userPreferencesRepository: UserPreferencesRepository,
) {
    suspend operator fun invoke(enabled: Boolean) {
        userPreferencesRepository.setAnalyticsEnabled(enabled)
    }
}
