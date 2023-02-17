package com.carmabs.ema.presentation.ui.profile

import androidx.activity.ComponentActivity
import androidx.navigation.NavBackStackEntry
import androidx.navigation.NavController
import com.carmabs.ema.android.compose.navigation.EmaComposableNavigator

class ProfileNavigator(
    activity: ComponentActivity,
    navController: NavController,
    navBackStackEntry: NavBackStackEntry
) : EmaComposableNavigator<ProfileDestination>(
    activity = activity,
    navController = navController,
    navBackStackEntry = navBackStackEntry
) {
    override fun navigate(navigationTarget: ProfileDestination){

    }
}
