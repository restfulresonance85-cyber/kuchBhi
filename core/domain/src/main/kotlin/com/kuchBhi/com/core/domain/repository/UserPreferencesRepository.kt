package com.kuchBhi.com.core.domain.repository

import com.kuchBhi.com.core.model.ThemeConfig
import com.kuchBhi.com.core.model.UserPreferences
import kotlinx.coroutines.flow.Flow

/**
 * Contract for reading and mutating user-level preferences.
 */
interface UserPreferencesRepository {
    val userPreferences: Flow<UserPreferences>
    suspend fun setThemeConfig(themeConfig: ThemeConfig)
    suspend fun setDynamicColorPreference(useDynamicColor: Boolean)
    suspend fun setAnalyticsEnabled(enabled: Boolean)
}
