package com.carmabs.ema.compose.navigation

import android.app.Activity
import androidx.navigation.NavBackStackEntry
import androidx.navigation.NavController
import com.carmabs.ema.core.navigator.EmaDestination
import com.carmabs.ema.core.navigator.EmaNavigator


abstract class EmaComposableNavigator<D : EmaDestination>(
    protected val activity: Activity,
    protected val navController: NavController,
    protected val navBackStackEntry: NavBackStackEntry
) : EmaNavigator<D> {

    override fun navigateBack(): Boolean {
        val hasMoreBackScreens = navController.popBackStack()
        if (!hasMoreBackScreens)
            activity.finish()

        return hasMoreBackScreens
    }
}