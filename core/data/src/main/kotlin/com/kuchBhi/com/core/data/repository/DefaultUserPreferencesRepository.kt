package com.kuchBhi.com.core.data.repository

import com.kuchBhi.com.core.datastore.UserPreferencesDataSource
import com.kuchBhi.com.core.domain.repository.UserPreferencesRepository
import com.kuchBhi.com.core.model.ThemeConfig
import com.kuchBhi.com.core.model.UserPreferences
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class DefaultUserPreferencesRepository @Inject constructor(
    private val preferencesDataSource: UserPreferencesDataSource,
) : UserPreferencesRepository {

    override val userPreferences: Flow<UserPreferences> = preferencesDataSource.userPreferences

    override suspend fun setThemeConfig(themeConfig: ThemeConfig) {
        preferencesDataSource.setThemeConfig(themeConfig)
    }

    override suspend fun setDynamicColorPreference(useDynamicColor: Boolean) {
        preferencesDataSource.setDynamicColorPreference(useDynamicColor)
    }

    override suspend fun setAnalyticsEnabled(enabled: Boolean) {
        preferencesDataSource.setAnalyticsEnabled(enabled)
    }
}
