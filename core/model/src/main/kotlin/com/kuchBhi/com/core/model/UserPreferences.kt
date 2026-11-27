package com.kuchBhi.com.core.model

/**
 * User-configurable appearance theme.
 */
enum class ThemeConfig {
    FOLLOW_SYSTEM,
    LIGHT,
    DARK,
}

/**
 * Aggregated user preferences stored locally.
 */
data class UserPreferences(
    val themeConfig: ThemeConfig = ThemeConfig.FOLLOW_SYSTEM,
    val useDynamicColor: Boolean = true,
    val analyticsEnabled: Boolean = true,
)
