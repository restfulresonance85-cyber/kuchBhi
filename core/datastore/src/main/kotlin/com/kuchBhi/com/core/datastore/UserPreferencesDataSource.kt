package com.kuchBhi.com.core.datastore

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.booleanPreferencesKey
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import com.kuchBhi.com.core.model.ThemeConfig
import com.kuchBhi.com.core.model.UserPreferences
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject
import javax.inject.Singleton

private val Context.userPreferencesDataStore: DataStore<Preferences> by preferencesDataStore(
    name = "kuchbhi_user_preferences",
)

@Singleton
class UserPreferencesDataSource @Inject constructor(
    private val dataStore: DataStore<Preferences>,
) {
    val userPreferences: Flow<UserPreferences> = dataStore.data.map { prefs ->
        val themeName = prefs[KEY_THEME_CONFIG] ?: ThemeConfig.FOLLOW_SYSTEM.name
        val themeConfig = runCatching { ThemeConfig.valueOf(themeName) }
            .getOrDefault(ThemeConfig.FOLLOW_SYSTEM)

        UserPreferences(
            themeConfig = themeConfig,
            useDynamicColor = prefs[KEY_USE_DYNAMIC_COLOR] ?: true,
            analyticsEnabled = prefs[KEY_ANALYTICS_ENABLED] ?: true,
        )
    }

    suspend fun setThemeConfig(themeConfig: ThemeConfig) {
        dataStore.edit { prefs ->
            prefs[KEY_THEME_CONFIG] = themeConfig.name
        }
    }

    suspend fun setDynamicColorPreference(useDynamicColor: Boolean) {
        dataStore.edit { prefs ->
            prefs[KEY_USE_DYNAMIC_COLOR] = useDynamicColor
        }
    }

    suspend fun setAnalyticsEnabled(enabled: Boolean) {
        dataStore.edit { prefs ->
            prefs[KEY_ANALYTICS_ENABLED] = enabled
        }
    }

    private companion object {
        val KEY_THEME_CONFIG = stringPreferencesKey("theme_config")
        val KEY_USE_DYNAMIC_COLOR = booleanPreferencesKey("use_dynamic_color")
        val KEY_ANALYTICS_ENABLED = booleanPreferencesKey("analytics_enabled")
    }
}

@Module
@InstallIn(SingletonComponent::class)
object DataStoreModule {

    @Provides
    @Singleton
    fun providesUserPreferencesDataStore(
        @ApplicationContext context: Context,
    ): DataStore<Preferences> = context.userPreferencesDataStore
}
