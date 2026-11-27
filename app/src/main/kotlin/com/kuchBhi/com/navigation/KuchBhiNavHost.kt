package com.kuchBhi.com.navigation

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.rememberNavController
import com.kuchBhi.com.feature.home.navigation.HomeRouteDestination
import com.kuchBhi.com.feature.home.navigation.homeScreen
import com.kuchBhi.com.feature.settings.navigation.navigateToSettings
import com.kuchBhi.com.feature.settings.navigation.settingsScreen

@Composable
fun KuchBhiNavHost(
    modifier: Modifier = Modifier,
    navController: NavHostController = rememberNavController(),
) {
    NavHost(
        navController = navController,
        startDestination = HomeRouteDestination,
        modifier = modifier,
    ) {
        homeScreen(
            onNavigateToSettings = { navController.navigateToSettings() },
        )
        settingsScreen(
            onNavigateBack = { navController.popBackStack() },
        )
    }
}
