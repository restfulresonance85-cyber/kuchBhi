package com.kuchBhi.com.feature.home.navigation

import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavOptions
import androidx.navigation.compose.composable
import com.kuchBhi.com.feature.home.HomeRoute
import kotlinx.serialization.Serializable

@Serializable
data object HomeRouteDestination

fun NavController.navigateToHome(navOptions: NavOptions? = null) {
    navigate(HomeRouteDestination, navOptions)
}

fun NavGraphBuilder.homeScreen(
    onNavigateToSettings: () -> Unit,
) {
    composable<HomeRouteDestination> {
        HomeRoute(onNavigateToSettings = onNavigateToSettings)
    }
}
