package com.carmabs.ema.compose.navigation

import android.app.Activity
import androidx.navigation.NavController
import com.carmabs.ema.core.navigator.EmaDestination
import com.carmabs.ema.core.navigator.EmaNavigator


class EmaComposableNavigatorEmpty<D : EmaDestination>(
    private val activity: Activity,
    private val navController: NavController
) : EmaNavigator<D> {

    override fun navigate(destination: D): Boolean = false

    override fun navigateBack(): Boolean {
        val hasMoreBackScreens = navController.popBackStack()
        if (!hasMoreBackScreens)
            activity.finish()

        return hasMoreBackScreens
    }
}