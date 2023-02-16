package com.carmabs.ema.android.compose.navigation

import androidx.activity.ComponentActivity
import androidx.navigation.NavController
import com.carmabs.ema.core.navigator.EmaDestination
import com.carmabs.ema.core.navigator.EmaNavigator


abstract class EmaComposeNavigator<D : EmaDestination>(
    protected val activity:ComponentActivity,
    protected val navController: NavController
) : EmaNavigator<D> {


    override fun navigateBack(): Boolean {
        val hasMoreBackScreens = navController.popBackStack()
        if (!hasMoreBackScreens)
            activity.finish()

        return hasMoreBackScreens
    }
}