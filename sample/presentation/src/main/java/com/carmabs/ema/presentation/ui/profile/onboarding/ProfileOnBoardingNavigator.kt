package com.carmabs.ema.presentation.ui.profile.onboarding

import androidx.activity.ComponentActivity
import androidx.navigation.NavBackStackEntry
import androidx.navigation.NavController
import com.carmabs.ema.compose.extension.navigate
import com.carmabs.ema.compose.extension.routeId
import com.carmabs.ema.compose.navigation.EmaComposableNavigator
import com.carmabs.ema.presentation.ui.profile.creation.ProfileCreationScreenContent

class ProfileOnBoardingNavigator(
    activity: ComponentActivity,
    navController: NavController,
    navBackStackEntry: NavBackStackEntry
) : EmaComposableNavigator<ProfileOnBoardingDestination>(
    activity = activity,
    navController = navController,
    navBackStackEntry = navBackStackEntry
) {
    override fun navigate(destination: ProfileOnBoardingDestination) {
        when (destination) {
            is ProfileOnBoardingDestination.ProfileCreation -> {
                navController.navigate(
                    ProfileCreationScreenContent::class.routeId(),
                    destination.initializer
                )
            }
        }

    }
}
