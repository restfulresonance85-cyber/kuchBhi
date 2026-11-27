package com.kuchBhi.com.feature.settings.navigation

import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavOptions
import androidx.navigation.compose.composable
import com.kuchBhi.com.feature.settings.SettingsRoute
import kotlinx.serialization.Serializable

@Serializable
data object SettingsRouteDestination

fun NavController.navigateToSettings(navOptions: NavOptions? = null) {
    navigate(SettingsRouteDestination, navOptions)
}

fun NavGraphBuilder.settingsScreen(
    onNavigateBack: () -> Unit,
) {
    composable<SettingsRouteDestination> {
        SettingsRoute(onNavigateBack = onNavigateBack)
    }
}
